package edu.oregonstate.studentlife.ihcv2.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Omeed on 3/6/18.
 */

public class AboutLoader extends AsyncTaskLoader<String> {
    private final static String TAG = AboutLoader.class.getSimpleName();

    private String aboutText;
    private final static String IHC_GET_ABOUT_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/get_about.php";

    public AboutLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (aboutText != null) {
            Log.d(TAG, "getting about page text with URL: " + IHC_GET_ABOUT_URL);
            deliverResult(aboutText);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(IHC_GET_ABOUT_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Log.d(TAG, "Opened reader");

            StringBuffer sb = new StringBuffer("");
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Log.d(TAG, "read from file. line: " + line);

            return sb.toString();
        } catch (Exception e) { e.printStackTrace(); return new String("Exception: " + e.getMessage()); }
    }

    @Override
    public void deliverResult(String data) {
        aboutText = data;
        super.deliverResult(data);
    }
}
