package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;

/**
 * Created by Omeed on 1/18/18.
 */

public class Event extends AppCompatActivity {

    private String name;
    private String location;
    private String time;
    private String month;
    private String day;
    private String year;
    private String description;
    //private InputStream image;

    public Event(String name, String location, String time, String month, String day, String year, String description) {
        this.name = name;
        this.location = location;
        this.time = time;
        this.month = month;
        this.day = day;
        this.year = year;
        this.description = description;
        //this.image = image;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }*/
}
