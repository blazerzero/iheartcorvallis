package edu.oregonstate.studentlife.ihcv2;

import android.Manifest;
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
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.PassportLoader;

public class EventDetailActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<String> {

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

    private final static int IHC_PASSPORT_LOADER_ID = 0;
    private ArrayList<Integer> completedEventIDs;

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
            if (event.getImagePath().contains(".jpg") || event.getImagePath().contains(".jpeg") || event.getImagePath().contains(".png")) {
                Picasso.with(this)
                        .load(event.getImagePath())
                        .into(mEventImageIV);
            }
            mEventNameTV.setText(event.getName());
            mEventLocationTV.setText(event.getLocation());
            mEventAddressTV.setText(event.getAddress());
            int startMonthInt = Integer.parseInt(event.getStartMonth()) - 1;
            int endMonthInt = Integer.parseInt(event.getEndMonth()) - 1;
            String eventDateTimeText;
            if (event.getStartDay().equals("1") && event.getStartMonth().equals("01") && event.getStartYear().equals("1900") && event.getStartTime().equals("12:00 AM")
                    && event.getEndDay().equals("31") && event.getEndMonth().equals("12") && event.getEndYear().equals("2099") && event.getEndTime().equals("11:59 PM")) {
                eventDateTimeText = "This event can be completed anytime!";
            }
            else {
                eventDateTimeText = "BEGINS: " + monthLongNames[startMonthInt] + " " + event.getStartDay() + ", "
                        + event.getStartYear() + ", " + event.getStartTime() + "\nENDS: "
                        + monthLongNames[endMonthInt] + " " + event.getEndDay() + ", "
                        + event.getEndYear() + ", " + event.getEndTime();
            }
            mEventDateTimeTV.setText(eventDateTimeText);
            mEventDescriptionTV.setText(event.getDescription());
            mEventLink1TV.setText(event.getLink1());
            mEventLink2TV.setText(event.getLink2());
            mEventLink3TV.setText(event.getLink3());
            Log.d(TAG, "link1: " + event.getLink1());
            if (event.getLink1() == null || event.getLink1().equals("")) {
                mEventLink1TV.setVisibility(View.GONE);
            }
            if (event.getLink2() == null || event.getLink1().equals("")) {
                mEventLink1TV.setVisibility(View.GONE);
            }
            if (event.getLink2() == null || event.getLink1().equals("")) {
                mEventLink1TV.setVisibility(View.GONE);
            }
        }

        getSupportLoaderManager().initLoader(IHC_PASSPORT_LOADER_ID, null, this);

        mEventCheckInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to check user geolocation and ask for event verification PIN
                // go to geolocation first, but goes straight to PIN for now
                Log.d(TAG, "check in button pressed");

                if (!completedEventIDs.contains(event.getEventid())) {
                    Calendar currentDate = Calendar.getInstance(TimeZone.getTimeZone("America/LosAngeles"), Locale.US);
                    Log.d(TAG, "current date: " + currentDate.getTime());
                    Log.d(TAG, "event start dt: " + event.getStartDT());
                    if (currentDate.getTime().before(event.getStartDT())) {
                        Toast.makeText(EventDetailActivity.this, "This event hasn't started yet. Try again when the event has started.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        verifyLocation();
                    }
                }
                else {
                    Toast.makeText(EventDetailActivity.this, "You have already checked into this event!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new PassportLoader(this, String.valueOf(user.getId()));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "got results from loader");
        try {
            StringTokenizer stEvents = new StringTokenizer(data, "\\");
            completedEventIDs = new ArrayList<Integer>();
            while (stEvents.hasMoreTokens()) {
                String eventInfoString = stEvents.nextToken();
                Log.d(TAG, "eventInfoString: " + eventInfoString);
                JSONObject passportEventJSON = new JSONObject(eventInfoString);
                Log.d(TAG, "passportEventJSON: " + passportEventJSON);
                int eventid = Integer.parseInt(passportEventJSON.getString("eventid"));
                completedEventIDs.add(eventid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
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
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                try {
                    if (lm != null && (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
                        Toast.makeText(this, "Location services are not enabled! Please go to settings to enable location services!", Toast.LENGTH_LONG).show();
                    } else if (lm != null) {
                        Intent mapIntent = new Intent(this, MapsActivity.class);
                        mapIntent.putExtra(Constants.EXTRA_EVENT_DETAILED, event);
                        startActivity(mapIntent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission has been granted!");
            mLocationPermissionGranted = true;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Log.d(TAG, "permission has NOT been granted!");
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }
            else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Use Current Location")
                    .setMessage("I Heart Corvallis needs your location to verify that you are at this event.")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(EventDetailActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Nothing to do...
                        }
                    })
                    .create()
                    .show();
            /*ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);*/
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
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
                else {
                    Log.d(TAG, "Location has still NOT been granted!");
                    // Nothing to do...
                }
            }
        }
    }

    private void verifyLocation() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            if (lm != null && (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
                Toast.makeText(EventDetailActivity.this, "Location services are not enabled! Please go to settings to enable location services!", Toast.LENGTH_LONG).show();
            }
            else if (lm != null) {
                address = coder.getFromLocationName(event.getAddress(), 5);
                if (address == null || address.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Error reading event address.", Toast.LENGTH_LONG).show();
                } else {
                    eventLocation = address.get(0);
                    getLocationPermission();

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
