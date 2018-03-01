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
 */

public class AddCompletedEventLoader extends AsyncTaskLoader<String> {

    private final static String TAG = AddCompletedEventLoader.class.getSimpleName();
    final static String IHC_ADD_COMPLETED_EVENT_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/add_completed_event.php";

    private String userid;
    private String eventid;

    public AddCompletedEventLoader(Context context, int userid, int eventid) {
        super(context);
        this.userid = String.valueOf(userid);
        this.eventid = String.valueOf(eventid);
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(IHC_ADD_COMPLETED_EVENT_URL);
            String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("eventid", "UTF-8") + "=" + URLEncoder.encode(eventid, "UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuffer sb = new StringBuffer("");
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            Log.d(TAG, "Received script response: " + sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
    }
}
