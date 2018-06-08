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

import edu.oregonstate.studentlife.ihcv2.SignupPageActivity;
import edu.oregonstate.studentlife.ihcv2.data.PBKDF2;

/**
 * Created by Omeed on 4/5/18.
 * Calls the signup php script to put the users given information into the ihc_users table.
 */

public class SignupLoader extends AsyncTaskLoader<String> {

    private final static String TAG = SignupLoader.class.getSimpleName();
    final static String IHC_SIGNUP_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/signup.php";

    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public SignupLoader(Context context, Bundle args) {
        super(context);
        firstname = args.getString(SignupPageActivity.IHC_FIRSTNAME_KEY);
        lastname = args.getString(SignupPageActivity.IHC_LASTNAME_KEY);
        email = args.getString(SignupPageActivity.IHC_EMAIL_KEY);
        password = args.getString(SignupPageActivity.IHC_PASSWORD_KEY);
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        PBKDF2 pHash;

        try {
            pHash = new PBKDF2();
            pHash.createPBKDF2hash(password);

            Log.d(TAG, "hashed password");

            URL url = new URL(IHC_SIGNUP_URL);
            String data = URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(firstname, "UTF-8");
            data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(lastname, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pHash.hPassword, "UTF-8");

            Log.d(TAG, "about to open connection");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "opened connection");
            Log.d(TAG, "about to write to script");

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            Log.d(TAG, "wrote to script");
            Log.d(TAG, "about to set up reader");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuffer sb = new StringBuffer("");
            String line = null;

            //sb.append(firstname + " " + lastname + "\\" + email + "\\");

            Log.d(TAG, "about to read");

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
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
