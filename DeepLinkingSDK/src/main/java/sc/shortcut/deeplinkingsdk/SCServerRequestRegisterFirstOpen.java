package sc.shortcut.deeplinkingsdk;

public class SCServerRequestRegisterFirstOpen extends SCServerRequest {

    private static final String REQUEST_PATH = "/api/v1/deep_links/first_open";

    public SCServerRequestRegisterFirstOpen(SCSession session) {
        super(REQUEST_PATH, session);
        setPostData(new SCEnvironment().toMap());
    }

}
