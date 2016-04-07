package sc.shortcut.sdk.android.deeplinking;

/**
 * Created by franco on 21/09/15.
 */
final class SCErrorMessages {

    static final String SANITIZE_NOT_AVAILABLE_MESSAGE =
            "Automatic sanitizing of deep links is only available with API level 14 or above. " +
            "If you wish to use API level below 14 consider disabling automatic sanitizing and " +
             "call SCDeepLink.sanitize(String deepLink) manually.";

    private SCErrorMessages() {}
}
