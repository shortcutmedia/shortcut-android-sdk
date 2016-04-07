package sc.shortcut.sdk;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

class SCServerRequestCreateShortLink  extends SCServerRequest {

    private static final String REQUEST_PATH = "/api/v1/deep_links/create";
    private static final String JSON_DEEP_LINK_ITEM_KEY = "deep_link_item";
    private static final String JSON_SHORT_URL_RESPONSE_KEY = "short_url";

    private SCShortLinkCreateListener mCallback;

    SCServerRequestCreateShortLink(SCSession session, SCShortLinkItem item) {
        super(REQUEST_PATH, session);

        JSONObject json = new JSONObject();
        try {
            json.put(JSON_DEEP_LINK_ITEM_KEY, item.toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setPostData(json);
    }

    SCServerRequestCreateShortLink(SCSession session, SCShortLinkItem item, SCShortLinkCreateListener callback) {
        this(session, item);
        mCallback = callback;
    }

    void setOnShortLinkCreateListener(SCShortLinkCreateListener callback) {
        mCallback = callback;
    }

    @Override
    void onRequestSucceeded(SCServerResponse response) {
        if (mCallback != null) {
            JSONObject json = response.getJson();
            String shortUrlStr = json.optString(JSON_SHORT_URL_RESPONSE_KEY);
            if (shortUrlStr != null) {
                Uri shortLink = Uri.parse(shortUrlStr);
                mCallback.onLinkCreated(shortLink.toString());
            }
        }
    }
}
