package edu.oregonstate.studentlife.ihcv2;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailActivity extends AppCompatActivity {

    private ImageView mEventImageIV;
    private TextView mEventNameTV;
    private TextView mEventLocationTV;
    private TextView mEventDateTimeTV;
    private TextView mEventDescriptionTV;
    private TextView mEventLink1TV;
    private TextView mEventLink2TV;
    private TextView mEventLink3TV;

    private Event event;

    private String[] monthLongNames = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

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

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        overridePendingTransition(0,0);

        mEventImageIV = (ImageView) findViewById(R.id.iv_event_image_detail);
        mEventNameTV = (TextView) findViewById(R.id.tv_event_name_detail);
        mEventLocationTV = (TextView) findViewById(R.id.tv_event_location_detail);
        mEventDateTimeTV = (TextView) findViewById(R.id.tv_event_datetime_detail);
        mEventDescriptionTV = (TextView) findViewById(R.id.tv_event_description_detail);
        mEventLink1TV = (TextView) findViewById(R.id.tv_event_link1_detail);
        mEventLink2TV = (TextView) findViewById(R.id.tv_event_link2_detail);
        mEventLink3TV = (TextView) findViewById(R.id.tv_event_link3_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EventsActivity.EXTRA_EVENT)) {
            event = (Event) intent.getSerializableExtra(EventsActivity.EXTRA_EVENT);
            mEventNameTV.setText(event.getName());
            mEventLocationTV.setText(event.getLocation());
            int monthInt = Integer.parseInt(event.getMonth()) - 1;
            mEventDateTimeTV.setText(monthLongNames[monthInt] + " " + event.getDay() + ", "
                    + event.getYear() + " @ " + event.getTime());
            mEventDescriptionTV.setText(event.getDescription());
        }
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
