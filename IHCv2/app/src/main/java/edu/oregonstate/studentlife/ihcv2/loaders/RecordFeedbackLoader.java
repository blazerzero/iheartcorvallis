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

import edu.oregonstate.studentlife.ihcv2.SurveyActivity;

/**
 * Created by Omeed on 4/6/18.
 * Calls add_app_feedback script to input the users feedback of a completed event
 * into the ihc_feedback table.
 */

public class RecordFeedbackLoader extends AsyncTaskLoader<String> {

    private final static String TAG = RecordFeedbackLoader.class.getSimpleName();
    final static String IHC_ADD_APP_FEEDBACK_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/add_app_feedback.php";

    private String userid;
    private String rating;
    private String comment;

    public RecordFeedbackLoader(Context context, Bundle args) {
        super(context);
        userid = String.valueOf(args.getInt(SurveyActivity.IHC_USERID_KEY));
        rating = String.valueOf(args.getInt(SurveyActivity.IHC_APPRATING_KEY));
        comment = args.getString(SurveyActivity.IHC_APPCOMMENT_KEY);
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "adding feedback with URL: " + IHC_ADD_APP_FEEDBACK_URL);

        try {
            URL url = new URL(IHC_ADD_APP_FEEDBACK_URL);
            String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("rating", "UTF-8") + "=" + URLEncoder.encode(rating, "UTF-8");
            data += "&" + URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8");

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
                break;
            }

            Log.d(TAG, "Received response from URL!");
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
