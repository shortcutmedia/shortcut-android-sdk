package sc.shortcut.sdk;

import android.net.Uri;

import org.json.JSONObject;

public class SCServerResponse {

    public static final String JSON_DEEP_LINK_KEY = "uri";

    private JSONObject json;

    public SCServerResponse(JSONObject json) {
        this.json = json;
    }

    public String getDeepLinkString() {
        return json.optString(JSON_DEEP_LINK_KEY);
    }

    public Uri getDeepLink() {
        return Uri.parse(getDeepLinkString());
    }

    public JSONObject getJson() {
        return json;
    }
}
