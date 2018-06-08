package edu.oregonstate.studentlife.ihcv2.data;

import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by dylan on 1/21/2018.
 * Holds the user's account information
 */

public class User extends AppCompatActivity implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private int id;
    private int stampCount;
    private int didSurvey;
    private int grade;
    private Date birthDate;
    private int type;

    public User(String firstName, String lastName, String email, int id, int stampCount, int didSurvey, int grade, Date birthDate, int type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
        this.stampCount = stampCount;
        this.didSurvey = didSurvey;
        this.grade = grade;
        this.birthDate = birthDate;
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStampCount() {
        return stampCount;
    }

    public void setStampCount(int stampCount) {
        this.stampCount = stampCount;
    }

    public int getDidSurvey() {
        return didSurvey;
    }

    public void setDidSurvey(int didSurvey) {
        this.didSurvey = didSurvey;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class LeaderboardUser {
        private String firstName;
        private String lastName;
        private String stampCount;

        public LeaderboardUser(String firstName, String lastName, String stampCount) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.stampCount = stampCount;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getStampCount() {
            return stampCount;
        }

        public void setStampCount(String stampCount) {
            this.stampCount = stampCount;
        }
    }
}
