package edu.oregonstate.studentlife.ihcv2Demo;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import edu.oregonstate.studentlife.ihcv2Demo.data.Constants;
import edu.oregonstate.studentlife.ihcv2Demo.data.Event;
import edu.oregonstate.studentlife.ihcv2Demo.data.User;

public class EventDetailActivity extends AppCompatActivity
    {

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
        if (intent != null && intent.hasExtra(Constants.EXTRA_EVENT)) {
            event = (Event) intent.getSerializableExtra(Constants.EXTRA_EVENT);
            mEventNameTV.setText(event.getName());
            mEventLocationTV.setText(event.getLocation());
            mEventAddressTV.setText(event.getAddress());
            int startMonthInt = Integer.parseInt(event.getStartMonth()) - 1;
            int endMonthInt = Integer.parseInt(event.getEndMonth()) - 1;
            String eventDateTimeText;
            if (event.getStartDay().equals("1") && event.getStartMonth().equals("1") && event.getStartYear().equals("1900") && event.getStartTime().equals("12:00 AM")
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

        mEventCheckInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to check user geolocation and ask for event verification PIN
                // go to geolocation first, but goes straight to PIN for now
                Log.d(TAG, "check in button pressed");

                if (event.getEventid() == 1) {
                    Calendar currentDate = Calendar.getInstance(TimeZone.getTimeZone("America/LosAngeles"), Locale.US);
                    Log.d(TAG, "current date: " + currentDate.getTime());
                    Log.d(TAG, "event start dt: " + event.getStartDT());
                    if (currentDate.getTime().before(event.getStartDT())) {
                        Toast.makeText(EventDetailActivity.this, "This event hasn't started yet. Try again when the event has started.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent pinIntent = new Intent(EventDetailActivity.this, EventPINActivity.class);
                        pinIntent.putExtra(Constants.EXTRA_EVENT, event);
                        startActivity(pinIntent);
                    }
                }
                else {
                    Toast.makeText(EventDetailActivity.this, "You have already checked into this event!", Toast.LENGTH_LONG).show();
                }
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

}
