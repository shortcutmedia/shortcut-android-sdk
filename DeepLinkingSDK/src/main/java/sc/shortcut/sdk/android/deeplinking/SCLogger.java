package sc.shortcut.sdk.android.deeplinking;

import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Logger class only logs out message if logLevel high enough.
 */
public class SCLogger {

    public static final String LOG_TAG = SCDeepLinking.class.getSimpleName();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOG_LEVEL_VERBOSE, LOG_LEVEL_DEBUG, LOG_LEVEL_INFO, LOG_LEVEL_WARN, LOG_LEVEL_ERROR})
    public @interface LogLevel {}


    // LogLevel constants
    public static final int LOG_LEVEL_VERBOSE = 2;
    public static final int LOG_LEVEL_DEBUG = 3;
    public static final int LOG_LEVEL_INFO = 4;
    public static final int LOG_LEVEL_WARN = 5;
    public static final int LOG_LEVEL_ERROR = 6;

    private @LogLevel int mLogLevel = LOG_LEVEL_INFO;

    public @LogLevel int getLogLevel() {
        return mLogLevel;
    }

    public void setLogLevel(@LogLevel int logLevel) {
        mLogLevel = logLevel;
    }

    public void debug(String message) {
        if (mLogLevel <= LOG_LEVEL_DEBUG) {
            Log.d(LOG_TAG, message);
        }
    }

    public void warning(String message) {
        if (mLogLevel <= LOG_LEVEL_WARN) {
            Log.w(LOG_TAG, message);
        }
    }

    public static int fromString(String logLevelStr) {
        if (logLevelStr != null) {
            switch (logLevelStr) {
                case "verbose":
                    return LOG_LEVEL_VERBOSE;
                case "debug":
                    return LOG_LEVEL_DEBUG;
                case "info":
                    return LOG_LEVEL_INFO;
                case "warn":
                    return LOG_LEVEL_WARN;
                case "error":
                    return LOG_LEVEL_ERROR;
            }
        }
        return -1;
    }
}
