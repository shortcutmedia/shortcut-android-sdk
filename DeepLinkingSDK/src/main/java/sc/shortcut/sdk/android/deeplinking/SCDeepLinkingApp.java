package sc.shortcut.sdk.android.deeplinking;

import android.app.Application;

/**
 * Application class for Shortcut projects.
 */
public class SCDeepLinkingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SCConfig config = SCConfig.initFromManifest(this);
        if (config != null) {
            SCDeepLinking shortcut = SCDeepLinking.getInstance(config, this);
            shortcut.sendAppOpenEvent();
        }
    }
}
