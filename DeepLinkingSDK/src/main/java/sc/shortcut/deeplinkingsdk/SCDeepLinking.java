package sc.shortcut.deeplinkingsdk;

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
public class SCDeepLinking {

    private static final String LOG_TAG = SCDeepLinking.class.getSimpleName();

    private static final String LINK_ID_KEY= "sc_link_id";

    private static SCDeepLinking sSCDeepLinking;
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


    private SCDeepLinking(Context context) {
        mContext = context;
        mPreference = new SCPreference(context.getApplicationContext());
        mSessions = new HashMap<>();

        if (context instanceof SCDeepLinkingApp) {
            mAutoSessionMode = true;
            setActivityLifeCycleObserver((Application) context.getApplicationContext());
        }

        if (context instanceof Activity) {
            Intent intent = ((Activity) context).getIntent();
            mDeepLinkAtLaunch = getDeepLinkFromIntent(intent);

            // TODO do not strip Deeplink id if prevented by configuration
            mDeepLink = SCUtils.uriStripQueryParameter(mDeepLinkAtLaunch, LINK_ID_KEY);
            intent.setData(mDeepLink);
        }
    }

    public static SCDeepLinking getInstance(Context context) {
        if (sSCDeepLinking == null) {
            sSCDeepLinking = new SCDeepLinking(context);
        }
        return sSCDeepLinking;
    }

    public static SCDeepLinking getInstance() {
        return sSCDeepLinking;
    }

    public void startSession(Intent intent) {
        Activity activity = (Activity) mContext;
        startSession(activity, intent);
    }


    public void startSession(Activity activity, Intent intent) {

        mCurrentActivity = activity;

        mDeepLinkAtLaunch = getDeepLinkFromIntent(intent);

        if (mDeepLinkAtLaunch == null && isFirstLaunch()) {

            generateSessionId();
            SCSession session = mSessions.get(activity);

            // check for deferred deep link
            PostTask postTask = new PostTask();
            postTask.execute(new SCServerRequestRegisterFirstOpen(session));

            try {
                SCServerResponse response = postTask.get(5000, TimeUnit.MILLISECONDS);

                Uri deferredDeepLink = response.getDeepLink();

                if (deferredDeepLink != null) {
                    handleDeepLink(activity, deferredDeepLink);
                    mCurrentSession.setDeepLink(deferredDeepLink);
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
                PostTask postTask = new PostTask();
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
            PostTask postTask = new PostTask();
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


    private class PostTask extends AsyncTask<SCServerRequest, Void, SCServerResponse> {
        @Override
        protected SCServerResponse doInBackground(SCServerRequest... params) {
            SCServerRequest request = params[0];
            JSONObject json = request.doRequest();
            return new SCServerResponse(json);
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
        }

        @Override
        public void onActivityStarted(Activity activity) {
            SCDeepLinking.getInstance(activity).startSession(activity, activity.getIntent());
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            SCDeepLinking.getInstance(activity).stopSession(activity);
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
}
