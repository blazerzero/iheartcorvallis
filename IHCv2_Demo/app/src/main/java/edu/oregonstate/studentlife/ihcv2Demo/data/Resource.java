package edu.oregonstate.studentlife.ihcv2Demo.data;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by Omeed on 3/1/18.
 */

public class Resource extends AppCompatActivity implements Serializable {

    private int rID;
    private String rTitle;
    private String rDescription;
    private String rLink;

    public Resource(int rID, String rTitle, String rDescription, String rLink) {
        this.rID = rID;
        this.rTitle = rTitle;
        this.rDescription = rDescription;
        this.rLink = rLink;
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

}
