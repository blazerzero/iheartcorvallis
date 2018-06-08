package edu.oregonstate.studentlife.ihcv2.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Omeed on 3/1/18.
 * Calls the getresources php script to query the ihc_resources_info table for relevant information
 * on every resource in the database. Used to populate the resources page in the app.
 */

public class ResourceLoader extends AsyncTaskLoader<String> {
    private final static String TAG = ResourceLoader.class.getSimpleName();
    final static String IHC_GET_RESOURCES_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getresources.php";
    private String resourceJSON;

    public ResourceLoader(Context context) {
        super(context);
    }

    @Override
    public void onStartLoading() {
        if (resourceJSON != null) {
            Log.d(TAG, "loader returning cached resources");
            deliverResult(resourceJSON);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "getting resources with URL: " + IHC_GET_RESOURCES_URL);
        try {
            URL url = new URL(IHC_GET_RESOURCES_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuffer sb = new StringBuffer("");
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    @Override
    public void deliverResult(String data) {
        resourceJSON = data;
        super.deliverResult(data);
    }
}
