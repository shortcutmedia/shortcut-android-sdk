package sc.shortcut.sdk;

import java.util.Arrays;

class SCStringUtils {

    static String rjust(String string, char padding, int length) {
        if (string == null) {
            return null;
        }

        int paddingNum = length - string.length();
        if (paddingNum < 1 ) {
            return string;
        }

        char[] pads = new char[paddingNum];
        Arrays.fill(pads, padding);

        return new String(pads) + string;
    }
}
