package sc.shortcut.sdk;

import android.app.Application;

/**
 * Application class for Shortcut projects.
 */
public class SCApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SCConfig config = SCConfig.initFromManifest(this);
        if (config != null) {
            Shortcut shortcut = Shortcut.getInstance(config, this);
            shortcut.sendAppOpenEvent();
        }
    }
}
