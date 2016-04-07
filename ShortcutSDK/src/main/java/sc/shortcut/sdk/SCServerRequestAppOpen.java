package sc.shortcut.sdk;

/**
 * HTTP request that sends an app open event.
 */
class SCServerRequestAppOpen extends SCServerRequest {

    private static final String REQUEST_PATH = "/api/v1/mobile_apps/open";

    SCServerRequestAppOpen() {
        super(REQUEST_PATH, null);
    }
}
