package edu.oregonstate.studentlife.ihcv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    private String[] monthLongNames = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    public static final String EXTRA_EVENT_DETAILED = "Detailed Event";
    public static final String EXTRA_USER = "User";
    private static final String TAG = EventDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

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

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EventsActivity.EXTRA_EVENT) && intent.hasExtra(EventsActivity.EXTRA_USER)) {
            user = (User) intent.getSerializableExtra(EventsActivity.EXTRA_USER);
            Log.d(TAG, "User ID: " + user.getId());
            event = (Event) intent.getSerializableExtra(EventsActivity.EXTRA_EVENT);
            mEventNameTV.setText(event.getName());
            mEventLocationTV.setText(event.getLocation());
            mEventAddressTV.setText(event.getAddress());
            int monthInt = Integer.parseInt(event.getMonth()) - 1;
            mEventDateTimeTV.setText(monthLongNames[monthInt] + " " + event.getDay() + ", "
                    + event.getYear() + " @ " + event.getTime());
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
                Intent enterEventPINIntent = new Intent(EventDetailActivity.this, EventPINActivity.class);
                enterEventPINIntent.putExtra(EXTRA_EVENT_DETAILED, event);
                enterEventPINIntent.putExtra(EXTRA_USER, user);
                startActivity(enterEventPINIntent);
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
                mapIntent.putExtra(EXTRA_EVENT_DETAILED, event);
                startActivity(mapIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
