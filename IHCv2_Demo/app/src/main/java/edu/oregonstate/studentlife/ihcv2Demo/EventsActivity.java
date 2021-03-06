package edu.oregonstate.studentlife.ihcv2Demo;

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import edu.oregonstate.studentlife.ihcv2Demo.adapters.EventCardAdapter;
import edu.oregonstate.studentlife.ihcv2Demo.adapters.EventListAdapter;
import edu.oregonstate.studentlife.ihcv2Demo.data.Constants;
import edu.oregonstate.studentlife.ihcv2Demo.data.Event;
import edu.oregonstate.studentlife.ihcv2Demo.data.IHCDBContract;
import edu.oregonstate.studentlife.ihcv2Demo.data.IHCDBHelper;
import edu.oregonstate.studentlife.ihcv2Demo.data.Session;
import edu.oregonstate.studentlife.ihcv2Demo.data.User;
;

/**
 * Created by Omeed on 12/20/17.
 */

public class EventsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EventListAdapter.OnEventClickListener,
        EventCardAdapter.OnEventClickListener {

    private final static String TAG = EventsActivity.class.getSimpleName();

    private ImageView mProfilePictureIV;

    private RecyclerView mEventListRV;
    private RecyclerView mEventCardRV;
    private EventListAdapter mEventListAdapter;
    private EventCardAdapter mEventCardAdapter;
    private ArrayList<Event> eventList;
    private SQLiteDatabase mDB;

    Session session;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        IHCDBHelper dbHelper = new IHCDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        overridePendingTransition(0,0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

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

                if (id == R.id.bottom_nav_events) {
                    // Do nothing, you're already here
                } else {
                    if (id == R.id.bottom_nav_dash) {
                        Intent intent = new Intent(EventsActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_passport) {
                        Intent intent = new Intent(EventsActivity.this, PassportActivity.class);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_resources) {
                        Intent intent = new Intent(EventsActivity.this, ResourcesActivity.class);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_aboutus) {
                        Intent intent = new Intent(EventsActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        eventList = new ArrayList<Event>();
        try {
            eventList.add(
                    new Event(1, "Impact", "College of Public Health and Human Sciences",
                            "College of Public Health and Human Sciences", "160 SW 26th St, Corvallis, OR 97331",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("1900-01-01 00:00:00"),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2099-12-31 23:59:59"),
                            "12:00 AM", "11:59 PM", "1", "1", "1900",
                            "12", "31", "2099", getResources().getString(R.string.hc_impact_event_description),
                            "https://health.oregonstate.edu/impact", "", "", 1234)
            );
            eventList.add(
                    new Event(2, "OSU COE Engineering Expo", "College of Engineering", "Kelley Engineering Center",
                            "2500 NW Monroe Ave, Corvallis, OR 97331",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-18 11:00:00"),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-18 16:00:00"),
                            "11:00 AM", "4:00 PM", "5", "18", "2018",
                            "5", "18", "2018", getResources().getString(R.string.hc_expo_event_description),
                            "https://expo.engr.oregonstate.edu", "", "", 1078)
            );
            eventList.add(
                    new Event(3, "Corvallis Farmers' Market", "Locally Grown", "Downtown Corvallis",
                            "NW 1st St, Corvallis, OR 97330",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-30 09:00:00"),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2018-05-30 13:00:00"),
                            "9:00 AM", "1:00 PM", "5", "30", "2018",
                            "5", "30", "2018", getResources().getString(R.string.hc_farmers_event_description),
                            "https://locallygrown.org/home/", "", "", 3676)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        mEventListRV = (RecyclerView) findViewById(R.id.rv_event_list);
        mEventListRV.setLayoutManager(new LinearLayoutManager(this));
        mEventListRV.setHasFixedSize(true);

        mEventListAdapter = new EventListAdapter(this);
        mEventListRV.setAdapter(mEventListAdapter);

        mEventCardRV = (RecyclerView) findViewById(R.id.rv_event_card);
        mEventCardRV.setLayoutManager(new LinearLayoutManager(this));
        mEventCardRV.setHasFixedSize(true);

        mEventCardAdapter = new EventCardAdapter(this, this);
        mEventCardRV.setAdapter(mEventCardAdapter);

        mEventCardRV.setVisibility(View.GONE);

        for (Event event : eventList) {
            mEventListAdapter.addEvent(event);
            mEventCardAdapter.addEvent(event);
        }

    }

    @Override
    public void onEventClick(Event event) {
        Intent eventDetailActivityIntent = new Intent(this, EventDetailActivity.class);
        eventDetailActivityIntent.putExtra(Constants.EXTRA_EVENT, event);
        startActivity(eventDetailActivityIntent);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
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

    public int monthValue(String month) {
        int value = 0;
        if (month.equals("January")) value = Calendar.JANUARY + 1;
        else if (month.equals("February")) value = Calendar.FEBRUARY + 1;
        else if (month.equals("March")) value = Calendar.MARCH + 1;
        else if (month.equals("April")) value = Calendar.APRIL + 1;
        else if (month.equals("May")) value = Calendar.MAY + 1;
        else if (month.equals("June")) value = Calendar.JUNE + 1;
        else if (month.equals("July")) value = Calendar.JULY + 1;
        else if (month.equals("August")) value = Calendar.AUGUST + 1;
        else if (month.equals("September")) value = Calendar.SEPTEMBER + 1;
        else if (month.equals("October")) value = Calendar.OCTOBER + 1;
        else if (month.equals("November")) value = Calendar.NOVEMBER + 1;
        else if (month.equals("December")) value = Calendar.DECEMBER + 1;
        return value;
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
        getMenuInflater().inflate(R.menu.events_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {

            case R.id.action_map:


                return true;
            case R.id.action_switch_view:
                if (mEventListRV.getVisibility() == View.VISIBLE && mEventCardRV.getVisibility() == View.GONE) {
                    mEventListRV.setVisibility(View.GONE);
                    mEventCardRV.setVisibility(View.VISIBLE);
                } else if (mEventListRV.getVisibility() == View.GONE && mEventCardRV.getVisibility() == View.VISIBLE) {
                    mEventCardRV.setVisibility(View.GONE);
                    mEventListRV.setVisibility(View.VISIBLE);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        session = new Session(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(Session.KEY_NAME);
        String email = user.get(Session.KEY_EMAIL);
        mProfilePictureIV = (ImageView) findViewById(R.id.iv_profile_picture);
        getProfilePicture();

        TextView sesName = (TextView) findViewById(R.id.sesName);
        TextView sesEmail = (TextView) findViewById(R.id.sesEmail);
        sesName.setText(name);
        sesEmail.setText(email);
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_events) {
            onBackPressed();
        } else {
            if (id == R.id.nav_dash) {
                Intent intent = new Intent(this, DashboardActivity.class);
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
                session.logoutUser();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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