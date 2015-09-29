package sc.shortcut.sdk.android.deeplinking;

import java.util.HashMap;
import java.util.Map;


public class SCServerRequestRegisterOpen extends SCServerRequest {

    private static final String LOG_TAG = SCServerRequestRegisterOpen.class.getSimpleName();

    private static final String REQUEST_PATH = "/api/v1/deep_links/open";

    public static final String JSON_LINK_ID_KEY = "sc_link_id";


    public SCServerRequestRegisterOpen(SCSession session) {
        super(REQUEST_PATH, session);


        // TODO send Environment if needed whereas needed means if not already sent or if data
        // already sent has changed
//        JSONObject json =  new SCEnvironment().toJson()


        Map<String, String> postData = new HashMap<>();
        postData.put(JSON_LINK_ID_KEY, session.getLinkId());
        setPostData(postData);
    }

}
