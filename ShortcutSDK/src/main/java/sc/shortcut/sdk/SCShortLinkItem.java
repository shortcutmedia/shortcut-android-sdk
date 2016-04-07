package sc.shortcut.sdk;

import org.json.JSONException;
import org.json.JSONObject;

class SCShortLinkItem {

    private static final String URI_KEY = "uri";
    private static final String MOBILE_DEEP_LINK_KEY = "mobile_deep_link";
    private static final String ANDROID_DEEP_LINK_KEY = "android_in_app_url";
    private static final String ANDROID_PLAY_STORE_KEY = "android_app_store_url";
    private static final String IOS_DEEP_LINK_KEY = "ios_in_app_url";
    private static final String IOS_APP_STORE_KEY = "ios_app_store_url";
    private static final String SHORT_LINK_KEY = "short_link";


    private String mAndroidDeepLink;
    private String mIOSDeepLink;
    private String mWebDeepLink;
    private String mGooglePlayStore;
    private String mAppleAppStore;
    private String mShortLink; // offline generated

    SCShortLinkItem() {
    }

    SCShortLinkItem(String webDeepLink) {
        this(webDeepLink, null, null, null, null);
    }

    SCShortLinkItem(String webDeepLink, String androidDeepLink, String googlePlayStore) {
        this(webDeepLink, androidDeepLink, null, googlePlayStore, null);
    }

    SCShortLinkItem(String webDeepLink, String androidDeepLink, String IOSDeepLink,
                           String googlePlayStore, String appleAppStore) {

        this(webDeepLink, androidDeepLink, IOSDeepLink, googlePlayStore, appleAppStore, null);
    }


    SCShortLinkItem(String webDeepLink, String androidDeepLink, String IOSDeepLink,
                           String googlePlayStore, String appleAppStore, String shortLink) {
        mAndroidDeepLink = androidDeepLink;
        mIOSDeepLink = IOSDeepLink;
        mWebDeepLink = webDeepLink;
        mGooglePlayStore = googlePlayStore;
        mAppleAppStore = appleAppStore;
        mShortLink = shortLink;
    }

    String getAndroidDeepLink() {
        return mAndroidDeepLink;
    }

    String getIOSDeepLink() {
        return mIOSDeepLink;
    }

    String getWebDeepLink() {
        return mWebDeepLink;
    }

    String getGooglePlayStore() {
        return mGooglePlayStore;
    }

    String getAppleAppStore() {
        return mAppleAppStore;
    }

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
            jsonDeepLinkData.put(ANDROID_PLAY_STORE_KEY, mGooglePlayStore);
            jsonDeepLinkData.put(IOS_DEEP_LINK_KEY, mIOSDeepLink);
            jsonDeepLinkData.put(IOS_APP_STORE_KEY, mAppleAppStore);
            json.put(URI_KEY, mWebDeepLink);
            json.put(SHORT_LINK_KEY, mShortLink);
            json.put(MOBILE_DEEP_LINK_KEY, jsonDeepLinkData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
