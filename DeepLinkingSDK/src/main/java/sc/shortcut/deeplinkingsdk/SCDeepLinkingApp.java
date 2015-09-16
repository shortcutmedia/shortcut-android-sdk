package sc.shortcut.deeplinkingsdk;

import android.app.Application;
import android.util.Log;

/**
 * Created by franco on 16/09/15.
 */
public class SCDeepLinkingApp extends Application {
    private static final String LOG_TAG = SCDeepLinkingApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Application is starting");
    }
}
