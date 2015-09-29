package sc.shortcut.deeplinkingsdk;

import android.net.Uri;
import android.os.Build;

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

    /**
     * Generates a pseudo-unique ID according to http://www.pocketmagic.net/android-unique-device-id/
     *
     * @return
     */
    public static String generateDeviceId() {
        return "35" + //we make this look like a valid IMEI
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits
    }
}
