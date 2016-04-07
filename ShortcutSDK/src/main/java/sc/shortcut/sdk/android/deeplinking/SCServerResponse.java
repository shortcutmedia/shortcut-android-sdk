package sc.shortcut.sdk.android.deeplinking;

import android.net.Uri;

import org.json.JSONObject;

/**
 * Created by franco on 21/09/15.
 */
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
