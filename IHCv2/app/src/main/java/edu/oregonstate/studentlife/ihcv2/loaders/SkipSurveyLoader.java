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
 * Created by Omeed on 4/20/18.
 * Calls skip_survey php script to update ihc_users to say this user skipped the survey, so that they
 * will not get prompted with the survey again
 */

public class SkipSurveyLoader extends AsyncTaskLoader<String> {

    private final static String TAG = SkipSurveyLoader.class.getSimpleName();
    final static String IHC_SKIP_SURVEY_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/skip_survey.php";

    private int userid;

    public SkipSurveyLoader(Context context, int userid) {
        super(context);
        this.userid = userid;
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(IHC_SKIP_SURVEY_URL);

            String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(userid), "UTF-8");
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
