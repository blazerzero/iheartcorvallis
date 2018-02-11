package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by Omeed on 1/18/18.
 */

public class Event extends AppCompatActivity implements Serializable {

    private String name;
    private String location;
    private String address;
    private double latitude;
    private double longitude;
    private String time;
    private String month;
    private String day;
    private String year;
    private String description;
    private String link1;
    private String link2;
    private String link3;
    //private InputStream image;

    public Event(String name, String location, String address, double latitude, double longitude, String time, String month, String day, String year, String description, String link1, String link2, String link3) {
        this.name = name;
        this.location = location;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.month = month;
        this.day = day;
        this.year = year;
        this.description = description;
        this.link1 = link1;
        this.link2 = link2;
        this.link3 = link3;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    /*public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }*/
}
