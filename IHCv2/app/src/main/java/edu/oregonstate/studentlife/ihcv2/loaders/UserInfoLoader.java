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
 * Created by Omeed on 2/22/18.
 */

public class UserInfoLoader extends AsyncTaskLoader<String> {

    private final static String TAG = UserInfoLoader.class.getSimpleName();

    private String userJSON;
    private String email;
    final static String IHC_GETUSERINFO_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getuserinfo.php";

    public UserInfoLoader(Context context, String email) {
        super(context);
        this.email = email;
    }

    @Override
    protected void onStartLoading() {
        if (userJSON != null) {
            Log.d(TAG, "loader returning cached user information");
            deliverResult(userJSON);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "getting user information with URL: " + IHC_GETUSERINFO_URL);

        try {
            URL url = new URL(IHC_GETUSERINFO_URL);
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

            //Log.d(TAG, "About to open connection.");
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

            return sb.toString();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    @Override
    public void deliverResult(String data) {
        userJSON = data;
        super.deliverResult(data);
    }
}
