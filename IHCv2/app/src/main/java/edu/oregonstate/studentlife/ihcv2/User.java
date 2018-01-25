package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by dylan on 1/21/2018.
 */

public class User extends AppCompatActivity {
    private String firstName;
    private String lastName;
    private String email;
    private String id;
    private String stampCount;

    public User(String firstName, String lastName, String email, String id, String stampCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStampCount() {
        return stampCount;
    }

    public void setStampCount(String stampCount) {
        this.stampCount = stampCount;
    }
}
