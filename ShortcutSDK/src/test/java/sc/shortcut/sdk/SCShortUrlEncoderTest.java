package sc.shortcut.sdk;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SCShortUrlEncoderTest {

    @Test
    public void constructorTest() {
        String base = "abcd";
        SCShortUrlEncoder encoder = new SCShortUrlEncoder(base);
        assertThat(encoder.getBase(), is(base));

        // the base equals th alphabet size
        assertThat(encoder.getBaseNum(), is(4));

        // it defaults to the default alphabet
        encoder = new SCShortUrlEncoder();
        assertThat(encoder.getBase(), is(SCShortUrlEncoder.DEFAULT_BASE));
    }

    @Test
    public void encodeTest() {
        String base = "abcdef";
        SCShortUrlEncoder encoder = new SCShortUrlEncoder(base);
        assertThat(encoder.encode(0), is("a"));
        assertThat(encoder.encode(1), is("b"));
        assertThat(encoder.encode(2), is("c"));
        assertThat(encoder.encode(3), is("d"));
        assertThat(encoder.encode(4), is("e"));
        assertThat(encoder.encode(5), is("f"));
        assertThat(encoder.encode(6), is("ba"));
        assertThat(encoder.encode(7), is("bb"));
        assertThat(encoder.encode(8), is("bc"));
        assertThat(encoder.encode(12), is("ca"));
        assertThat(encoder.encode(35), is("ff"));
        assertThat(encoder.encode(36), is("baa"));
        assertThat(encoder.encode(37), is("bab"));
        assertThat(encoder.encode(38), is("bac"));
        assertThat(encoder.encode(215), is("fff"));
        assertThat(encoder.encode(216), is("baaa"));
        assertThat(encoder.encode(1296), is("baaaa"));
        assertThat(encoder.encode(7776), is("baaaaa"));
    }

    @Test
    public void decodeTest() {
        String base = "abcdef";
        SCShortUrlEncoder encoder = new SCShortUrlEncoder(base);
        assertThat(encoder.decode("a"), is(0));
        assertThat(encoder.decode("b"), is(1));
        assertThat(encoder.decode("c"), is(2));
        assertThat(encoder.decode("d"), is(3));
        assertThat(encoder.decode("e"), is(4));
        assertThat(encoder.decode("f"), is(5));
        assertThat(encoder.decode("ba"), is(6));
        assertThat(encoder.decode("bb"), is(7));
        assertThat(encoder.decode("bc"), is(8));
        assertThat(encoder.decode("ca"), is(12));
        assertThat(encoder.decode("ff"), is(35));
        assertThat(encoder.decode("baa"), is(36));
        assertThat(encoder.decode("bab"), is(37));
        assertThat(encoder.decode("bac"), is(38));
        assertThat(encoder.decode("fff"), is(215));
        assertThat(encoder.decode("baaa"), is(216));
        assertThat(encoder.decode("baaaa"), is(1296));
        assertThat(encoder.decode("baaaaa"), is(7776));
    }

    @Test
    public void base62EncodingTest() {
        String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SCShortUrlEncoder encoder = new SCShortUrlEncoder(base);
        int base_num = encoder.getBaseNum();
        assertThat(encoder.encode((long) Math.pow(base_num, 0) - 1), is("0"));
        assertThat(encoder.encode((long) Math.pow(base_num, 1) - 1), is("Z"));
        assertThat(encoder.encode((long) Math.pow(base_num, 1 - 0)), is("10"));
        assertThat(encoder.encode((long) Math.pow(base_num, 2) - 1), is("ZZ"));
        assertThat(encoder.encode((long) Math.pow(base_num, 2) - 0), is("100"));
        assertThat(encoder.encode((long) Math.pow(base_num, 3) - 1), is("ZZZ"));
        assertThat(encoder.encode((long) Math.pow(base_num, 3) - 0), is("1000"));
        assertThat(encoder.encode((long) Math.pow(base_num, 4) - 1), is("ZZZZ"));
        assertThat(encoder.encode((long) Math.pow(base_num, 4) - 0), is("10000"));
        assertThat(encoder.encode((long) Math.pow(base_num, 5) - 1), is("ZZZZZ"));

        // these values are too long
        assertThat(encoder.encode((long) Math.pow(base_num, 5) - 0), is("100000"));
//        assertThat(encoder.encode((long) Math.pow(base_num, 6) - 1), is("ZZZZZZ"));

    }

    @Test
    public void base62DecodingTest() {
        String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SCShortUrlEncoder encoder = new SCShortUrlEncoder(base);
        int base_num = encoder.getBaseNum();

        assertThat(encoder.decode("0"), is((int) Math.pow(base_num, 0) - 1));
        assertThat(encoder.decode("Z"), is((int) Math.pow(base_num, 1) - 1));
        assertThat(encoder.decode("10"), is((int) Math.pow(base_num, 1) - 0));
        assertThat(encoder.decode("ZZ"), is((int) Math.pow(base_num, 2) - 1));
        assertThat(encoder.decode("100"), is((int) Math.pow(base_num, 2) - 0));
        assertThat(encoder.decode("ZZZ"), is((int) Math.pow(base_num, 3) - 1));
        assertThat(encoder.decode("1000"), is((int) Math.pow(base_num, 3) - 0));
        assertThat(encoder.decode("ZZZZ"), is((int) Math.pow(base_num, 4) - 1));
        assertThat(encoder.decode("10000"), is((int) Math.pow(base_num, 4) - 0));
        assertThat(encoder.decode("ZZZZZ"), is((int) Math.pow(base_num, 5) - 1));
    }


    @Test
    public void convertTest() {
        String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SCShortUrlEncoder encoder = new SCShortUrlEncoder(base);

        assertThat( encoder.convert(0), is(new int[]{0}) );
        assertThat( encoder.convert(1), is(new int[]{1}) );
        assertThat( encoder.convert(61), is(new int[]{61}) );
        assertThat( encoder.convert(62), is(new int[]{1, 0}) );
        assertThat( encoder.convert(63), is(new int[]{1, 1}) );
        assertThat( encoder.convert(124), is(new int[]{2, 0}) );
        assertThat( encoder.convert(3844), is(new int[]{1, 0, 0}) );
        assertThat( encoder.convert(238328), is(new int[]{1, 0, 0, 0}) );
        assertThat( encoder.convert(14776336), is(new int[]{1, 0, 0, 0, 0}) );
        assertThat( encoder.convert(916132831), is(new int[]{61, 61, 61, 61, 61}) );
    }

}
