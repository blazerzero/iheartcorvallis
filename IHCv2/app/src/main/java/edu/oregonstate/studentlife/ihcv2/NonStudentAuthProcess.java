package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Omeed on 1/18/18.
 */

/*class NonStudentAuthProcess extends AsyncTask {

    private Context context;
    private TextView loginSuccessTextView;
    private String email;
    private String password;
    final static String IHC_NS_LOGIN_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/non_student_login.php";

    public NonStudentAuthProcess(Context context, TextView loginStatusTextView) {
        this.context = context;
        this.loginSuccessTextView = loginStatusTextView;
    }

    protected void onPreExecute() {
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        email = (String) objects[0];
        password = (String) objects[1];

        try {
            URL url = new URL(IHC_NS_LOGIN_URL);
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            //Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            //this.loginSuccessTextView.setText(data);
            Thread.sleep(1000);
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

            //Thread.sleep(1000);
            //Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
            return sb.toString();
        } catch (Exception e) { return new String("Exception: " + e.getMessage()); }
    }


    @Override
    protected void onPostExecute(Object result) {
        String resultText = (String) result;
        this.loginSuccessTextView.setText(resultText);
        if (this.loginSuccessTextView.getText() == "LOGINSUCCESS") {
            NonStudentLoginActivity.this.onBackgroundTaskDataObtained(result);
        }
    }
}*/
