package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by dylan on 1/21/2018.
 */

public class User extends AppCompatActivity {
    private String userName;
    private String stampCount;

    public User(String userName, String stampCount){
        this.userName = userName;
        this.stampCount = stampCount;
    }

    public String getUserName(){ return userName;}

    public void setUserName(String userName) {this.userName = userName;}

    public String getStampCount() {return stampCount;}

    public void setStampCount(String stampCount){this.stampCount = stampCount;}
}
