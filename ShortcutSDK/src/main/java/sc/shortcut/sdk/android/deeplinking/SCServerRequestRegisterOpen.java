package sc.shortcut.sdk.android.deeplinking;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


class SCServerRequestRegisterOpen extends SCServerRequest {

    private static final String LOG_TAG = SCServerRequestRegisterOpen.class.getSimpleName();

    private static final String REQUEST_PATH = "/api/v1/deep_links/open";

    static final String JSON_LINK_ID_KEY = "sc_link_id";


    SCServerRequestRegisterOpen(SCSession session) {
        super(REQUEST_PATH, session);


        // TODO send Environment if needed whereas needed means if not already sent or if data
        // already sent has changed
//        JSONObject json =  new SCEnvironment().toJson()


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
