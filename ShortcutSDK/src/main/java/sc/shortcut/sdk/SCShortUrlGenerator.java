package sc.shortcut.sdk;


import android.net.Uri;

import java.util.Random;

class SCShortUrlGenerator {

    static final String DEFAULT_BASE_URL = "http://scm.st";
    static final int SHORTCUT_EPOCH = 1456790400; /* 2016-03-01 00:00:00 (utc) */
    private static final int RANDOM_LENGTH = 2;

    private SCShortUrlEncoder mEncoder;
    private SCTimeHelper mTimeHelper;
    private SCConfig mConfig;


    SCShortUrlGenerator(SCConfig config) {
        this(config, new SCShortUrlEncoder());
    }

    SCShortUrlGenerator(SCConfig config, SCShortUrlEncoder encoder) {
        this(config, encoder, new SCTimeHelper());
    }

    SCShortUrlGenerator(SCConfig config, SCShortUrlEncoder encoder, SCTimeHelper timeHelper) {
        mConfig = config;
        mEncoder = encoder;
        mTimeHelper = timeHelper;
    }

    String generate() {
        Uri builtUri = Uri.parse(getBaseUrl()).buildUpon()
                .appendPath(generateId())
                .build();
        return builtUri.toString();
    }

    String generateId() {
        int random = generateRandom();
        String keyPart = null;

        if (isCustomeDomain()) {
            keyPart = "";
        } else {
            keyPart = rotate(getKeyPart(), random);
        }

        return keyPart +
                SCStringUtils.rjust( mEncoder.encode(random), '0', RANDOM_LENGTH ) +
                encodedTime();
    }

    String encodedTime() {
        return encodedTime(secondsSinceShortcutEpoch());
    }

    String encodedTime(long seconds) {
        return SCStringUtils.rjust(mEncoder.encode(seconds), '0', 5);
    }

    long secondsSinceShortcutEpoch() {
        long now = mTimeHelper.currentTimeMillis() / 1000; // num of seconds since UNIX epoch (utc)
        return  now - SHORTCUT_EPOCH;
    }

    /**
     * Returns a pseudo-random uniformly distributed {@code int}
     * in the half-open range [1, basenum^RANDOM_LENGTH).
     */
    int generateRandom() {
        Random random = new Random();
        return random.nextInt( (int) Math.pow(mEncoder.getBaseNum(), RANDOM_LENGTH) - 1) + 1;
    }

    String rotate(String string, int num) {
        int max_value = (int) Math.pow(mEncoder.getBaseNum(), string.length());
        int decoded = mEncoder.decode(string);
        int value;
        if (decoded + num < 0) {
            value = max_value + decoded + num;
        } else {
            value = decoded + num;
        }
        String encoded = mEncoder.encode( value % max_value);
        return SCStringUtils.rjust(encoded, '0', string.length());
    }


    String getKeyPart() {
        return mConfig.getAuthToken().substring(0, 3);
    }

    boolean isCustomeDomain() {
        return getBaseUrl() != DEFAULT_BASE_URL;
    }

    private String getBaseUrl() {
        String baseUrl = mConfig.getBaseUrl();
        if (baseUrl == null) {
            baseUrl = DEFAULT_BASE_URL;
        }
        return baseUrl;
    }

}
