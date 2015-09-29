package sc.shortcut.deeplinkingsdk;

import android.content.Context;
import android.content.SharedPreferences;

public class SCPreference {

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
    }


    public boolean isFirstLaunch() {
        return mSharedPreferences.getBoolean(KEY_PREF_FIRST_LAUNCH, true);
    }

    public void setFirstLuanch(boolean value) {
        setBool(KEY_PREF_FIRST_LAUNCH, value);
    }

    public String getDeviceId() {
        return SCUtils.generateDeviceId();
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
