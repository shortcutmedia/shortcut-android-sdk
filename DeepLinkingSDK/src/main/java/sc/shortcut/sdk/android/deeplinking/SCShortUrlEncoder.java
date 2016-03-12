package sc.shortcut.sdk.android.deeplinking;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class SCShortUrlEncoder {

    static final String DEFAULT_BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String mBase;
    private int mBaseNum;


    SCShortUrlEncoder() {
        this(DEFAULT_BASE);
    }

    SCShortUrlEncoder(String base) {
        mBase = base;
        mBaseNum = base.length();
    }

    String getBase() {
        return mBase;
    }

    int getBaseNum() {
        return mBaseNum;
    }

    String encode(long number) {
        int[] convertedNumber = convert(number);
        char[] convertedDigits = new char[convertedNumber.length];
        for(int i = 0; i < convertedNumber.length; i++) {
            convertedDigits[i] = mBase.charAt(convertedNumber[i]);
        }

        return new String(convertedDigits);
    }

    int decode(String string) {
        char[] characters = string.toCharArray();
        int num = 0;
        int len = characters.length;
        int baseNum = getBaseNum();

        for (int i=0; i < len; i++) {
            int index = mBase.indexOf(characters[i]);
            int f = len - 1 - i;
            num = num + (index * (int) Math.pow(baseNum,  f));
        }
        return num;
    }


    int[] convert(long num) {

        int baseNum = getBaseNum();

        List<Integer> converted = new ArrayList<>();
        while (num > 0) {
            long quotient = num / baseNum;
            int reminder = (int) num % baseNum; // baseNum is int so reminder must be int too
            num = quotient;
            converted.add(reminder);
        }

        if (converted.isEmpty()) {
            return new int[] { 0 };
        } else {
            Collections.reverse(converted);
            int[] convertedInts = new int[converted.size()];
            int i = 0;
            for( Iterator<Integer> it = converted.iterator();
                 it.hasNext();
                 convertedInts[i++] = it.next() );
            return convertedInts;
        }
    }
}
