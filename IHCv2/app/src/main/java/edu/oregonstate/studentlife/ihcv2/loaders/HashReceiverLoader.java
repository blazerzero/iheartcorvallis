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
 * Created by Omeed on 2/28/18.
 * Calls the gethash php script to query the database for the user's hashed password. This is used
 * to check against the inputted passwords hash to see if the user inputted the correct password
 * for their account
 */

public class HashReceiverLoader extends AsyncTaskLoader<String> {

    private final static String TAG = HashReceiverLoader.class.getSimpleName();
    final static String IHC_PASS_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/gethash.php";

    private String email;

    public HashReceiverLoader(Context context, String email) {
        super(context);
        this.email = email;
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "authenticating user with URL: " + IHC_PASS_URL);
        try {
            URL url = new URL(IHC_PASS_URL);
            Log.d(TAG, "email being passed: " + email);

            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            Log.d(TAG, "about to read");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuffer sb = new StringBuffer("");
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
    }

}
