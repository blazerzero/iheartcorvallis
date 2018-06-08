package edu.oregonstate.studentlife.ihcv2.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Omeed on 4/8/18.
 * Calls the studentsignup php script to input the users given information into the ihc_users table.
 */

public class StudentSignupLoader extends AsyncTaskLoader<String> {

  private final static String TAG = StudentSignupLoader.class.getSimpleName();
  final static String IHC_STUDENT_SIGNUP_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/studentsignup.php";

  private String status;

  public StudentSignupLoader(Context context) {
    super(context);
  }

  @Override
  public void onStartLoading() {
    forceLoad();
  }

  public String loadInBackground() {
    try {
      URL url = new URL(IHC_STUDENT_SIGNUP_URL);

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setRequestMethod("POST");
      conn.setDoOutput(true);

      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

      StringBuffer sb = new StringBuffer("");
      String line = null;

      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }

      Log.d(TAG, "read line: " + line);
      Log.d(TAG, "sb.toString(): " + sb.toString());

      return sb.toString();
    } catch (Exception e) { e.printStackTrace(); return new String("Exception: " + e.getMessage()); }

  }

  @Override
  public void deliverResult(String data) {
    super.deliverResult(data);
  }

}
