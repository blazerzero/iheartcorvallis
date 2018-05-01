package edu.oregonstate.studentlife.ihcv2Demo.data;

import android.support.v7.app.AppCompatActivity;

import edu.oregonstate.studentlife.ihcv2Demo.R;

/**
 * Created by Omeed on 1/22/18.
 */

public class Constants extends AppCompatActivity {
    private int goldThreshold = 15;
    private int silverThreshold = 10;
    private int bronzeThreshold = 7;
    //public final int GEOFENCE_RADIUS_IN_METERS = 50;
    private int goldColor = getResources().getColor(R.color.eventGold);
    private int silverColor = getResources().getColor(R.color.eventSilver);
    private int bronzeColor = getResources().getColor(R.color.eventBronze);
    public static final String EXTRA_EVENT = "Event";
    public static final String EXTRA_EVENT_DETAILED = "Detailed Event";
    public static final String EXTRA_USER = "User";
    public static final String EXTRA_USER_ID = "User ID";
    public static final String EXTRA_CALLING_ACTIVITY_ID = "Login Activity";
    public static final String EXTRA_USER_PROFILE_PICTURE = "User Profile Picture";
    public static final String EXTRA_USER_STATUS = "User Status";
    public static final String EXTRA_USER_NAME = "User Name";
    public static final String EXTRA_USER_EMAIL = "User Email";
    public static final String EXTRA_MSG = "Extra Message";
    /*public static final String EXTRA_USER_BIRTHDATE = "User Birthdate";
    public static final String EXTRA_USER_GRADE = "User Grade";
    public static final String EXTRA_USER_TYPE = "User Type";*/

    public int getGoldThreshold() {
        return goldThreshold;
    }

    public void setGoldThreshold(int goldThreshold) {
        this.goldThreshold = goldThreshold;
    }

    public int getSilverThreshold() {
        return silverThreshold;
    }

    public void setSilverThreshold(int silverThreshold) {
        this.silverThreshold = silverThreshold;
    }

    public int getBronzeThreshold() {
        return bronzeThreshold;
    }

    public void setBronzeThreshold(int bronzeThreshold) {
        this.bronzeThreshold = bronzeThreshold;
    }

    public int getGoldColor() {
        return goldColor;
    }

    public void setGoldColor(int goldColor) {
        this.goldColor = goldColor;
    }

    public int getSilverColor() {
        return silverColor;
    }

    public void setSilverColor(int silverColor) {
        this.silverColor = silverColor;
    }

    public int getBronzeColor() {
        return bronzeColor;
    }

    public void setBronzeColor(int bronzeColor) {
        this.bronzeColor = bronzeColor;
    }
}
