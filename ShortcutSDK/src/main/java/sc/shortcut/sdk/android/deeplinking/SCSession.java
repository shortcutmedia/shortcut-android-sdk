package sc.shortcut.sdk.android.deeplinking;

import android.net.Uri;

import java.util.Random;

/**
 * Created by franco on 28/09/15.
 */
public class SCSession {

    private static final String LINK_ID_KEY= "sc_link_id";

    private Uri mDeepLink;
    private int mId;
    private boolean mClosed;
    private String mLinkId;

    public SCSession(Uri deepLink) {
        mDeepLink = deepLink;
        mId = (new Random()).nextInt(Integer.SIZE - 1);
    }

    public void setDeepLink(Uri deepLink) {
        mDeepLink = deepLink;
    }

    public void open() {
        mClosed = false;
    }

    public void close() {
        mClosed = true;
    }

    public boolean isClosed() {
        return mClosed;
    }

    public int getId() {
        return mId;
    }

    public String getLinkId() {
        if (mLinkId == null) {
            mLinkId = mDeepLink.getQueryParameter(LINK_ID_KEY);
        }
        return mLinkId;
    }

    @Override
    public String toString() {
        return "SESSION_ID=" + mId + " SESSION_STATE=" + (mClosed ? "closed" : "open") +
                " DEEP_LINK="+mDeepLink;
    }
}
