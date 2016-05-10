package sc.shortcut.sdk;

/**
 * Class for building a Shortcut short link.
 */
public class SCShortLinkBuilder {

    private String mAndroidDeepLink;
    private String mIOSDeepLink;
    private String mWebDeepLink;
    private String mWindowsPhoneDeepLink;


    public SCShortLinkBuilder addDeepLink(String url) {
        addAndroidDeepLink(url);
        addIosDeepLink(url);
        addWindowsPhoneDeepLink(url);
        return this;
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

    public SCShortLinkBuilder addWindowsPhoneDeepLink(String url) {
        mWindowsPhoneDeepLink = url;
        return this;
    }


    public SCShortLinkItem getItem() {
        return new SCShortLinkItem(mWebDeepLink, mAndroidDeepLink, mIOSDeepLink,
                mWindowsPhoneDeepLink);
    }

    public void createShortLink(SCShortLinkCreateListener callback) {
        Shortcut deepLinking = Shortcut.getInstance();
        deepLinking.createShortLink(getItem(), callback);
    }

    public String createShortLink() {
        Shortcut deepLinking = Shortcut.getInstance();
        return deepLinking.createShortLink(getItem());
    }

}
