package sc.shortcut.sdk.android.deeplinking;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Main Singleton class.
 */
public class Shortcut {

    private static final String LOG_TAG = Shortcut.class.getSimpleName();

    private static final String LINK_ID_KEY= "sc_link_id";

    private static Shortcut sSCDeepLinking;
    private Context mContext;
    private SCActivityLifecyleObserver mActivityLifecyleObserver;
    private boolean mActivityLifecyleCallbackRegistred;
    private SCPreference mPreference;
    private boolean mAutoSessionMode;
    private boolean mTestMode;

    private Activity mCurrentActivity;
    private Uri mDeepLinkAtLaunch;
    private Uri mDeepLink;
    private SCSession mCurrentSession;
    private Map<Activity, SCSession> mSessions;
    private boolean mDeviceRotated;
    private SCConfig mConfig;


    private Shortcut(SCConfig config, Context context) {
        mConfig = config;
        mContext = context;
        mPreference = new SCPreference(context.getApplicationContext());
        mSessions = new HashMap<>();

        if (context instanceof SCApplication) {
            mAutoSessionMode = true;
            setActivityLifeCycleObserver((Application) context.getApplicationContext());
        }
    }

    public static Shortcut getInstance(SCConfig config, Context context) {
        if (sSCDeepLinking == null) {
            sSCDeepLinking = new Shortcut(config, context);
        }
        return sSCDeepLinking;
    }

    public static Shortcut getInstance() {
        return sSCDeepLinking;
    }

    public void startSession(Intent intent) {
        Activity activity = (Activity) mContext;
        startSession(activity, intent);
    }

    public void createShortLink(SCShortLinkItem item, SCShortLinkCreateListener callback) {
        PostTask postTask = new PostTask(mContext);
        postTask.execute(new SCServerRequestCreateShortLink(mCurrentSession, item, callback));
    }


    public String createOfflineShortLink(SCShortLinkItem item) {
        Log.d(LOG_TAG, "creating short link offline");

        if (SCUtils.isNetworkAvailable(mContext)) {
            SCShortUrlGenerator generator = new SCShortUrlGenerator(mConfig);
            String shortLink = generator.generate();
            item.setShortLink(shortLink);
            createShortLink(item, null);
            return shortLink;
        } else {
            return null;
        }
    }

