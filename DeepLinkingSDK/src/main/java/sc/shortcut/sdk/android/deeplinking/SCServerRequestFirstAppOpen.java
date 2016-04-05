package sc.shortcut.sdk.android.deeplinking;

/**
 * HTTP request that sends a first app open event.
 */
class SCServerRequestFirstAppOpen extends SCServerRequest {
    private static final String REQUEST_PATH = "/api/v1/mobile_apps/first_open";

    SCServerRequestFirstAppOpen() {
        super(REQUEST_PATH, null);
    }
}
