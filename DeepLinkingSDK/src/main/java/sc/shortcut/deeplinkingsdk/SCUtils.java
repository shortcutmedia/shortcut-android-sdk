package sc.shortcut.deeplinkingsdk;

import android.net.Uri;

/**
 * Created by franco on 21/09/15.
 */
public class SCUtils {

    public static Uri uriStripQueryParameter(Uri uri, String paramKey) {
        String queryParam = uri.getQueryParameter(paramKey);
        if (queryParam == null) {
            // nothing to strip
            return uri;
        } else {
            String uriString = uri.toString();
            String paramString = paramKey + "=" + queryParam;

            if (uri.getQuery().length() == paramString.length()) {
                paramString = "?" + paramString;
            } else if (uriString.length() - paramString.length() == uriString.indexOf(paramString)) {
                paramString = "&" + paramString;
            } else {
                paramString = paramString + "&";
            }
            return Uri.parse(uriString.replace(paramString, ""));
        }
    }
}