    public void startSession(Activity activity, Intent intent) {

        mDeepLink = null; // reset deep link
        mCurrentActivity = activity;

        mDeepLinkAtLaunch = getDeepLinkFromIntent(intent);

        if (mDeepLinkAtLaunch == null && isFirstLaunch()) {

            generateSessionId();
            SCSession session = mSessions.get(activity);

            // check for deferred deep link
            PostTask postTask = new PostTask(mContext);
            postTask.execute(new SCServerRequestRegisterFirstOpen(session));

            try {
                if (!postTask.isCancelled()) {
                    SCServerResponse response = postTask.get(5000, TimeUnit.MILLISECONDS);
                    if (response != null) {
                        Uri deferredDeepLink = response.getDeepLink();

                        if (deferredDeepLink != null) {
                            handleDeepLink(activity, deferredDeepLink);
                            mCurrentSession.setDeepLink(deferredDeepLink);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        } else if (mDeepLinkAtLaunch != null) {

            SCSession session = mSessions.get(activity);
            if (session == null) {
                if (!mDeviceRotated) {
                    generateSessionId();
                    mDeviceRotated = false;
                } else {
                    saveSessionId();
                }
            }

            session = mSessions.get(activity);
            if (session != null) {
                session.open();
                handleDeepLink(activity, mDeepLinkAtLaunch);
                PostTask postTask = new PostTask(mContext);
                postTask.execute(new SCServerRequestRegisterOpen(session));
            }
        }

        // clean up
        if (isFirstLaunch()) {
            mPreference.setFirstLuanch(false);
        }
    }

    public void stopSession(Activity activity) {
        SCSession session = mSessions.get(activity);
        if (session != null && !session.isClosed()) {
            session.close();
            PostTask postTask = new PostTask(mContext);
            postTask.execute(new SCServerRequestRegisterClose(session));
        }
    }

    public Uri getDeepLink() {
        return mDeepLink;
    }

    public boolean isFirstLaunch() {
        return mPreference.isFirstLaunch();
    }

    private void handleDeepLink(Activity activity, Uri deepLink) {

        String linkId = deepLink.getQueryParameter(LINK_ID_KEY);

        if (linkId != null) {
            mCurrentActivity = activity;
        }


        // TODO do not strip Deeplink id if prevented by configuration
        mDeepLink = SCUtils.uriStripQueryParameter(deepLink, LINK_ID_KEY);
        activity.getIntent().setData(mDeepLink);
    }

    public void sendAppOpenEvent() {
        PostTask postTask = new PostTask(mContext);
        if (mPreference.isFirstLaunch()) {
            postTask.execute(new SCServerRequestFirstAppOpen());
        } else {
            postTask.execute(new SCServerRequestAppOpen());
        }

    }


    private class PostTask extends AsyncTask<SCServerRequest, Void, SCServerResponse> {
        private SCServerRequest mRequest;
        private Context mContext;

        public PostTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            // There is no point in doing any networking without a network connection.
            // But we do not want to request too many permissions. Therefore network state
            // is checked proactively only if the app has the necessary permissions. The SDK
            // will not request those permissions itself. In case the network check can not
            // be performed in advance an SCServerRequest will fail gracefully.
            if (SCUtils.hasPermission(mContext, Manifest.permission.ACCESS_NETWORK_STATE)) {
                if (!SCUtils.isNetworkAvailable(mContext)) {
                    cancel(true);
                }
            }
        }

        @Override
        protected SCServerResponse doInBackground(SCServerRequest... params) {
            mRequest = params[0];
            JSONObject json = mRequest.doRequest();
            if (mRequest.getStatus() == SCServerRequest.STATUS_SUCCESS) {
                return new SCServerResponse(json);
            } else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(SCServerResponse scServerResponse) {
            if (scServerResponse != null) {
                mRequest.onRequestSucceeded(scServerResponse);
            }
        }
    }

    private Uri getDeepLinkFromIntent(Intent intent) {
        String action = intent.getAction();
        Uri data = intent.getData();
         return Intent.ACTION_VIEW.equals(action) && data != null ? data : null;
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setActivityLifeCycleObserver(Application application) {
        try {
            mActivityLifecyleObserver = new SCActivityLifecyleObserver();
            /* Set an observer for activity life cycle events. */
            application.unregisterActivityLifecycleCallbacks(mActivityLifecyleObserver);
            application.registerActivityLifecycleCallbacks(mActivityLifecyleObserver);
            mActivityLifecyleCallbackRegistred = true;

        } catch (NoSuchMethodError Ex) {
            mActivityLifecyleCallbackRegistred = false;
//            isAutoSessionMode_ = false;
            /* LifeCycleEvents are  available only from API level 14. */
            Log.w(LOG_TAG, SCErrorMessages.SANITIZE_NOT_AVAILABLE_MESSAGE);
        } catch (NoClassDefFoundError Ex) {
            mActivityLifecyleCallbackRegistred = false;
//            isAutoSessionMode_ = false;
            /* LifeCycleEvents are  available only from API level 14. */
            Log.w(LOG_TAG, SCErrorMessages.SANITIZE_NOT_AVAILABLE_MESSAGE);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private class SCActivityLifecyleObserver implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mDeviceRotated = savedInstanceState != null;
            if (!mDeviceRotated) {
                Shortcut.getInstance().startSession(activity, activity.getIntent());
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
//            SCDeepLinking.getInstance(activity).stopSession(activity);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }


    SCPreference getPreference() {
        return mPreference;
    }

    private void generateSessionId() {
        mCurrentSession = new SCSession(mDeepLinkAtLaunch);
        saveSessionId();
    }

    private void saveSessionId() {
        mSessions.put(mCurrentActivity, mCurrentSession);
    }

    public SCConfig getConfig() {
        return mConfig;
    }
}
