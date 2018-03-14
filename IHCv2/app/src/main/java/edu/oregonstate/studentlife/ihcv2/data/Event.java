package edu.oregonstate.studentlife.ihcv2.data;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Omeed on 1/18/18.
 */

public class Event extends AppCompatActivity implements Serializable {

    private int eventid;
    private String name;
    private String location;
    private String address;
    private Date startDT;
    private Date endDT;
    private String startTime;
    private String endTime;
    private String startMonth;
    private String startDay;
    private String startYear;
    private String endMonth;
    private String endDay;
    private String endYear;
    private String description;
    private String link1;
    private String link2;
    private String link3;
    private int pin;
    //private InputStream image;

    public Event(int eventid, String name, String location, String address, Date startDT, Date endDT, String startTime, String endTime,
                 String startMonth, String startDay, String startYear,
                 String endMonth, String endDay, String endYear,
                 String description, String link1, String link2, String link3, int pin) {
        this.eventid = eventid;
        this.name = name;
        this.location = location;
        this.address = address;
        this.startDT = startDT;
        this.endDT = endDT;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.startYear = startYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.endYear = endYear;
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

    public Date getStartDT() {
        return startDT;
    }

    public void setStartDT(Date startDT) {
        this.startDT = startDT;
    }

    public Date getEndDT() {
        return endDT;
    }

    public void setEndDT(Date endDT) {
        this.endDT = endDT;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
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
