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
 * Created by Omeed on 2/22/18.
 */

public class PassportLoader extends AsyncTaskLoader<String> {
    private final static String TAG = PassportLoader.class.getSimpleName();

    private String userid;
    private String passportJSON;
    private final static String IHC_GET_COMPLETED_EVENTS_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/get_completed_events.php";

    public PassportLoader(Context context, String userid) {
        super(context);
        this.userid = userid;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(IHC_GET_COMPLETED_EVENTS_URL);
            String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuffer sb = new StringBuffer("");
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (Exception e) { e.printStackTrace(); return new String("Exception: " + e.getMessage()); }
    }

    @Override
    public void deliverResult(String data) {
        passportJSON = data;
        Log.d(TAG, "passport data: " + data);
        super.deliverResult(data);
    }

}
