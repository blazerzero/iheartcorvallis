package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.adapters.EventCardAdapter;
import edu.oregonstate.studentlife.ihcv2.adapters.EventListAdapter;
import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBContract;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBHelper;
import edu.oregonstate.studentlife.ihcv2.data.Session;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.EventLoader;
;

/**
 * Created by Omeed on 12/20/17.
 */

public class EventsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EventListAdapter.OnEventClickListener,
        EventCardAdapter.OnEventClickListener,
        LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = EventsActivity.class.getSimpleName();

    private ImageView mProfilePictureIV;

    private RecyclerView mEventListRV;
    private RecyclerView mEventCardRV;
    private EventListAdapter mEventListAdapter;
    private EventCardAdapter mEventCardAdapter;
    private ArrayList<Event> eventList;
    private User user;
    private SQLiteDatabase mDB;

    Session session;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private final static int IHC_EVENT_LOADER_ID = 0;

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
                if (id == R.id.bottom_nav_dash) {
                    Intent intent = new Intent(EventsActivity.this, DashboardActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_events) {
                    // Do nothing, you're already here
                }
                else if (id == R.id.bottom_nav_passport) {
                    Intent intent = new Intent(EventsActivity.this, PassportActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_resources) {
                    Intent intent = new Intent(EventsActivity.this, ResourcesActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_aboutus) {
                    Intent intent = new Intent(EventsActivity.this, AboutUsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                return false;
            }
        });

        eventList = new ArrayList<Event>();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_USER)) {
            user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            Log.d(TAG, "User ID: " + user.getId());
        }

        mEventListRV = (RecyclerView) findViewById(R.id.rv_event_list);
        mEventListRV.setLayoutManager(new LinearLayoutManager(this));
        mEventListRV.setHasFixedSize(true);

        mEventListAdapter = new EventListAdapter(this);
        mEventListRV.setAdapter(mEventListAdapter);

        mEventCardRV = (RecyclerView) findViewById(R.id.rv_event_card);
        mEventCardRV.setLayoutManager(new LinearLayoutManager(this));
        mEventCardRV.setHasFixedSize(true);

        mEventCardAdapter = new EventCardAdapter(this);
        mEventCardRV.setAdapter(mEventCardAdapter);

        //eventList = mergeSortEventList(eventList);

        mEventCardRV.setVisibility(View.GONE);

        if (isNetworkAvailable()) {
            //new EventReceiver(this).execute();
            getSupportLoaderManager().initLoader(IHC_EVENT_LOADER_ID, null, this);
        }
        else {
            showNoInternetConnectionMsg();
        }

    }

    @Override
    public void onEventClick(Event event) {
        Intent eventDetailActivityIntent = new Intent(this, EventDetailActivity.class);
        eventDetailActivityIntent.putExtra(Constants.EXTRA_EVENT, event);
        eventDetailActivityIntent.putExtra(Constants.EXTRA_USER, user);
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
                if (isNetworkAvailable()) {
                    Intent mapIntent = new Intent(this, MapsActivity.class);
                    mapIntent.putExtra(Constants.EXTRA_EVENT, eventList);
                    startActivity(mapIntent);
                }
                else {
                    showNoInternetConnectionMsg();
                }
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

        if (id == R.id.nav_dash || id == R.id.bottom_nav_dash) {
            Intent intent = new Intent(EventsActivity.this, DashboardActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_events || id == R.id.bottom_nav_events) {
            onBackPressed();
        } else if (id == R.id.nav_passport) {
            Intent intent = new Intent(EventsActivity.this, PassportActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_prizes) {
            Intent intent = new Intent(EventsActivity.this, PrizesActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(EventsActivity.this, LeaderboardActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_resources || id == R.id.bottom_nav_resources) {
            Intent intent = new Intent(EventsActivity.this, ResourcesActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus || id == R.id.bottom_nav_aboutus) {
            Intent intent = new Intent(EventsActivity.this, AboutUsActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(EventsActivity.this, SettingsActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        }  else if (id == R.id.nav_logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new EventLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "got results from loader");
        try {
            StringTokenizer stEvents = new StringTokenizer(data, "\\");
            while (stEvents.hasMoreTokens()) {
                String eventInfoString = stEvents.nextToken();
                JSONObject eventJSON = new JSONObject(eventInfoString);
                int eventid = Integer.parseInt(eventJSON.getString("eventid"));
                String eventName = eventJSON.getString("name");
                String eventLocation = eventJSON.getString("location");
                String eventAddress = eventJSON.getString("address");
                String eventStartDT = eventJSON.getString("startdt");
                String eventEndDT = eventJSON.getString("enddt");
                String eventDescription = eventJSON.getString("description");
                String eventLink1 = eventJSON.getString("link1");
                String eventLink2 = eventJSON.getString("link2");
                String eventLink3 = eventJSON.getString("link3");
                int eventPin = Integer.parseInt(eventJSON.getString("pin"));

                StringTokenizer dateTimeTokenizer = new StringTokenizer(eventStartDT);
                String eventStartYear = dateTimeTokenizer.nextToken("-");
                String eventStartMonth = dateTimeTokenizer.nextToken("-");
                String eventStartDay = dateTimeTokenizer.nextToken(" ");
                String eventStartTime = dateTimeTokenizer.nextToken();

                dateTimeTokenizer = new StringTokenizer(eventEndDT);
                String eventEndYear = dateTimeTokenizer.nextToken("-");
                String eventEndMonth = dateTimeTokenizer.nextToken("-");
                String eventEndDay = dateTimeTokenizer.nextToken(" ");
                String eventEndTime = dateTimeTokenizer.nextToken();

                SimpleDateFormat sdfEvent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date eventStartDate = sdfEvent.parse(eventStartDT);
                Date eventEndDate = sdfEvent.parse(eventEndDT);

                SimpleDateFormat _24HourFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourFormat = new SimpleDateFormat("hh:mm a");

                Date _24HourEventTime = _24HourFormat.parse(eventStartTime);
                eventStartTime = _12HourFormat.format(_24HourEventTime).toString();
                if (eventStartTime.charAt(0) == '0') {
                    eventStartTime = eventStartTime.substring(1);
                }
                eventStartDay = eventStartDay.substring(1);
                if (eventStartDay.charAt(0) == '0') {
                    eventStartDay = eventStartDay.substring(1);
                }

                _24HourEventTime = _24HourFormat.parse(eventEndTime);
                eventEndTime = _12HourFormat.format(_24HourEventTime).toString();
                if (eventEndTime.charAt(0) == '0') {
                    eventEndTime = eventEndTime.substring(1);
                }
                eventEndDay = eventEndDay.substring(1);
                if (eventEndDay.charAt(0) == '0') {
                    eventEndDay = eventEndDay.substring(1);
                }

                Date currentDate = new Date();
                if (eventEndDate.after(currentDate)) {

                    Event retrievedEvent = new Event(eventid, eventName, eventLocation, eventAddress,
                            eventStartDate, eventEndDate, eventStartTime, eventEndTime,
                            eventStartMonth, eventStartDay, eventStartYear,
                            eventEndMonth, eventEndDay, eventEndYear,
                            eventDescription, eventLink1, eventLink2, eventLink3, eventPin);

                    eventList.add(retrievedEvent);
                }

            }

            sortEventsByDate();

            for (Event event : eventList) {
                if (isNetworkAvailable()) {
                    mEventListAdapter.addEvent(event);
                    mEventCardAdapter.addEvent(event);
                } else {
                    showNoInternetConnectionMsg();
                }
            }
            Log.d(TAG, "eventList.size(): " + String.valueOf(eventList.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void showNoInternetConnectionMsg() {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else {
            builder = new android.app.AlertDialog.Builder(this);
        }
        builder.setTitle("No Internet Connection");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close alert. User can try action again.
            }
        });
        builder.setMessage(getResources().getString(R.string.no_internet_connection_msg));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    private void getProfilePicture() {
        Cursor cursor = mDB.query(
                IHCDBContract.SavedImages.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                IHCDBContract.SavedImages.COLUMN_TIMESTAMP + " DESC"
        );

        ArrayList<File> savedImagesList = new ArrayList<File>();
        while (cursor.moveToNext()) {
            File savedImage;
            Uri fileUri = Uri.parse(cursor.getString(
                    cursor.getColumnIndex(IHCDBContract.SavedImages.COLUMN_IMAGE)
            ));
            savedImage = new File(fileUri.getPath());
            savedImagesList.add(savedImage);
        }
        cursor.close();
        Log.d(TAG, "number of images: " + savedImagesList.size());
        File profilePicture = savedImagesList.get(0);
        Log.d(TAG, "path of image: " + profilePicture.getAbsolutePath());
        Bitmap profilePictureBitmap = BitmapFactory.decodeFile(profilePicture.getAbsolutePath());
        mProfilePictureIV.setImageBitmap(profilePictureBitmap);

    }
}