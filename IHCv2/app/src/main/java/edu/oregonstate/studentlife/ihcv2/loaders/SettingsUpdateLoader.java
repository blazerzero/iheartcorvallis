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

import edu.oregonstate.studentlife.ihcv2.SettingsActivity;

/**
 * Created by Omeed on 3/13/18.
 */

public class SettingsUpdateLoader extends AsyncTaskLoader<String> {

    private final static String TAG = AddCompletedEventLoader.class.getSimpleName();
    final static String IHC_UPDATE_USER_INFO_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/update_prefs.php";

    private String userid;
    private String type;
    private String grade;
    private String age;

    public SettingsUpdateLoader(Bundle args, Context context) {
        super(context);
        userid = args.getString(SettingsActivity.IHC_USER_ID_KEY);
        type = args.getString(SettingsActivity.IHC_USER_TYPE_KEY);
        grade = args.getString(SettingsActivity.IHC_USER_GRADE_KEY);
        age = args.getString(SettingsActivity.IHC_USER_AGE_KEY);
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(IHC_UPDATE_USER_INFO_URL);
            String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
            data += "&" + URLEncoder.encode("grade", "UTF-8") + "=" + URLEncoder.encode(grade, "UTF-8");
            data += "&" + URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8");

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
