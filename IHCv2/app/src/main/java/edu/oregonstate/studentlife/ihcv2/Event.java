package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Omeed on 1/18/18.
 */

public class Event extends AppCompatActivity {

    private String date;
    private String time;
    private String name;
    private String location;

    public Event(String date, String time, String name, String location) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
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

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) { this.name = name; }

    public void setLocation(String location) {
        this.location = location;
    }

}
