package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Omeed on 1/18/18.
 */

public class Event extends AppCompatActivity {

    private String month;
    private String day;
    private String year;
    private String time;
    private String name;
    private String location;

    public Event(String month, String day, String year, String time, String name, String location) {
        this.month = month;
        this.day = day;
        this.year = year;
        this.time = time;
        this.name = name;
        this.location = location;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
