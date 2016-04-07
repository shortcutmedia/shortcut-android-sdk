package sc.shortcut.sdk;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SCShortcutUrlGeneratorTest {

    @Mock private SCConfig mConfig;


    @Before
    public void setUp() {
        mConfig = new SCConfig("abcd");
    }


    @Test
    public void keyPartTest() {
        SCShortUrlGenerator shortUrlGenerator = new SCShortUrlGenerator(mConfig);
        assertThat(shortUrlGenerator.getKeyPart(), is("abc"));
    }

    @Test
    public void generateIdTest() {
        SCShortUrlGenerator shortUrlGenerator = new SCShortUrlGenerator(mConfig);
        assertThat(shortUrlGenerator.generateId().length(), is(10));
    }

    @Test
    public void generateIdTestWithCustomeDomain() {
        mConfig.setBaseUrl("http://short.li");
        SCShortUrlGenerator shortUrlGenerator = new SCShortUrlGenerator(mConfig);
        assertThat(shortUrlGenerator.generateId().length(), is(7));
    }

    @Test
    public void rotateTest() {
        String base = "0123456789abcdef";
        SCShortUrlEncoder encoder = new SCShortUrlEncoder(base);
        SCShortUrlGenerator shortUrlGenerator = new SCShortUrlGenerator(null, encoder);
        assertThat(shortUrlGenerator.rotate("00", 4), is("04"));
        assertThat(shortUrlGenerator.rotate("ff", 1), is("00"));
        assertThat(shortUrlGenerator.rotate("ff", 4), is("03"));
        assertThat(shortUrlGenerator.rotate("03", -4), is("ff"));
    }

    @Test
    public void secondsSinceShortcutEpochTest() {
        String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SCTimeHelper mockTime = mock(SCTimeHelper.class);
        SCShortUrlEncoder encoder = new SCShortUrlEncoder(base);
        SCShortUrlGenerator shortUrlGenerator = new SCShortUrlGenerator(mConfig, encoder, mockTime);
        when(mockTime.currentTimeMillis()).thenReturn(SCShortUrlGenerator.SHORTCUT_EPOCH * 1000L + 500000L);
        assertThat(shortUrlGenerator.secondsSinceShortcutEpoch(), is(500L));
    }

}
