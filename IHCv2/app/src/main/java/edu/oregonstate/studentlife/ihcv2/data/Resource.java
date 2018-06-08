package edu.oregonstate.studentlife.ihcv2.data;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by Omeed on 3/1/18.
 * Holds information on resources featured on the Resource Page
 */

public class Resource extends AppCompatActivity implements Serializable {

    private int rID;
    private String rTitle;
    private String rDescription;
    private String rLink;
    private String rImagePath;

    public Resource(int rID, String rTitle, String rDescription, String rLink, String rImagePath) {
        this.rID = rID;
        this.rTitle = rTitle;
        this.rDescription = rDescription;
        this.rLink = rLink;
        this.rImagePath = rImagePath;
    }

    public String getResourceTitle() {
        return rTitle;
    }

    public void setResourceTitle(String rTitle) {
        this.rTitle = rTitle;
    }

    public String getResourceDescription() {
        return rDescription;
    }

    public void setResourceDescription(String rDescription) {
        this.rDescription = rDescription;
    }

    public String getResourceLink() {
        return rLink;
    }

    public void setResourceLink(String rLink) {
        this.rLink = rLink;
    }

    public String getResourceImagePath() {
        return rImagePath;
    }

    public void setResourceImagePath(String rImagePath) {
        this.rImagePath = rImagePath;
    }
}
