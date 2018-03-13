package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.data.User;

public class CheckLocationActivity extends AppCompatActivity {

    private GeofencingClient mGeofencingClient;
    private Geofence mGeofence;
    private Event event;
    private User user;
    private final static String eventkey = "event geofence";
    private Geocoder coder;
    private List<Address> address;
    private final static int GEOFENCE_RADIUS_IN_METERS = 50;
    private final static int GEOFENCE_EXPIRATION_IN_MILLISECONDS = 6000;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    public static final String EXTRA_EVENT = "Detailed Event";
    public static final String EXTRA_USER = "User";
    private ProgressBar mLocationCheckPB;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Address eventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_location);
        coder = new Geocoder(this);
        mLocationCheckPB = (ProgressBar) findViewById(R.id.pb_location_check);
        mGeofencingClient = LocationServices.getGeofencingClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EventDetailActivity.EXTRA_EVENT_DETAILED) && intent.hasExtra(EventDetailActivity.EXTRA_USER)) {
            event = (Event) intent.getSerializableExtra(EventDetailActivity.EXTRA_EVENT_DETAILED);
            user = (User) intent.getSerializableExtra(EventDetailActivity.EXTRA_USER);

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                try {
                    address = coder.getFromLocationName(event.getAddress(), 5);
                    if (address == null || address.size() == 0) {
                        Toast.makeText(this, "Error reading event address.", Toast.LENGTH_LONG).show();
                    } else {
                        eventLocation = address.get(0);

                        /*mGeofence = new Geofence.Builder()
                                // Set the request ID of the geofence. This is a string to identify this
                                // geofence.
                                .setRequestId(eventkey)

                                .setCircularRegion(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        GEOFENCE_RADIUS_IN_METERS
                                )
                                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                        Geofence.GEOFENCE_TRANSITION_EXIT)
                                .build();*/
                        getLocationPermission();
                        if (mLocationPermissionGranted) {
                            try {
                                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Location> task) {
                                        if (task.isSuccessful()) {
                                            // Set the map's camera position to the current location of the device.
                                            Location myLocation = task.getResult();
                                            if (distance(myLocation.getLatitude(), myLocation.getLongitude(),
                                                    eventLocation.getLatitude(), eventLocation.getLongitude()) < 0.1) {
                                                Intent eventPINIntent = new Intent(CheckLocationActivity.this, EventPINActivity.class);
                                                eventPINIntent.putExtra(EXTRA_EVENT, event);
                                                eventPINIntent.putExtra(EXTRA_USER, user);
                                                mLocationCheckPB.setVisibility(View.GONE);
                                                startActivity(eventPINIntent);
                                            } else {
                                                mLocationCheckPB.setVisibility(View.GONE);
                                                AlertDialog.Builder builder;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    builder = new AlertDialog.Builder(CheckLocationActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                                } else {
                                                    builder = new AlertDialog.Builder(CheckLocationActivity.this);
                                                }
                                                builder.setTitle("Not In Range");
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        onBackPressed();
                                                    }
                                                });
                                                builder.setMessage("You're not close enough to the event.");
                                                builder.setIcon(android.R.drawable.ic_dialog_alert);
                                                builder.show();
                                            }
                                        }
                                    }
                                });
                            } catch (SecurityException se) {
                                mLocationCheckPB.setVisibility(View.GONE);
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(this);
                                }
                                builder.setTitle("Location Permission Denied");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onBackPressed();
                                    }
                                });
                                builder.setMessage("I Heart Corvallis needs location permission to verify that you're at this event.");
                                builder.setIcon(android.R.drawable.ic_dialog_alert);
                                builder.show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(mGeofence);
        return builder.build();
    }

    @Override
    public void onResume() {
        super.onResume();
        super.onBackPressed(); // make page go back to EventDetailActivity on activity resume
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
