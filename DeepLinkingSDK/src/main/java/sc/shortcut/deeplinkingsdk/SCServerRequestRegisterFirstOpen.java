package sc.shortcut.deeplinkingsdk;

/**
 * Created by franco on 21/09/15.
 */
public class SCServerRequestRegisterFirstOpen extends SCServerRequest {

    private static final String REQUEST_PATH = "/api/v1/deep_links/first_open";

    public SCServerRequestRegisterFirstOpen() {
        super(REQUEST_PATH);
        setPostData(new SCEnvironment().toString());
    }

}
