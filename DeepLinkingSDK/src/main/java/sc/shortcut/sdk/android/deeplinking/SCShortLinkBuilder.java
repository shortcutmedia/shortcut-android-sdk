package sc.shortcut.sdk.android.deeplinking;

import android.content.Context;

/**
 * Class for building a Shortcut short link.
 */
public class SCShortLinkBuilder {

    private String mAndroidDeepLink;
    private String mIOSDeepLink;
    private String mWebDeepLink;
    private String mGooglePlayStore;
    private String mAppleAppStore;
    private Context mContext;

    public SCShortLinkBuilder(Context context) {
        mContext = context;
    }


    public SCShortLinkBuilder addAndroidDeepLink(String url) {
        mAndroidDeepLink = url;
        return this;
    }

    public SCShortLinkBuilder addIosDeepLink(String url) {
        mIOSDeepLink = url;
        return this;
    }

    public SCShortLinkBuilder addWebLink(String url) {
        mWebDeepLink = url;
        return this;
    }

    public SCShortLinkBuilder addGooglePlayStoreUrl(String url) {
        mGooglePlayStore = url;
        return this;
    }

    public SCShortLinkBuilder addAppStoreUrl(String url) {
        mAppleAppStore = url;
        return this;
    }

    public SCShortLinkItem getItem() {
        return new SCShortLinkItem(mWebDeepLink, mAndroidDeepLink, mIOSDeepLink, mGooglePlayStore, mAppleAppStore);
    }

    public void createShortLink(SCShortLinkCreateListener callback) {
        SCDeepLinking deepLinking = SCDeepLinking.getInstance();
        deepLinking.createShortLink(getItem(), callback);
    }

}
