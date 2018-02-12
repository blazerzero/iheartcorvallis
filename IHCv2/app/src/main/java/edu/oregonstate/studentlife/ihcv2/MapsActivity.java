package edu.oregonstate.studentlife.ihcv2;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Event> eventList;
    private Event singleEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        overridePendingTransition(0,0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventList = new ArrayList<Event>();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EventDetailActivity.EXTRA_EVENT_DETAILED)) {
                singleEvent = (Event) intent.getSerializableExtra(EventDetailActivity.EXTRA_EVENT_DETAILED);
                eventList.add(singleEvent);
            }
            else if (intent.hasExtra(EventsActivity.EXTRA_EVENT)) {
                eventList = (ArrayList<Event>) intent.getSerializableExtra(EventsActivity.EXTRA_EVENT);
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(12.0f);

        // Set the camera over Corvallis
        LatLng Corvallis = new LatLng(44.564663, -123.263282);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Corvallis));

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng eventLatLng = null;

        for (int i = 0; i < eventList.size(); i++) {
            try {
                address = coder.getFromLocationName(eventList.get(i).getAddress(), 5);
                if (address == null || address.size() == 0) {
                    Toast.makeText(this, "Error reading event address.", Toast.LENGTH_LONG).show();
                }
                else {
                    Address location = address.get(0);
                    eventLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(eventLatLng)
                            .title(eventList.get(i).getName()));
                    if (eventList.size() == 1) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(eventLatLng));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
