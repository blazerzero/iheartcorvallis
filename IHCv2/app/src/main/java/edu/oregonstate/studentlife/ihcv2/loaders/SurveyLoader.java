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
 * Created by Omeed on 4/3/18.
 * Calls the getsurvey php script to query the ihc_survey table for the survey questions and the
 * possible responses to each question
 */

public class SurveyLoader extends AsyncTaskLoader<String> {

    private final static String TAG = SurveyLoader.class.getSimpleName();

    private String surveyJSON;
    final static String IHC_GETSURVEY_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getsurvey.php";

    public SurveyLoader(Context context) {
        super(context);
    }

    @Override
    public void onStartLoading() {
        if (surveyJSON != null) {
            Log.d(TAG, "loader returning cached survey information");
            deliverResult(surveyJSON);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "getting survey information with URL: " + IHC_GETSURVEY_URL);

        try {
            URL url = new URL(IHC_GETSURVEY_URL);

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
        surveyJSON = data;
        super.deliverResult(data);
    }

}
