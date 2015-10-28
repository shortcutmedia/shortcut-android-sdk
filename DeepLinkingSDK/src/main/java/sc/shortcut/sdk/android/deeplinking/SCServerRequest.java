package sc.shortcut.sdk.android.deeplinking;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;

import sc.shortcut.deeplinkingsdk.BuildConfig;


public class SCServerRequest {

    private static final String LOG_TAG = SCServerRequest.class.getSimpleName();

    private static final String JSON_DEVICE_ID_KEY = "sc_device_id";
    private static final String JSON_SESSION_ID_KEY = "sc_session_id";
    private static final String JSON_AUTH_TOKEN_KEY = "token";

    private String mRequestUrl;
    private JSONObject mPostData;
    private SCPreference mPreference;
    private SCSession mSession;
    private SCConfig mConfig;

    public SCServerRequest(String requestPath, SCSession session) {

        Uri requestUri = Uri.parse(BuildConfig.SERVER_BASE_URL).buildUpon()
                .appendEncodedPath(requestPath)
                .build();
        mRequestUrl = requestUri.toString();
        mPreference = SCDeepLinking.getInstance().getPreference();
        mSession = session;
    }

    public JSONObject doRequest() {

        if (!shouldSend()) {
            return null;
        }

        Log.d(LOG_TAG, "doRequest " + mRequestUrl);
        try {
            String postData = buildPostData().toString();

            URL url = new URL(mRequestUrl);

            // Create the request to OpenWeatherMap, and open the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true); // required to upload data via POST
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setFixedLengthStreamingMode(postData.length()); // performance optimization

            // Send POST output.
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(postData);
            out.flush();
            out.close();


            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            StringBuffer buffer = new StringBuffer();


            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            Log.d(LOG_TAG, buffer.toString());

            JSONObject responseJson = new JSONObject(buffer.toString());

            return responseJson;


        } catch (UnknownHostException ignored) {

        } catch (MalformedURLException e) {
            Log.d(LOG_TAG, "MALFORMEDURLEXCEPTION");
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.d(LOG_TAG, "PROTOCOL EXCEPTION");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;

    }

    public JSONObject getPostData() {
        return mPostData;
    }

    public void setPostData(JSONObject postData) {
        mPostData = postData;
    }

    private JSONObject buildPostData() {
        JSONObject json = getCommonPostData();
        SCUtils.appendJson(json, getPostData());

        return json;
    }


    protected JSONObject getCommonPostData() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON_DEVICE_ID_KEY, mPreference.getDeviceId());
            if (mSession != null) {
                json.put(JSON_SESSION_ID_KEY, "" + mSession.getId());
            }
            json.put(JSON_AUTH_TOKEN_KEY, getConfig().getAuthToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;

    }

    /** Returns false if Request is initialized with insufficient or invalid data. */
    protected boolean shouldSend() {
        return true;
    }

    protected SCConfig getConfig() {
        if (mConfig == null) {
            mConfig = SCDeepLinking.getInstance().getConfig();
        }
        return mConfig;
    }

    public void onRequestSucceeded(SCServerResponse response) {
    }

}
