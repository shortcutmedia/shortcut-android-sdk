package sc.shortcut.deeplinkingsdk;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by franco on 22/09/15.
 */
public class SCServerRequestRegisterOpen extends SCServerRequest {

    private static final String LOG_TAG = SCServerRequestRegisterOpen.class.getSimpleName();

    private static final String REQUEST_PATH = "/api/v1/deep_links/open";

    public static final String JSON_LINK_ID_KEY = "sc_link_id";


    public SCServerRequestRegisterOpen(String linkId) {
        super(REQUEST_PATH);


        // TODO send Environment if needed whereas needed means if not already sent or if data
        // already sent has changed
//        JSONObject json =  new SCEnvironment().toJson()


        JSONObject json = new JSONObject();
        try {
            json.putOpt(JSON_LINK_ID_KEY, linkId);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error while writing environment information to json!");
            e.printStackTrace();
        }

        setPostData(json.toString());
    }

}
