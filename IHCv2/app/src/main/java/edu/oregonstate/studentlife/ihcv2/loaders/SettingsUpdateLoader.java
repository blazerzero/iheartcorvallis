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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.oregonstate.studentlife.ihcv2.SettingsActivity;
import edu.oregonstate.studentlife.ihcv2.SettingsFragment;

/**
 * Created by Omeed on 3/13/18.
 * Calls the update_prefs php script to upload the information the user entered on the settings page,
 * upon account creation. The information is stored in the ihc_users table in the database.
 */

public class SettingsUpdateLoader extends AsyncTaskLoader<String> {

    private final static String TAG = AddCompletedEventLoader.class.getSimpleName();
    final static String IHC_UPDATE_USER_INFO_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/update_prefs.php";

    private String userid;
    private String firstname;
    private String lastname;
    private String email;
    private String type;
    private String grade;
    private int bdDay;
    private int bdMonth;
    private int bdYear;
    private String birthdate;

    public SettingsUpdateLoader(Bundle args, Context context) {
        super(context);
        userid = args.getString(SettingsActivity.IHC_USER_ID_KEY);
        firstname = args.getString(SettingsFragment.IHC_USER_FIRST_NAME_KEY);
        lastname = args.getString(SettingsFragment.IHC_USER_LAST_NAME_KEY);
        email = args.getString(SettingsFragment.IHC_USER_EMAIL_KEY);
        type = args.getString(SettingsFragment.IHC_USER_TYPE_KEY);
        grade = args.getString(SettingsFragment.IHC_USER_GRADE_KEY);
        bdDay = args.getInt(SettingsFragment.IHC_USER_BD_DAY_KEY);
        bdMonth = args.getInt(SettingsFragment.IHC_USER_BD_MONTH_KEY);
        bdYear = args.getInt(SettingsFragment.IHC_USER_BD_YEAR_KEY);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, bdYear);
        c.set(Calendar.MONTH, bdMonth);
        c.set(Calendar.DATE, bdDay);
        SimpleDateFormat sdfBirthDate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        birthdate = sdfBirthDate.format(c.getTime());
        Log.d(TAG, "birthdate: " + birthdate);
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
            data += "&" + URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(firstname, "UTF-8");
            data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(lastname, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
