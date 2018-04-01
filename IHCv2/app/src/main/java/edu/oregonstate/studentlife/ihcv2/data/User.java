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
    private int grade;
    private int age;
    private int type;
    //private LeaderboardUser leaderboardUser;

    public User(String firstName, String lastName, String email, int id, String stampCount, int grade, int age, int type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
        this.stampCount = stampCount;
        this.grade = grade;
        this.age = age;
        this.type = type;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
