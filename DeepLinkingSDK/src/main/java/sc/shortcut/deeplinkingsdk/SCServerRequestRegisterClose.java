package sc.shortcut.deeplinkingsdk;

import java.util.HashMap;
import java.util.Map;

public class SCServerRequestRegisterClose extends SCServerRequest {

    private static final String REQUEST_PATH = "/api/v1/deep_links/close";

    public static final String JSON_LINK_ID_KEY = "sc_link_id";


    public SCServerRequestRegisterClose(SCSession session) {
        super(REQUEST_PATH, session);

        Map<String, String> postData = new HashMap<>();
        postData.put(JSON_LINK_ID_KEY, session.getLinkId());
        setPostData(postData);
    }
}
