package edu.oregonstate.studentlife.ihcv2.data;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Omeed on 1/18/18.
 */

public class Event extends AppCompatActivity implements Serializable {

    private int eventid;
    private String name;
    private String location;
    private String address;
    private Date dateTime;
    private String time;
    private String month;
    private String day;
    private String year;
    private String description;
    private String link1;
    private String link2;
    private String link3;
    private int pin;
    //private InputStream image;

    public Event(int eventid, String name, String location, String address, Date dateTime, String time, String month, String day, String year, String description, String link1, String link2, String link3, int pin) {
        this.eventid = eventid;
        this.name = name;
        this.location = location;
        this.address = address;
        this.dateTime = dateTime;
        this.time = time;
        this.month = month;
        this.day = day;
        this.year = year;
        this.description = description;
        this.link1 = link1;
        this.link2 = link2;
        this.link3 = link3;
        this.pin = pin;
        //this.image = image;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public String getLink3() {
        return link3;
    }

    public void setLink3(String link3) {
        this.link3 = link3;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    /*public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }*/
}
