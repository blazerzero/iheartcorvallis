package edu.oregonstate.studentlife.ihcv2.data;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by dylan on 1/21/2018.
 */

public class User extends AppCompatActivity implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private int id;
    private String stampCount;
    private LeaderboardUser leaderboardUser;

    public User(String firstName, String lastName, String email, int id, String stampCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
        this.stampCount = stampCount;
        //this.leaderboardUser = new LeaderboardUser(firstName, lastName, stampCount);
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

    public String getStampCount() {
        return stampCount;
    }

    public void setStampCount(String stampCount) {
        this.stampCount = stampCount;
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
