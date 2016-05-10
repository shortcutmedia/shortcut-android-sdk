package sc.shortcut.sdk;

import org.json.JSONException;
import org.json.JSONObject;

class SCShortLinkItem {

    private static final String URI_KEY = "uri";
    private static final String MOBILE_DEEP_LINK_KEY = "mobile_deep_link";
    private static final String ANDROID_DEEP_LINK_KEY = "android_in_app_url";
    private static final String IOS_DEEP_LINK_KEY = "ios_in_app_url";
    private static final String WINDOWS_PHONE_DEEP_LINK_KEY = "windows_phone_in_app_url";
    private static final String SHORT_LINK_KEY = "short_link";


    private String mAndroidDeepLink;
    private String mIOSDeepLink;
    private String mWebDeepLink;
    private String mWindowsPhoneDeepLink;
    private String mShortLink; // offline generated

    SCShortLinkItem(String webDeepLink, String androidDeepLink, String IOSDeepLink,
                    String windowsPhoneDeepLink) {
        mAndroidDeepLink = androidDeepLink;
        mIOSDeepLink = IOSDeepLink;
        mWindowsPhoneDeepLink = windowsPhoneDeepLink;
        mWebDeepLink = webDeepLink;
    }

    @SuppressWarnings("unused")
    SCShortLinkItem(String webDeepLink, String androidDeepLink, String IOSDeepLink,
                           String windowsPhoneDeepLink, String shortLink) {
        this(webDeepLink, androidDeepLink, IOSDeepLink, windowsPhoneDeepLink);
        mShortLink = shortLink;
    }

    @SuppressWarnings("unused")
    String getAndroidDeepLink() {
        return mAndroidDeepLink;
    }

    @SuppressWarnings("unused")
    String getIOSDeepLink() {
        return mIOSDeepLink;
    }

    @SuppressWarnings("unused")
    String getWebDeepLink() {
        return mWebDeepLink;
    }

    @SuppressWarnings("unused")
    String getWindowsPhoneDeepLink() {
        return mWindowsPhoneDeepLink;
    }

    @SuppressWarnings("unused")
    String getShortLink() {
        return mShortLink;
    }

    void setShortLink(String shortLink) {
        mShortLink = shortLink;
    }

    JSONObject toJson() {
        JSONObject json =  new JSONObject();
        JSONObject jsonDeepLinkData = new JSONObject();
        try {
            jsonDeepLinkData.put(ANDROID_DEEP_LINK_KEY, mAndroidDeepLink);
            jsonDeepLinkData.put(IOS_DEEP_LINK_KEY, mIOSDeepLink);
            jsonDeepLinkData.put(WINDOWS_PHONE_DEEP_LINK_KEY, mWindowsPhoneDeepLink);
            json.put(URI_KEY, mWebDeepLink);
            json.put(SHORT_LINK_KEY, mShortLink);
            json.put(MOBILE_DEEP_LINK_KEY, jsonDeepLinkData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
