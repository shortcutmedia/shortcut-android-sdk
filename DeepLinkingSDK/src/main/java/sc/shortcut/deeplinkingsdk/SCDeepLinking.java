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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 *
 * <p>
 *     When app is started for the first time...
 * </p>
 *
 *
 * Scenarios
 *
 * - App was installed but never opened. It gets then launched by a deep link
 *   ( -> send action 'open' and use deep link. It is not registered as a first open because the
 *   deep link didn't cause the install)
 * -
 *
 */
public class SCDeepLinking {

    private static final String LOG_TAG = SCDeepLinking.class.getSimpleName();

    private static final String LINK_ID_KEY= "link_id";


    private static SCDeepLinking sSCDeepLinking;
    private Context mContext;
    private SCActivityLifecyleObserver mActivityLifecyleObserver;
    private boolean mActivityLifecyleCallbackRegistred;
    private SCPreference mPreference;
    private boolean mAutoSessionMode;
    private boolean mTestMode;



    private Uri mDeepLinkAtLaunch;
    private Uri mDeepLink;
    private String mLinkId;



    private SCDeepLinking(Context context) {
        mContext = context;
        mPreference = new SCPreference(context.getApplicationContext());

        if (context instanceof SCDeepLinkingApp) {
            mAutoSessionMode = true;
            setActivityLifeCycleObserver((Application) context.getApplicationContext());
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



    public void start(Intent intent) {
        Activity activity = (Activity) mContext;
        start(activity, intent);
    }

    public void start(Activity activity, Intent intent) {


//        if (!mAutoSessionMode) {

        mDeepLinkAtLaunch = getDeepLinkFromIntent(intent);

        Log.d(LOG_TAG, "is first launch = " + isFirstLaunch());

        if (mDeepLinkAtLaunch == null && isFirstLaunch()) {
            // check for deferred deep link
            PostTask postTask = new PostTask();
            postTask.execute(new SCServerRequestRegisterFirstOpen());

            try {
                SCServerResponse response = postTask.get(5000, TimeUnit.MILLISECONDS);

                Uri deferredDeepLink = response.getDeepLink();

                if (deferredDeepLink != null) {
                    handleDeepLink(activity, deferredDeepLink);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        } else if (mDeepLinkAtLaunch != null) {
            handleDeepLink(activity, mDeepLinkAtLaunch);
            PostTask postTask = new PostTask();
            postTask.execute(new SCServerRequestRegisterOpen(mLinkId));
        }

        // clean up
        if (isFirstLaunch()) {
            mPreference.setFirstLuanch(false);
        }
    }

    public Uri getDeepLink() {
        return mDeepLink;
    }

    public boolean isFirstLaunch() {
        return mPreference.isFirstLaunch();
    }

    private void handleDeepLink(Activity activity, Uri deepLink) {

        mLinkId = deepLink.getQueryParameter(LINK_ID_KEY);

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
            Log.d(LOG_TAG, "onActivityCreated()");
            SCDeepLinking.getInstance(activity).start(activity, activity.getIntent());
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

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }


}
