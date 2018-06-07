package edu.oregonstate.studentlife.ihcv2.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import edu.oregonstate.studentlife.ihcv2.EventPINActivity;

/**
 * Created by Omeed on 2/28/18.
 * Calls the add_completed_event php script to add relevant info (eventId,userID,etc.) into
 * the ihc_completed_events table of the database
 */

public class AddCompletedEventLoader extends AsyncTaskLoader<String> {

    private final static String TAG = AddCompletedEventLoader.class.getSimpleName();
    final static String IHC_ADD_COMPLETED_EVENT_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/add_completed_event.php";

    private String userid;
    private String eventid;
    private String rating;
    private String comment;

    public AddCompletedEventLoader(Context context, Bundle args) {
        super(context);
        this.userid = String.valueOf(args.getInt(EventPINActivity.IHC_COMPLETED_EVENT_USERID_KEY));
        this.eventid = String.valueOf(args.getInt(EventPINActivity.IHC_COMPLETED_EVENT_EVENTID_KEY));
        this.rating = String.valueOf(args.getInt(EventPINActivity.IHC_COMPLETED_EVENT_RATING_KEY));
        this.comment = String.valueOf(args.getString(EventPINActivity.IHC_COMPLETED_EVENT_COMMENT_KEY));
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
            data += "&" + URLEncoder.encode("rating", "UTF-8") + "=" + URLEncoder.encode(rating, "UTF-8");
            data += "&" + URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8");
            Log.d(TAG, "data: " + data);

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
