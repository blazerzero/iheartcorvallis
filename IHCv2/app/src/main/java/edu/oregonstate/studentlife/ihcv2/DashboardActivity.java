package edu.oregonstate.studentlife.ihcv2;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.DialogInterface;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.adapters.EventListAdapter;
import edu.oregonstate.studentlife.ihcv2.adapters.PassportAdapter;
import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.EventLoader;
import edu.oregonstate.studentlife.ihcv2.loaders.PassportLoader;
import edu.oregonstate.studentlife.ihcv2.loaders.UserInfoLoader;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EventListAdapter.OnEventClickListener,
        LoaderManager.LoaderCallbacks<String> {

    private String email;
    private int numStamps;
    public LinearLayout mProgIndicatorLL;
    public static final String EXTRA_USER = "User";
    private final static String IHC_USER_EMAIL_KEY = "IHC_USER_EMAIL";
    private final static int IHC_USER_LOADER_ID = 0;
    private final static int IHC_PASSPORT_LOADER_ID = 1;
    private final static int IHC_EVENT_LOADER_ID = 2;

    private TextView mDashStampCountTV;
    private TextView mDashProgressTV;

    private String[] monthShortNames = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."};

    private RecyclerView mEventListRecyclerView;
    private ArrayList<Event> eventList;
    private EventListAdapter mEventListAdapter;

    private RecyclerView mPassportRecyclerView;
    private ArrayList<Event> completedEventList;
    private PassportAdapter mPassportAdapter;

    boolean gotUser = false;
    boolean gotPassport = false;
    boolean gotEvents = false;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private User user;

    SessionActivity session;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        overridePendingTransition(0,0);

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
                }
                else if (id == R.id.bottom_nav_events) {
                    Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_passport) {
                    Intent intent = new Intent(DashboardActivity.this, PassportActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_resources) {
                    Intent intent = new Intent(DashboardActivity.this, ResourcesActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_aboutus) {
                    Intent intent = new Intent(DashboardActivity.this, AboutUsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                return false;
            }
        });

        session = new SessionActivity(getApplicationContext());
        HashMap<String, String> userBasics = session.getUserDetails();
        email = userBasics.get(SessionActivity.KEY_EMAIL);

        mEventListRecyclerView = (RecyclerView) findViewById(R.id.rv_event_list);
        mEventListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEventListRecyclerView.setHasFixedSize(true);

        mEventListAdapter = new EventListAdapter(this);
        mEventListRecyclerView.setAdapter(mEventListAdapter);

        mPassportRecyclerView = (RecyclerView) findViewById(R.id.rv_passport_list);
        mPassportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPassportRecyclerView.setHasFixedSize(true);

        mPassportAdapter = new PassportAdapter();
        mPassportRecyclerView.setAdapter(mPassportAdapter);

        eventList = new ArrayList<Event>();
        completedEventList = new ArrayList<Event>();

        mDashStampCountTV = (TextView)findViewById(R.id.tv_dash_stamp_count);
        mDashProgressTV = (TextView) findViewById(R.id.tv_dash_progress);
        mProgIndicatorLL = (LinearLayout)findViewById(R.id.progIndicator);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_USER)) {
            user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            Log.d(TAG, "User ID: " + user.getId());
            numStamps = Integer.parseInt(user.getStampCount());
        }
        else {
            Bundle args = new Bundle();
            args.putString(IHC_USER_EMAIL_KEY, email);
            getSupportLoaderManager().initLoader(IHC_USER_LOADER_ID, args, this);
        }

        initProgIndicator();

        mProgIndicatorLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PrizesActivity.class);
                intent.putExtra(Constants.EXTRA_USER, user);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        if (args != null) {
            email = args.getString(IHC_USER_EMAIL_KEY);
        }
        if (id == IHC_USER_LOADER_ID) {
            return new UserInfoLoader(this, email);
        }
        else if (id == IHC_PASSPORT_LOADER_ID) {
            return new PassportLoader(this, email);
        }
        else if (id == IHC_EVENT_LOADER_ID){
            return new EventLoader(this);
        }
        else
            return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Bundle args = new Bundle();
        args.putString(IHC_USER_EMAIL_KEY, email);
        if (!gotUser && !gotPassport && !gotEvents) {
            // user stuff
            if (data != null) {
                try {
                    JSONObject userJSON = new JSONObject(data);
                    String firstname = userJSON.getString("firstname");
                    String lastname = userJSON.getString("lastname");
                    String email = userJSON.getString("email");
                    int id = Integer.parseInt(userJSON.getString("id"));
                    String stampcount = userJSON.getString("stampcount");
                    int didsurvey = Integer.parseInt(userJSON.getString("didsurvey"));
                    if (didsurvey == 0) {
                        Intent surveyIntent = new Intent(this, SurveyActivity.class);
                        surveyIntent.putExtra(Constants.EXTRA_USER_ID, id);
                        startActivity(surveyIntent);
                    }
                    int grade = Integer.parseInt(userJSON.getString("grade"));
                    //int age = Integer.parseInt(userJSON.getString("age"));
                    String birthDateString = userJSON.getString("birthdate");
                    int type = Integer.parseInt(userJSON.getString("type"));

                    SimpleDateFormat sdfBirthDate = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthDate = sdfBirthDate.parse(birthDateString);

                    user = new User(firstname, lastname, email, id, stampcount, didsurvey, grade, birthDate, type);
                    numStamps = Integer.parseInt(stampcount);
                    initProgIndicator();


                    mDashStampCountTV.setText("STAMPS: " + String.valueOf(numStamps));

                    gotUser = true;
                    getSupportLoaderManager().initLoader(IHC_PASSPORT_LOADER_ID, args, this);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Error");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close alert dialog
                    }
                });
                builder.setMessage("Error retrieving user info.");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        }
        else if (gotUser && !gotPassport && !gotEvents) {
            // passport stuff
            Log.d(TAG, "got results from loader");
            try {
                StringTokenizer stEvents = new StringTokenizer(data, "\\");
                while (stEvents.hasMoreTokens()) {
                    String eventInfoString = stEvents.nextToken();
                    Log.d(TAG, "eventInfoString: " + eventInfoString);
                    JSONObject eventJSON = new JSONObject(eventInfoString);
                    Log.d(TAG, "eventJSON: " + eventJSON);
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

                    if (eventStartMonth.charAt(0) == '0') {
                        eventStartMonth = eventStartMonth.substring(1);
                    }

                    int monthInt = Integer.parseInt(eventStartMonth);
                    eventStartMonth = monthShortNames[monthInt - 1];

                    _24HourEventTime = _24HourFormat.parse(eventEndTime);
                    eventEndTime = _12HourFormat.format(_24HourEventTime).toString();
                    if (eventEndTime.charAt(0) == '0') {
                        eventEndTime = eventEndTime.substring(1);
                    }
                    eventEndDay = eventEndDay.substring(1);
                    if (eventEndDay.charAt(0) == '0') {
                        eventEndDay = eventEndDay.substring(1);
                    }

                    if (eventEndMonth.charAt(0) == '0') {
                        eventEndMonth = eventEndMonth.substring(1);
                    }

                    monthInt = Integer.parseInt(eventEndMonth);
                    eventEndMonth = monthShortNames[monthInt - 1];

                    Event retrievedEvent = new Event(eventid, eventName, eventLocation, eventAddress,
                            eventStartDate, eventEndDate, eventStartTime, eventEndTime,
                            eventStartMonth, eventStartDay, eventStartYear,
                            eventEndMonth, eventEndDay, eventEndYear,
                            eventDescription, eventLink1, eventLink2, eventLink3, eventPin);

                    completedEventList.add(retrievedEvent);
                }


                sortEventsByDate();

                mPassportAdapter.addEventToPassport(completedEventList.get(completedEventList.size()-1));
                mPassportAdapter.addEventToPassport(completedEventList.get(completedEventList.size()-2));

                gotPassport = true;
                getSupportLoaderManager().initLoader(IHC_EVENT_LOADER_ID, null, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (gotUser && gotPassport && !gotEvents) {
            // passport stuff
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

                mEventListAdapter.addEvent(eventList.get(0));
                mEventListAdapter.addEvent(eventList.get(1));

                gotEvents = true;
            } catch (Exception e) {
                e.printStackTrace();
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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
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
        //mProgIndicatorLL.setBackgroundColor(progColor);
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
            settingsIntent.putExtra(Constants.EXTRA_USER, user);
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
        //session = new SessionActivity(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(SessionActivity.KEY_NAME);
        String email = user.get(SessionActivity.KEY_EMAIL);
        TextView sesName = (TextView) findViewById(R.id.sesName);
        TextView sesEmail = (TextView) findViewById(R.id.sesEmail);
        sesName.setText(name);
        sesEmail.setText(email);

        return super.onPrepareOptionsMenu(menu);
    }

    public void showNoInternetConnectionMsg() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else {
            builder = new AlertDialog.Builder(this);
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

    // possibly change to just navigate to the events page
    @Override
    public void onEventClick(Event event) {
        Intent eventDetailActivityIntent = new Intent(this, EventDetailActivity.class);
        eventDetailActivityIntent.putExtra(Constants.EXTRA_EVENT, event);
        eventDetailActivityIntent.putExtra(Constants.EXTRA_USER, user);
        startActivity(eventDetailActivityIntent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dash || id == R.id.bottom_nav_dash) {
            onBackPressed();
        } else if (id == R.id.nav_events || id == R.id.bottom_nav_events) {
            Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_passport) {
            Intent intent = new Intent(DashboardActivity.this, PassportActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_prizes) {
            Intent intent = new Intent(DashboardActivity.this, PrizesActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(DashboardActivity.this, LeaderboardActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_resources || id == R.id.bottom_nav_resources) {
            Intent intent = new Intent(DashboardActivity.this, ResourcesActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus || id == R.id.bottom_nav_aboutus) {
            Intent intent = new Intent(DashboardActivity.this, AboutUsActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = new Bundle();
        args.putString(IHC_USER_EMAIL_KEY, email);
        getSupportLoaderManager().initLoader(IHC_USER_LOADER_ID, args, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
