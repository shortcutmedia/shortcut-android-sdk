package sc.shortcut.sdk.android.deeplinking;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SCServerRequestRegisterClose extends SCServerRequest {

    private static final String REQUEST_PATH = "/api/v1/deep_links/close";

    public static final String JSON_LINK_ID_KEY = "sc_link_id";


    public SCServerRequestRegisterClose(SCSession session) {
        super(REQUEST_PATH, session);

        Map<String, String> postData = new HashMap<>();
        postData.put(JSON_LINK_ID_KEY, session.getLinkId());
        setPostData(new JSONObject(postData));
    }

    @Override
    protected boolean shouldSend() {
        // no need to send request without a link_id
        return getPostData().has(JSON_LINK_ID_KEY);
    }

}
