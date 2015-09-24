package sc.shortcut.deeplinkingsdk;

import android.content.Context;

/**
 * Created by franco on 23/09/15.
 */
public class SCExtPreference extends SCPreference{

    public SCExtPreference(Context context) {
        super(context);
    }

    public void reset() {
        getEditor().clear();
        getEditor().commit();
    }

}
