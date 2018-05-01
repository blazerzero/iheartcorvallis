package edu.oregonstate.studentlife.ihcv2Demo;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import edu.oregonstate.studentlife.ihcv2Demo.adapters.EventListAdapter;
import edu.oregonstate.studentlife.ihcv2Demo.adapters.PassportAdapter;
import edu.oregonstate.studentlife.ihcv2Demo.data.Constants;
import edu.oregonstate.studentlife.ihcv2Demo.data.Event;
import edu.oregonstate.studentlife.ihcv2Demo.data.IHCDBContract;
import edu.oregonstate.studentlife.ihcv2Demo.data.IHCDBHelper;
import edu.oregonstate.studentlife.ihcv2Demo.data.Session;
import edu.oregonstate.studentlife.ihcv2Demo.data.User;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EventListAdapter.OnEventClickListener {

    private String email;
    private int numStamps;
    private ImageView mProfilePictureIV;
    private LinearLayout mProgIndicatorLL;
    public static final String EXTRA_USER = "User";

    private TextView mDashStampCountTV;
    private TextView mDashProgressTV;

    private RecyclerView mEventListRV;
    private EventListAdapter mEventListAdapter;

    private RecyclerView mPassportRV;
    private PassportAdapter mPassportAdapter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private File profilePicture;

    Session session;

    private SQLiteDatabase mDB;

    private ArrayList<Event> eventList = new ArrayList<Event>();
    private ArrayList<Event> completedEventList = new ArrayList<Event>();
    private ArrayList<Integer> completedEventIDs = new ArrayList<Integer>();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        IHCDBHelper dbHelper = new IHCDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        overridePendingTransition(0,0);


        try {
            eventList.add(
                new Event(1, "Impact", "College of Public Health and Human Sciences",
                        "College of Public Health and Human Sciences", "160 SW 26th St, Corvallis, OR 97331",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-20 10:00:00"),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-20 14:00:00"),
                        "10:00 AM", "2:00 PM", "5", "20", "2018",
                        "5", "20", "2018", getResources().getString(R.string.hc_impact_event_description),
                        getResources().getDrawable(R.drawable.mu_sunset), "https://health.oregonstate.edu/impact", null, null, 1234)
            );
            eventList.add(
                    new Event(2, "OSU COE Engineering Expo", "College of Engineering", "Kelley Engineering Center",
                            "2500 NW Monroe Ave, Corvallis, OR 97331",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-18 11:00:00"),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-18 16:00:00"),
                            "11:00 AM", "4:00 PM", "5", "18", "2018",
                            "5", "18", "2018", getResources().getString(R.string.hc_expo_event_description),
                            getResources().getDrawable(R.drawable.mu_sunset), "https://expo.engr.oregonstate.edu", null, null, 1078)
            );
            eventList.add(
                    new Event(3, "Corvallis Farmers' Market", "Locally Grown", "Downtown Corvallis",
                            "NW 1st St, Corvallis, OR 97330",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-30 09:00:00"),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-30 13:00:00"),
                            "9:00 AM", "1:00 PM", "5", "30", "2018",
                            "5", "30", "2018", getResources().getString(R.string.hc_farmers_event_description),
                            getResources().getDrawable(R.drawable.mu_sunset), "https://locallygrown.org/home/", null, null, 3676)
            );
            completedEventList.add(
                    new Event(1, "Impact", "College of Public Health and Human Sciences",
                            "College of Public Health and Human Sciences", "160 SW 26th St, Corvallis, OR 97331",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-20 10:00:00"),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-20 14:00:00"),
                            "10:00 AM", "2:00 PM", "5", "20", "2018",
                            "5", "20", "2018", getResources().getString(R.string.hc_impact_event_description),
                            getResources().getDrawable(R.drawable.mu_sunset), "https://health.oregonstate.edu/impact", null, null, 1234)
            );
            completedEventIDs.add(1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        BottomNavigationMenuView bottomMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        try {
            Field shiftingMode = bottomMenuView.getClass().getDeclaredField("mShiftingMode");

            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(bottomMenuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < bottomMenuView.getChildCount(); i++) {
                BottomNavigationItemView bottomItemView = (BottomNavigationItemView) bottomMenuView.getChildAt(i);
                bottomItemView.setShiftingMode(false);
                bottomItemView.setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bottom_nav_dash) {
                    // Do nothing, you're already here
                } else {
                    if (id == R.id.bottom_nav_events) {
                        Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_passport) {
                        Intent intent = new Intent(DashboardActivity.this, PassportActivity.class);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_resources) {
                        Intent intent = new Intent(DashboardActivity.this, ResourcesActivity.class);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_aboutus) {
                        Intent intent = new Intent(DashboardActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        session = new Session(getApplicationContext());
        HashMap<String, String> userBasics = session.getUserDetails();
        email = userBasics.get(Session.KEY_EMAIL);
        Log.d(TAG, "email: " + email);

        mEventListRV = (RecyclerView) findViewById(R.id.rv_event_list);
        mEventListRV.setLayoutManager(new LinearLayoutManager(this));
        mEventListRV.setHasFixedSize(true);

        mEventListAdapter = new EventListAdapter(this);
        mEventListRV.setAdapter(mEventListAdapter);

        mPassportRV = (RecyclerView) findViewById(R.id.rv_passport_list);
        mPassportRV.setLayoutManager(new LinearLayoutManager(this));
        mPassportRV.setHasFixedSize(true);

        mPassportAdapter = new PassportAdapter();
        mPassportRV.setAdapter(mPassportAdapter);

        eventList = new ArrayList<Event>();
        completedEventList = new ArrayList<Event>();

        mDashStampCountTV = (TextView) findViewById(R.id.tv_dash_stamp_count);
        mDashProgressTV = (TextView) findViewById(R.id.tv_dash_progress);
        mProgIndicatorLL = (LinearLayout) findViewById(R.id.progIndicator);

        /*Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_USER)) {
            user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            Log.d(TAG, "User ID: " + user.getId());
            numStamps = user.getStampCount();
        }*/
        numStamps = completedEventList.size();

        mProgIndicatorLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prizeIntent = new Intent(DashboardActivity.this, PrizesActivity.class);
                startActivity(prizeIntent);
            }
        });

        sortEventsByDate();
        for (Event event : completedEventList) {
            mPassportAdapter.addEventToPassport(event);
        }
        for (Event event : eventList) {
            if (!completedEventIDs.contains(event.getEventid())) {
                mEventListAdapter.addEvent(event);
            }
        }
    }

    public void sortEventsByDate() {
        Event temp;
        int indexLeft = 0;
        int indexRight = 1;
        int pointer = 0;

        for (int i = 0; i < eventList.size() - 1; i++) {
            while (indexLeft >= 0 && compareDates(eventList.get(indexLeft), eventList.get(indexRight)) > 0) {
                Collections.swap(eventList, indexLeft, indexRight);
                indexLeft--;
                indexRight--;
            }
            pointer++;
            indexLeft = pointer;
            indexRight = pointer + 1;
        }
    }

    public int compareDates(Event eLeft, Event eRight) {
        return eLeft.getStartDT().compareTo(eRight.getStartDT());
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    public void initProgIndicator() {
        String message;
        int eventsToGo;
        int progColor;
        String stampCountMessage = "STAMPS: " + String.valueOf(numStamps);
        mDashStampCountTV.setText(stampCountMessage);
        if (numStamps >= getResources().getInteger(R.integer.bronzeThreshold)
                && numStamps < getResources().getInteger(R.integer.silverThreshold)) {
            progColor = getResources().getColor(R.color.eventBronze);
            eventsToGo = getResources().getInteger(R.integer.silverThreshold) - numStamps;
            if (eventsToGo == 1) {
                message = "Only " + eventsToGo + " event away from reaching silver status!\nCLICK HERE TO VIEW PRIZES";
            }
            else {
                message = "Only " + eventsToGo + " events away from reaching silver status!\nCLICK HERE TO VIEW PRIZES";
            }
        }
        else if (numStamps >= getResources().getInteger(R.integer.silverThreshold)
                && numStamps < getResources().getInteger(R.integer.goldThreshold)) {
            eventsToGo = getResources().getInteger(R.integer.goldThreshold) - numStamps;
            progColor = getResources().getColor(R.color.eventSilver);
            if (eventsToGo == 1) {
                message = "Only " + eventsToGo + " event away from reaching gold status!\nCLICK HERE TO VIEW PRIZES";
            }
            else {
                message = "Only " + eventsToGo + " events away from reaching gold status!\nCLICK HERE TO VIEW PRIZES";
            }
        }
        else if (numStamps >= getResources().getInteger(R.integer.goldThreshold)) {
            progColor = getResources().getColor(R.color.eventGold);
            message = "Congratulations on achieving gold status!\nCLICK HERE TO VIEW PRIZES";
        }
        else {
            eventsToGo = getResources().getInteger(R.integer.bronzeThreshold) - numStamps;
            progColor = getResources().getColor(R.color.colorAccent);
            if (eventsToGo == 1) {
                message = "Only " + eventsToGo + " event away from reaching bronze status!\nCLICK HERE TO VIEW PRIZES";
            }
            else {
                message = "Only " + eventsToGo + " events away from reaching bronze status!\nCLICK HERE TO VIEW PRIZES";
            }
        }
        mDashProgressTV.setText(message);
        mDashProgressTV.setTextColor(getResources().getColor(R.color.maroon));
        mDashProgressTV.setBackgroundColor(progColor);
        mDashStampCountTV.setBackgroundColor(progColor);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // session information is retrieved and displayed on nav menu
        //session = new Session(getApplicationContext());
        String name = "Test User";
        String email = "testuser@oregonstate.edu";
        mProfilePictureIV = (ImageView) findViewById(R.id.iv_profile_picture);
        getProfilePicture();

        TextView sesName = (TextView) findViewById(R.id.sesName);
        TextView sesEmail = (TextView) findViewById(R.id.sesEmail);
        sesName.setText(name);
        sesEmail.setText(email);

        return super.onPrepareOptionsMenu(menu);
    }

    // possibly change to just navigate to the events page
    @Override
    public void onEventClick(Event event) {
        Intent eventDetailActivityIntent = new Intent(this, EventDetailActivity.class);
        eventDetailActivityIntent.putExtra(Constants.EXTRA_EVENT, event);
        startActivity(eventDetailActivityIntent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_dash || id == R.id.bottom_nav_dash) {
            onBackPressed();
        } else {
            if (id == R.id.nav_events) {
                Intent intent = new Intent(this, EventsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_passport) {
                Intent intent = new Intent(this, PassportActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_prizes) {
                Intent intent = new Intent(this, PrizesActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_leaderboard) {
                Intent intent = new Intent(this, LeaderboardActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_resources) {
                Intent intent = new Intent(this, ResourcesActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_aboutus) {
                Intent intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
            }
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    public void getProfilePicture() {
        Cursor cursor = mDB.query(
                IHCDBContract.SavedImages.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                IHCDBContract.SavedImages.COLUMN_TIMESTAMP + " DESC"
        );

        Uri fileUri = null;
        if (cursor.moveToNext()) {
            fileUri = Uri.parse(cursor.getString(
                    cursor.getColumnIndex(IHCDBContract.SavedImages.COLUMN_IMAGE)
            ));
        }
        if (fileUri != null) {
            String filePath = fileUri.toString();
            Log.d(TAG, "path of image: " + filePath);
            if (filePath.contains(".jpg") || filePath.contains(".jpeg") || filePath.contains(".png")) {
                if (!filePath.contains("file://")) {
                    filePath = "file://" + filePath;
                }
                Picasso.with(this)
                        .load(filePath)
                        .into(mProfilePictureIV);
            }
        }
        cursor.close();
    }

}
