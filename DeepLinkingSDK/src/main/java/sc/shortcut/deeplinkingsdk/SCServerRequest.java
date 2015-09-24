package sc.shortcut.deeplinkingsdk;

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

/**
 * Created by franco on 21/09/15.
 */
public class SCServerRequest {

    private static final String TAG = SCServerRequest.class.getSimpleName();

    protected static final String REQUEST_BASE_URL = "http://192.168.178.56:3000";

    private String mRequestUrl;
    private String mPostData;

    public SCServerRequest(String requestPath) {

        Uri requestUri = Uri.parse(REQUEST_BASE_URL).buildUpon()
                .appendEncodedPath(requestPath)
                .build();
        mRequestUrl = requestUri.toString();
    }

    public JSONObject doRequest() {
        Log.d(TAG, "doRequest " + mRequestUrl);
        try {
            URL url = new URL(mRequestUrl);

            // Create the request to OpenWeatherMap, and open the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true); // required to upload data via POST
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setFixedLengthStreamingMode(mPostData.length()); // performance optimization

            // Send POST output.
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(mPostData);
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

    public String getPostData() {
        return mPostData;
    }

    public void setPostData(String postData) {
        mPostData = postData;
    }
}
