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
import java.util.HashMap;
import java.util.Map;

public class SCServerRequest {

    private static final String TAG = SCServerRequest.class.getSimpleName();

    private static final String REQUEST_BASE_URL = "http://192.168.178.56:3000";

    private static final String JSON_DEVICE_ID_KEY = "sc_device_id";
    private static final String JSON_SESSION_ID_KEY = "sc_session_id";

    private String mRequestUrl;
    private Map<String, String> mPostData;
    private SCPreference mPreference;
    private SCSession mSession;

    public SCServerRequest(String requestPath, SCSession session) {

        Uri requestUri = Uri.parse(REQUEST_BASE_URL).buildUpon()
                .appendEncodedPath(requestPath)
                .build();
        mRequestUrl = requestUri.toString();
        mPreference = SCDeepLinking.getInstance().getPreference();
        mSession = session;
    }

    public JSONObject doRequest() {
        Log.d(TAG, "doRequest " + mRequestUrl);
        try {
            String postData = buildPostData();

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

            Log.d(TAG, buffer.toString());

            JSONObject responseJson = new JSONObject(buffer.toString());
            responseJson.get("uri");

            return responseJson;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    protected Map<String, String> getPostData() {
        return mPostData;
    }

    protected void setPostData(Map<String, String> postData) {
        mPostData = postData;
    }

    private String buildPostData() {
        Map<String, String> postData = getPostData();
        Map<String, String> commonData = getCommonPostData();
        Map<String, String> combinedData = new HashMap<>(postData.size() + commonData.size());
        combinedData.putAll(postData);
        combinedData.putAll(commonData);
        return new JSONObject(combinedData).toString();
    }


    protected Map<String, String> getCommonPostData() {
        Map<String, String> commonParams = new HashMap<>(1);
        commonParams.put(JSON_DEVICE_ID_KEY, mPreference.getDeviceId());
        commonParams.put(JSON_SESSION_ID_KEY, ""+mSession.getId());
        return commonParams;
    }
}
