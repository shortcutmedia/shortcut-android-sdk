package sc.shortcut.deeplinkingsdk;

import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by franco on 21/09/15.
 */
public class SCEnvironment {

    public static final String PLATFORM_KEY = "platform";
    public static final String PLATFORM_VALUE = "Android";
    public static final String PLATFORM_VERSION_KEY = "platform_version";
    public static final String PLATFORM_BUILD_KEY = "platform_build";
    public static final String MODEL_KEY = "model";

    private static final String LOG_TAG = SCEnvironment.class.getSimpleName();

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put(PLATFORM_KEY, PLATFORM_VALUE);
            json.put(PLATFORM_VERSION_KEY, Build.VERSION.RELEASE);
            json.put(PLATFORM_BUILD_KEY, Build.ID);
            json.put(MODEL_KEY, Build.MODEL);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error while writing environment information to json!");
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }
}
