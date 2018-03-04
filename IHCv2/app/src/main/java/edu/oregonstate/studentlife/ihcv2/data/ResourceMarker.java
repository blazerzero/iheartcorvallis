package edu.oregonstate.studentlife.ihcv2.data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Omeed on 1/25/18.
 */

public class ResourceMarker {

    private String name;
    private LatLng latLng;
    private String type;

    public ResourceMarker(String name, LatLng latLng, String type) {
        this.name = name;
        this.latLng = latLng;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
