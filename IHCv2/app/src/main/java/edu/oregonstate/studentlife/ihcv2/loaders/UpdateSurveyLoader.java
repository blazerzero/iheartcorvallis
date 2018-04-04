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

public class UpdateSurveyLoader extends AsyncTaskLoader<String> {

    private final static String TAG = UpdateSurveyLoader.class.getSimpleName();
    final static String IHC_UPDATE_RESPONSE_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/add_survey_response.php";

    private String userid;
    private String response1;
    private String response2;
    private String response3;

    public UpdateSurveyLoader(Context context, Bundle args) {
        super(context);
        userid = String.valueOf(args.getInt(SurveyActivity.IHC_USERID_KEY));
        Log.d(TAG, "user ID: " + userid);
        ArrayList<String> responses = args.getStringArrayList(SurveyActivity.IHC_RESPONSES_KEY);
        response1 = responses.get(0);
        response2 = responses.get(1);
        response3 = responses.get(2);
        Log.d(TAG, "response1: " + response1);
        Log.d(TAG, "response2: " + response2);
        Log.d(TAG, "response3: " + response3);
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "updating survey responses with URL: " + IHC_UPDATE_RESPONSE_URL);

        try {
            URL url = new URL(IHC_UPDATE_RESPONSE_URL);
            String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("response1", "UTF-8") + "=" + URLEncoder.encode(response1, "UTF-8");
            data += "&" + URLEncoder.encode("response2", "UTF-8") + "=" + URLEncoder.encode(response2, "UTF-8");
            data += "&" + URLEncoder.encode("response3", "UTF-8") + "=" + URLEncoder.encode(response3, "UTF-8");

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
