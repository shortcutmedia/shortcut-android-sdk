package sc.shortcut.deeplinkingsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by franco on 16/09/15.
 */
public class SCPreference {

    private static final String LOG_TAG = SCPreference.class.getSimpleName();

    private static final String KEY_PREF_FINGERPRINT_ID = "fingerprint_id_key";
    private static final String KEY_PREF_FIRST_LAUNCH = "is_first_launch";
    private static final String SHARED_PREFERENCE_FILE = "shortcut_deeplinking_shared_preferences";


    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private SharedPreferences.Editor mEditor;

    public SCPreference(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE,
                Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

//        mEditor.putString("Hello", "hello");
//        mEditor.commit();

        String hello = mSharedPreferences.getString("Hello", "");

        Log.d(LOG_TAG, "HELLO PREFERENCE IS " + hello);
    }


    public String getFingerprintId() {
//        return getString(KEY_PREF_FINGERPRINT_ID);
        return "";
    }

    public boolean isFirstLaunch() {
        return mSharedPreferences.getBoolean(KEY_PREF_FIRST_LAUNCH, true);
    }

    public void setFirstLuanch(boolean value) {
        setBool(KEY_PREF_FIRST_LAUNCH, value);
    }


    /**
     * <p>Sets the value of the {@link String} key value supplied in preferences.</p>
     *
     * @param key   A {@link String} value containing the key to reference.
     * @param value A {@link Boolean} value to set the preference record to.
     */
    private void setBool(String key, Boolean value) {
        mEditor.putBoolean(key, value).apply();
    }

    protected SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    protected SharedPreferences.Editor getEditor() {
        return mEditor;
    }
}
