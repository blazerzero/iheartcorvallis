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
import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2.SurveyActivity;

/**
 * Created by Omeed on 4/3/18.
 */

public class RecordSurveyResponseLoader extends AsyncTaskLoader<String> {

    private final static String TAG = RecordSurveyResponseLoader.class.getSimpleName();
    final static String IHC_UPDATE_RESPONSE_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/add_survey_response.php";

    private String userid;
    private ArrayList<Integer> questionids;
    private ArrayList<String> responses;

    public RecordSurveyResponseLoader(Context context, Bundle args) {
        super(context);
        userid = String.valueOf(args.getInt(SurveyActivity.IHC_USERID_KEY));
        Log.d(TAG, "user ID: " + userid);
        responses = args.getStringArrayList(SurveyActivity.IHC_RESPONSES_KEY);
        questionids = args.getIntegerArrayList(SurveyActivity.IHC_QUESTIONIDS_KEY);
        Log.d(TAG, "question ID array length: " + questionids.size());
        Log.d(TAG, "response array length: " + responses.size());
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "adding survey responses with URL: " + IHC_UPDATE_RESPONSE_URL);

        try {
            URL url = new URL(IHC_UPDATE_RESPONSE_URL);
            HttpURLConnection conn;
            StringBuffer sb = new StringBuffer("");


            String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            for (int i = 0; i < questionids.size(); i++) {
                sb = new StringBuffer("");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                data += "&" + URLEncoder.encode("questionid", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(questionids.get(i)), "UTF-8");
                data += "&" + URLEncoder.encode("response", "UTF-8") + "=" + URLEncoder.encode(responses.get(i), "UTF-8");
                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                Log.d(TAG, "Received script1 response: " + sb.toString());
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
