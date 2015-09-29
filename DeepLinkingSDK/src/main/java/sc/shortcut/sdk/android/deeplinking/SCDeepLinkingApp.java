package sc.shortcut.sdk.android.deeplinking;

import android.app.Application;

/**
 * Created by franco on 16/09/15.
 */
public class SCDeepLinkingApp extends Application {
    private static final String LOG_TAG = SCDeepLinkingApp.class.getSimpleName();

    private SCDeepLinking mSCDeepLinking;

    @Override
    public void onCreate() {
        super.onCreate();
        mSCDeepLinking = SCDeepLinking.getInstance(this);
    }
}
