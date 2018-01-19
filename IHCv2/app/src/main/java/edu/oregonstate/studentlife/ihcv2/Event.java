package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Omeed on 1/18/18.
 */

public class Event extends AppCompatActivity {

    private String date;
    private String name;
    private String location;

    public Event(String date, String name, String location) {
        this.date = date;
        this.name = name;
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
