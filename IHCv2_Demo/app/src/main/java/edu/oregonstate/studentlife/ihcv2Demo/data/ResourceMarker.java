package edu.oregonstate.studentlife.ihcv2Demo.data;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Omeed on 1/25/18.
 */

public class ResourceMarker {

    private String name;
    private Address address;
    private LatLng latLng;
    private String type;
    private Marker marker;

    public ResourceMarker(String name, Address address, LatLng latLng, String type, Marker marker) {
        this.name = name;
        this.address = address;
        this.latLng = latLng;
        this.type = type;
        this.marker = marker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setMarkerVisibility(boolean visibility) {
        this.marker.setVisible(visibility);
    }
}
