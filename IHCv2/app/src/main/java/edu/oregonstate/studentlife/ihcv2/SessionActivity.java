package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by dylan on 2/1/2018.
 */


public class SessionActivity {
    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "IHCPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NAME = "name";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_ID = "id";
    //Constructor
    public SessionActivity(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //create login session
    public void createLoginSession(String name, String email, String id){
        //store login value as true
        editor.putBoolean(IS_LOGIN, true);

        //storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        editor.putString(KEY_ID, id);

        //commit changes

        editor.commit();
    }

    public void checkLogin(){

        if(!this.isLoggedIn()){
            // user not logged in redirect to dashboard
            Intent i = new Intent(_context, MainActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }
        else {
            Intent i = new Intent(_context, DashboardActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }
    }


    //get stored session data
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        //username
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        //user email
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_ID, pref.getString(KEY_ID, null));

        return user;
    }

    //clear session details
    public void logoutUser(){

        //clearing all data from shared preferences
        editor.clear();
        editor.commit();

        //after logout redirect user to main activity
        Intent i = new Intent(_context, MainActivity.class);

        //closing all the activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //add new flag to start new activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //starting login activity
        _context.startActivity(i);
    }

    //Quick check for login
    public boolean isLoggedIn() {

        return pref.getBoolean(IS_LOGIN, false);
    }

}
