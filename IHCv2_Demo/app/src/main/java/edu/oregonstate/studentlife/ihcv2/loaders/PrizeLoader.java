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

public class PrizeLoader extends AsyncTaskLoader<String> {
    private final static String TAG = PrizeLoader.class.getSimpleName();

    private String prizeJSON;
    private final static String IHC_GET_PRIZES_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getprizes.php";

    public PrizeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (prizeJSON != null) {
            Log.d(TAG, "loader returning cached prizes");
            deliverResult(prizeJSON);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(IHC_GET_PRIZES_URL);

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
        } catch (Exception e) { return new String("Exception: " + e.getMessage()); }
    }

    @Override
    public void deliverResult(String data) {
        prizeJSON = data;
        super.deliverResult(data);
    }

}
