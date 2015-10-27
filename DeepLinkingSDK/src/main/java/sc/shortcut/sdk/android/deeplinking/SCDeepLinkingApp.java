package sc.shortcut.sdk.android.deeplinking;

import android.app.Application;

/**
 * Created by franco on 16/09/15.
 */
public class SCDeepLinkingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SCConfig config = SCConfig.initFromManifest(this);
        if (config != null) {
            SCDeepLinking.getInstance(config, this);
        }
    }
}
