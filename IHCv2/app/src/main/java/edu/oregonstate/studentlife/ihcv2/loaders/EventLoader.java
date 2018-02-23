package edu.oregonstate.studentlife.ihcv2.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Omeed on 2/22/18.
 */

public class EventLoader extends AsyncTaskLoader<String> {
    private final static String TAG = EventLoader.class.getSimpleName();
    final static String IHC_GET_EVENTS_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getevents.php";
    private String eventJSON;

    public EventLoader(Context context) {
        super(context);
    }

    @Override
    public void onStartLoading() {
        if (eventJSON != null) {
            Log.d(TAG, "loader returning cached results");
            deliverResult(eventJSON);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "getting events with URL: " + IHC_GET_EVENTS_URL);
        try {
            URL url = new URL(IHC_GET_EVENTS_URL);

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
        eventJSON = data;
        super.deliverResult(data);
    }

}
