package sc.shortcut.sdk;

/**
 * Interface class to get the through {@link SCShortLinkCreateListener} generated link back.
 */
public interface SCShortLinkCreateListener {
    /**
     * Callback method is called with the from the Shortcut service retrieved short link.
     * @param shortLink The short link
     */
    void onLinkCreated(String shortLink);
}
