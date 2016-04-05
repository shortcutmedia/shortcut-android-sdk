package sc.shortcut.sdk.android.deeplinking;

class SCServerRequestRegisterFirstOpen extends SCServerRequest {

    private static final String REQUEST_PATH = "/api/v1/deep_links/first_open";

    SCServerRequestRegisterFirstOpen(SCSession session) {
        super(REQUEST_PATH, session);
        setPostData(new SCEnvironment().toJson());
    }

}
