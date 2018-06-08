package edu.oregonstate.studentlife.ihcv2.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Omeed on 3/4/18.
 * Calls the getresource_markers php script to query the ih_resources table for the resource markers
 * to display on the Resource Map
 */

public class MarkerLoader extends AsyncTaskLoader<String> {

    private final static String TAG = MarkerLoader.class.getSimpleName();
    final static String IHC_GET_RESOURCE_MARKERS_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getresource_markers.php";
    private String markerJSON;

    public MarkerLoader(Context context) {
        super(context);
    }

    @Override
    public void onStartLoading() {
        if (markerJSON != null) {
            Log.d(TAG, "loader returning cached markers");
            deliverResult(markerJSON);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(IHC_GET_RESOURCE_MARKERS_URL);

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
        markerJSON = data;
        super.deliverResult(data);
    }
}
