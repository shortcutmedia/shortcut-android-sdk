package sc.shortcut.sdk.android.deeplinking;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import sc.shortcut.deeplinkingsdk.R;

/**
 * This class holds the configuration details for the SDK.
 */
public class SCConfig {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ENVIRONMENT_SANDBOX, ENVIRONMENT_PRODUCTION})
    public @interface Environment {}

    // Environment constants
    public static final int ENVIRONMENT_SANDBOX = 0;
    public static final int ENVIRONMENT_PRODUCTION = 1;

    private static final String AUTH_TOKEN_KEY = "sc.shortcut.sdk.deeplinking.authToken";
    private static final String ENVIRONMENT_KEY = "sc.shortcut.sdk.deeplinking.environment";
    private static final String LOG_LEVEL_KEY = "sc.shortcut.sdk.deeplinking.logLevel";

    private String mAuthToken;
    private @Environment int mEnvironment = ENVIRONMENT_PRODUCTION;
    private @SCLogger.LogLevel int mLogLevel = SCLogger.LOG_LEVEL_INFO;



    public SCConfig(String authToken) {
        mAuthToken = authToken;

        // Set default values
        if (mEnvironment == ENVIRONMENT_SANDBOX) {
            mLogLevel = SCLogger.LOG_LEVEL_DEBUG;
        }
    }

    public @SCLogger.LogLevel int getLogLevel() {
        return mLogLevel;
    }

    public void setLogLevel(@SCLogger.LogLevel int logLevel) {
        mLogLevel = logLevel;
    }

    public @Environment int getEnvironment() {
        return mEnvironment;
    }

    public void setEnvironment(@Environment int environment) {
        mEnvironment = environment;
    }

    public String getAuthToken() {
        return mAuthToken;
    }

    public void setAuthToken(String authToken) {
        mAuthToken = authToken;
    }

    public static SCConfig initFromManifest(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String authToken = bundle.getString(AUTH_TOKEN_KEY);
            if (authToken == null) {
                Log.w(SCLogger.LOG_TAG, context.getString(R.string.sc_access_token_not_found));
                return null;
            }

            SCConfig config = new SCConfig(authToken);

            String environment = bundle.getString(ENVIRONMENT_KEY);
            if (environment != null) {
                switch (environment) {
                    case "production":
                        config.setEnvironment(ENVIRONMENT_PRODUCTION);
                        break;
                    case "sandbox":
                        config.setEnvironment(ENVIRONMENT_SANDBOX);
                }
            }

            int logLevel = SCLogger.fromString(bundle.getString(LOG_LEVEL_KEY));
            if (logLevel > -1) {
                //noinspection ResourceType
                config.setLogLevel(logLevel);
            }


            return new SCConfig(authToken);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
