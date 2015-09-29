package sc.shortcut.sdk.android.deeplinking;

import android.os.Build;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SCEnvironment {

    public static final String PLATFORM_KEY = "platform";
    public static final String PLATFORM_VALUE = "Android";
    public static final String PLATFORM_VERSION_KEY = "platform_version";
    public static final String PLATFORM_BUILD_KEY = "platform_build";
    public static final String MODEL_KEY = "model";


    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put(PLATFORM_KEY, PLATFORM_VALUE);
        map.put(PLATFORM_VERSION_KEY, Build.VERSION.RELEASE);
        map.put(PLATFORM_BUILD_KEY, Build.ID);
        map.put(MODEL_KEY, Build.MODEL);
        return map;
    }

    public JSONObject toJson() {
        return new JSONObject(toMap());
    }

    @Override
    public String toString() {
        return toJson().toString();
    }
}
