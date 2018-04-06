package edu.oregonstate.studentlife.ihcv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.data.User;

public class EventDetailActivity extends AppCompatActivity {

    private ImageView mEventImageIV;
    private TextView mEventNameTV;
    private TextView mEventLocationTV;
    private TextView mEventAddressTV;
    private TextView mEventDateTimeTV;
    private TextView mEventDescriptionTV;
    private TextView mEventLink1TV;
    private TextView mEventLink2TV;
    private TextView mEventLink3TV;
    private TextView mEventCheckInTV;

    private Event event;
    private User user;

    private Geocoder coder;
    private List<Address> address;
    private final static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Address eventLocation;

    private String[] monthLongNames = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    private static final String TAG = EventDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        overridePendingTransition(0,0);

        mEventImageIV = (ImageView) findViewById(R.id.iv_event_image_detail);
        mEventNameTV = (TextView) findViewById(R.id.tv_event_name_detail);
        mEventLocationTV = (TextView) findViewById(R.id.tv_event_location_detail);
        mEventAddressTV = (TextView) findViewById(R.id.tv_event_address_detail);
        mEventDateTimeTV = (TextView) findViewById(R.id.tv_event_datetime_detail);
        mEventDescriptionTV = (TextView) findViewById(R.id.tv_event_description_detail);
        mEventLink1TV = (TextView) findViewById(R.id.tv_event_link1_detail);
        mEventLink2TV = (TextView) findViewById(R.id.tv_event_link2_detail);
        mEventLink3TV = (TextView) findViewById(R.id.tv_event_link3_detail);
        mEventCheckInTV = (TextView) findViewById(R.id.tv_event_check_in);

        coder = new Geocoder(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_EVENT) && intent.hasExtra(Constants.EXTRA_USER)) {
            user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            Log.d(TAG, "User ID: " + user.getId());
            event = (Event) intent.getSerializableExtra(Constants.EXTRA_EVENT);
            mEventNameTV.setText(event.getName());
            mEventLocationTV.setText(event.getLocation());
            mEventAddressTV.setText(event.getAddress());
            int monthInt = Integer.parseInt(event.getStartMonth()) - 1;
            mEventDateTimeTV.setText(monthLongNames[monthInt] + " " + event.getStartDay() + ", "
                    + event.getStartYear() + ", " + event.getStartTime() + " - " + event.getEndTime());
            mEventDescriptionTV.setText(event.getDescription());
            mEventLink1TV.setText(event.getLink1());
            mEventLink2TV.setText(event.getLink2());
            mEventLink3TV.setText(event.getLink3());
        }

        mEventCheckInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to check user geolocation and ask for event verification PIN
                // go to geolocation first, but goes straight to PIN for now
                Log.d(TAG, "check in button pressed");
                /*Intent enterEventPINIntent = new Intent(EventDetailActivity.this, CheckLocationActivity.class);
                enterEventPINIntent.putExtra(Constants.EXTRA_EVENT_DETAILED, event);
                enterEventPINIntent.putExtra(Constants.EXTRA_USER, user);
                startActivity(enterEventPINIntent);*/
                verifyLocation();
            }
        });
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_map:
                Intent mapIntent = new Intent(EventDetailActivity.this, MapsActivity.class);
                mapIntent.putExtra(Constants.EXTRA_EVENT_DETAILED, event);
                startActivity(mapIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    private void verifyLocation() {
        try {
            address = coder.getFromLocationName(event.getAddress(), 5);
            if (address == null || address.size() == 0) {
                Toast.makeText(getApplicationContext(), "Error reading event address.", Toast.LENGTH_LONG).show();
            } else {
                eventLocation = address.get(0);
                getLocationPermission();
                if (mLocationPermissionGranted) {
                    try {
                        // TODO: implement location manager to get location updates from user, as getLastLocation can return null
                        // TODO: Or if getLastLocation return null then exit the verify location function
                        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                       // locationResult = mFusedLocationProviderClient.requestLocationUpdates();
                        locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location myLocation = task.getResult();
                                if (distance(myLocation.getLatitude(), myLocation.getLongitude(),
                                        eventLocation.getLatitude(), eventLocation.getLongitude()) < 0.1) {
                                    Intent eventPININtent = new Intent(getApplicationContext(), EventPINActivity.class);
                                    eventPININtent.putExtra(Constants.EXTRA_EVENT_DETAILED, event);
                                    eventPININtent.putExtra(Constants.EXTRA_USER, user);
                                    startActivity(eventPININtent);
                                } else {
                                    AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        builder = new AlertDialog.Builder(EventDetailActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                    } else {
                                        builder = new AlertDialog.Builder(EventDetailActivity.this);
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
                        });
                    } catch (SecurityException se) {
                        //mLocationCheckPB.setVisibility(View.GONE);
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
}
