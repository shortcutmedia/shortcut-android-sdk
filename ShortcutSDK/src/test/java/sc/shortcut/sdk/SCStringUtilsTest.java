package sc.shortcut.sdk;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class SCStringUtilsTest {

    @Test
    public void rjustTest() {
        assertThat( SCStringUtils.rjust(null, '0', 6), isEmptyOrNullString() );
        assertThat( SCStringUtils.rjust("abcdef", '0', 6), is("abcdef") );
        assertThat( SCStringUtils.rjust("abc", '0', 6), is("000abc") );
        assertThat( SCStringUtils.rjust("abcde", '0', 6), is("0abcde") );
        assertThat( SCStringUtils.rjust("", '0', 6), is("000000") );
    }
}
