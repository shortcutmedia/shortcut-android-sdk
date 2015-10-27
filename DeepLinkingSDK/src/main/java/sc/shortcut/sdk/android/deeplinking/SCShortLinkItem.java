package sc.shortcut.sdk.android.deeplinking;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by franco on 22/10/15.
 */
public class SCShortLinkItem {

    private static final String URI_KEY = "uri";
    private static final String MOBILE_DEEP_LINK_KEY = "mobile_deep_link";
    private static final String ANDROID_DEEP_LINK_KEY = "android_in_app_url";
    private static final String ANDROID_PLAY_STORE_KEY = "android_app_store_url";
    private static final String IOS_DEEP_LINK_KEY = "ios_in_app_url";
    private static final String IOS_APP_STORE_KEY = "ios_app_store_url";


    private Uri mAndroidDeepLink;
    private Uri mIOSDeepLink;
    private Uri mWebDeepLink;
    private Uri mGooglePlayStore;
    private Uri mAppleAppStore;

    public SCShortLinkItem() {
    }

    public SCShortLinkItem(Uri webDeepLink) {
        this(webDeepLink, null, null, null, null);
    }

    public SCShortLinkItem(Uri webDeepLink, Uri androidDeepLink, Uri googlePlayStore) {
        this(webDeepLink, androidDeepLink, null, googlePlayStore, null);
    }

    public SCShortLinkItem(Uri webDeepLink, Uri androidDeepLink, Uri IOSDeepLink, Uri googlePlayStore, Uri appleAppStore) {
        mAndroidDeepLink = androidDeepLink;
        mIOSDeepLink = IOSDeepLink;
        mWebDeepLink = webDeepLink;
        mGooglePlayStore = googlePlayStore;
        mAppleAppStore = appleAppStore;
    }

    public Uri getAndroidDeepLink() {
        return mAndroidDeepLink;
    }

    public void setAndroidDeepLink(Uri androidDeepLink) {
        mAndroidDeepLink = androidDeepLink;
    }

    public Uri getIOSDeepLink() {
        return mIOSDeepLink;
    }

    public void setIOSDeepLink(Uri IOSDeepLink) {
        mIOSDeepLink = IOSDeepLink;
    }

    public Uri getWebDeepLink() {
        return mWebDeepLink;
    }

    public void setWebDeepLink(Uri webDeepLink) {
        mWebDeepLink = webDeepLink;
    }

    public Uri getGooglePlayStore() {
        return mGooglePlayStore;
    }

    public void setGooglePlayStore(Uri googlePlayStore) {
        mGooglePlayStore = googlePlayStore;
    }

    public Uri getAppleAppStore() {
        return mAppleAppStore;
    }

    public void setAppleAppStore(Uri appleAppStore) {
        mAppleAppStore = appleAppStore;
    }

    public JSONObject toJson() {
        JSONObject json =  new JSONObject();
        JSONObject jsonDeepLinkData = new JSONObject();
        try {
            jsonDeepLinkData.put(ANDROID_DEEP_LINK_KEY, mAndroidDeepLink.toString());
            jsonDeepLinkData.put(ANDROID_PLAY_STORE_KEY, mGooglePlayStore.toString());
            jsonDeepLinkData.put(IOS_DEEP_LINK_KEY, mIOSDeepLink.toString());
            jsonDeepLinkData.put(IOS_APP_STORE_KEY, mAppleAppStore.toString());
            json.put(URI_KEY, mWebDeepLink);
            json.put(MOBILE_DEEP_LINK_KEY, jsonDeepLinkData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
