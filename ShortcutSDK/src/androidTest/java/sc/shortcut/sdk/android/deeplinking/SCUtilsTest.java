package sc.shortcut.sdk.android.deeplinking;

import android.net.Uri;
import android.test.AndroidTestCase;

import sc.shortcut.sdk.android.deeplinking.SCUtils;

public class SCUtilsTest extends AndroidTestCase {

    public void testSanitizeDeepLinkWithNoLinkIdParam() {
        // simple URL with no params
        Uri deepLink = Uri.parse("waypointli://waypoint.li/e94acf2c194221e50eefecd6731e30bf");
        assertEquals( deepLink, SCUtils.uriStripQueryParameter(deepLink, "link_id") );

        // more complex with params
        deepLink = Uri.parse("waypointli://waypoint.li/e94acf2c194221e50eefecd6731e30bf?foo=bar");
        assertEquals(deepLink, SCUtils.uriStripQueryParameter(deepLink, "link_id"));
    }

    public void testSanitizeDeepLinkWithLinkIdParam() {
        Uri deepLink = Uri.parse("waypointli://waypoint.li/e94acf2c194221e50eefecd6731e30bf?link_id=xsba2m");
        Uri expectedLink = Uri.parse("waypointli://waypoint.li/e94acf2c194221e50eefecd6731e30bf");
        assertEquals( expectedLink, SCUtils.uriStripQueryParameter(deepLink, "link_id") );

        deepLink = Uri.parse("waypointli://waypoint.li/e94acf2c194221e50eefecd6731e30bf?foo=bar&link_id=xsba2m");
        expectedLink = Uri.parse("waypointli://waypoint.li/e94acf2c194221e50eefecd6731e30bf?foo=bar");
        assertEquals( expectedLink, SCUtils.uriStripQueryParameter(deepLink, "link_id") );

        deepLink = Uri.parse("waypointli://waypoint.li/e94acf2c194221e50eefecd6731e30bf?link_id=xsba2m&foo=bar");
        expectedLink = Uri.parse("waypointli://waypoint.li/e94acf2c194221e50eefecd6731e30bf?foo=bar");
        assertEquals( expectedLink, SCUtils.uriStripQueryParameter(deepLink, "link_id") );
    }
}
