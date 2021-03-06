package edu.oregonstate.studentlife.ihcv2.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Omeed on 2/22/18.
 * Calls the getusers_stampcount php script to query the ihc_users table of the database for every
 * user's firstname, lastname, and stampcount
 */

public class LeaderboardLoader extends AsyncTaskLoader<String> {
    private final static String TAG = LeaderboardLoader.class.getSimpleName();
    private final static String IHC_GET_STAMPCOUNT_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getusers_stampcount.php";
    private String leaderboardJSON;

    public LeaderboardLoader(Context context) {
        super(context);
    }

    @Override
    public void onStartLoading() {
        if (leaderboardJSON != null) {
            Log.d(TAG, "loader returning cached leaderboard");
            deliverResult(leaderboardJSON);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        try {
            URL url = new URL(IHC_GET_STAMPCOUNT_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuffer sb = new StringBuffer("");
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (Exception e) { return new String("Exception: " + e.getMessage()); }
    }

    @Override
    public void deliverResult(String data) {
        leaderboardJSON = data;
        super.deliverResult(data);
    }

}
