package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Omeed on 1/18/18.
 */

class AuthProcess extends AsyncTask {

    private Context context;
    private int byGetOrPost = 0;    // 0 = Get, 1 = Post

    public AuthProcess(Context context, int flag) {
        this.context = context;
        byGetOrPost = flag;     // If flag = 0, Get; if flag = 1, Post
    }

    protected void onPreExecute() {

    }

    @Override
    protected Object doInBackground(Object[] args) {
        if (byGetOrPost == 0) {     // Get method (For logging in)
            try {
                String email = (String) args[0];
                String password = (String) args[1];
                String link = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/otherlogin.php";

                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                // Read Server Response
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } catch(Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        } else {                    // Post method (For signing up)
            try {
                String firstName = (String) args[0];
                String lastName = (String) args[1];
                String email = (String) args[2];
                String password = (String) args[3];
                String link = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/signup.php";
                String data = URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(firstName, "UTF-8");
                data += "&" + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(lastName, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            } catch(Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onPostExecute(Object result) {

    }
}
