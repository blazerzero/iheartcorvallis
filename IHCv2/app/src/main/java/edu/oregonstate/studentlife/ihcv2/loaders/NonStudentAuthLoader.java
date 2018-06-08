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
 * Calls the non_student_login script query the ihc_users table for a user who matches the given
 * email, if successful the user's information is passed back to the app.
 */

public class NonStudentAuthLoader extends AsyncTaskLoader<String> {

    private String email;
    final static String IHC_NS_LOGIN_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/non_student_login.php";
    private final static String TAG = NonStudentAuthLoader.class.getSimpleName();

    public NonStudentAuthLoader(Context context, String email) {
        super(context);
        this.email = email;
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(IHC_NS_LOGIN_URL);
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

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

            Log.d(TAG, "Received user info!");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    public void deliverResult(String data) {
        Log.d(TAG, "About to pass data back to login student activity!");
        Log.d(TAG, "Data: " + data);
        super.deliverResult(data);
    }
}
