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

import edu.oregonstate.studentlife.ihcv2.GetUserInfoActivity;

/**
 * Created by Omeed on 4/17/18.
 */

public class ExtraUserInfoLoader extends AsyncTaskLoader<String> {

    private final static String TAG = ExtraUserInfoLoader.class.getSimpleName();
    final static String IHC_ADD_EXTRA_USERINFO_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/add_extra_userinfo.php";

    private String userid;
    private String studentid;
    private String onid;
    private String grade;
    private String type;
    private String birthdate;

    public ExtraUserInfoLoader(Context context, Bundle args) {
        super(context);
        userid = String.valueOf(args.getInt(GetUserInfoActivity.IHC_USER_ID_KEY));
        studentid = args.getString(GetUserInfoActivity.IHC_USER_STUDENTID_KEY);
        onid = args.getString(GetUserInfoActivity.IHC_USER_ONID_KEY);
        grade = String.valueOf(args.getInt(GetUserInfoActivity.IHC_USER_GRADE_KEY));
        type = String.valueOf(args.getInt(GetUserInfoActivity.IHC_USER_TYPE_KEY));
        birthdate = args.getString(GetUserInfoActivity.IHC_USER_BD_KEY);
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(IHC_ADD_EXTRA_USERINFO_URL);
            String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("studentid", "UTF-8") + "=" + URLEncoder.encode(studentid, "UTF-8");
            data += "&" + URLEncoder.encode("onid", "UTF-8") + "=" + URLEncoder.encode(onid, "UTF-8");
            data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
            data += "&" + URLEncoder.encode("grade", "UTF-8") + "=" + URLEncoder.encode(grade, "UTF-8");
            data += "&" + URLEncoder.encode("birthdate", "UTF-8") + "=" + URLEncoder.encode(birthdate, "UTF-8");

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
