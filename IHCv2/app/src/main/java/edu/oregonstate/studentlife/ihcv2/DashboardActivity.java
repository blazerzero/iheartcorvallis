package edu.oregonstate.studentlife.ihcv2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;

import android.content.res.Configuration;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.DialogInterface;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import edu.oregonstate.studentlife.ihcv2.adapters.EventListAdapter;
import edu.oregonstate.studentlife.ihcv2.adapters.PassportAdapter;
import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBContract;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBHelper;
import edu.oregonstate.studentlife.ihcv2.data.Session;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.EventLoader;
import edu.oregonstate.studentlife.ihcv2.loaders.PassportLoader;
import edu.oregonstate.studentlife.ihcv2.loaders.UserInfoLoader;

/**
 * Utilizes EventLoader, UserInfoLoader and PassportLoader to populate the Dashboard Page with information relevant
 * to the current user.
 */
public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EventListAdapter.OnEventClickListener,
        LoaderManager.LoaderCallbacks<String> {

    private String email;
    private int userid;
    private int numStamps;
    private ImageView mProfilePictureIV;
    private LinearLayout mProgIndicatorLL;
    public static final String EXTRA_USER = "User";
    private final static String IHC_USER_EMAIL_KEY = "IHC_USER_EMAIL";
    private final static String IHC_USER_ID_KEY = "IHC_USER_ID";
    private final static int IHC_USER_LOADER_ID = 0;
    private final static int IHC_PASSPORT_LOADER_ID = 1;
    private final static int IHC_EVENT_LOADER_ID = 2;

    private TextView mDashStampCountTV;
    private TextView mDashProgressTV;

    private RecyclerView mEventListRV;
    private ArrayList<Event> eventList;
    private EventListAdapter mEventListAdapter;

    private RecyclerView mPassportRV;
    private ArrayList<Event> completedEventList;
    private PassportAdapter mPassportAdapter;

    boolean gotUser;
    boolean gotPassport;
    boolean gotEvents;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private User user = null;
    private File profilePicture;

    Session session;

    private SQLiteDatabase mDB;
    private String userStatus = null;

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

        gotUser = false;
        gotPassport = false;
        gotEvents = false;

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
                if (user != null) {
                    if (id == R.id.bottom_nav_dash) {
                        // Do nothing, you're already here
                    } else {
                        if (id == R.id.bottom_nav_events) {
                            Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
                            intent.putExtra(Constants.EXTRA_USER, user);
                            startActivity(intent);
                        } else if (id == R.id.bottom_nav_passport) {
                            Intent intent = new Intent(DashboardActivity.this, PassportActivity.class);
                            intent.putExtra(Constants.EXTRA_USER, user);
                            startActivity(intent);
                        } else if (id == R.id.bottom_nav_resources) {
                            Intent intent = new Intent(DashboardActivity.this, ResourcesActivity.class);
                            intent.putExtra(Constants.EXTRA_USER, user);
                            startActivity(intent);
                        } else if (id == R.id.bottom_nav_aboutus) {
                            Intent intent = new Intent(DashboardActivity.this, AboutUsActivity.class);
                            intent.putExtra(Constants.EXTRA_USER, user);
                            startActivity(intent);
                        }
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

        Bundle args = new Bundle();
        args.putString(IHC_USER_EMAIL_KEY, email);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_USER)) {
            user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            Log.d(TAG, "User ID: " + user.getId());
            numStamps = user.getStampCount();
            gotUser = true;
            args.putInt(IHC_USER_ID_KEY, user.getId());
            getSupportLoaderManager().initLoader(IHC_PASSPORT_LOADER_ID, args, this);
        }
        else {
            getSupportLoaderManager().initLoader(IHC_USER_LOADER_ID, args, this);
        }

        mProgIndicatorLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prizeIntent = new Intent(DashboardActivity.this, PrizesActivity.class);
                prizeIntent.putExtra(Constants.EXTRA_USER, user);
                startActivity(prizeIntent);
            }
        });
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        if (args != null) {
            email = args.getString(IHC_USER_EMAIL_KEY);
            userid = args.getInt(IHC_USER_ID_KEY);
        }
        Log.e(TAG, "loader id: " + id);
        if (id == IHC_USER_LOADER_ID) {
            Log.d(TAG, "user loader beginning");
            return new UserInfoLoader(this, email);
        }
        else if (id == IHC_PASSPORT_LOADER_ID) {
            Log.d(TAG, "passport loader beginning");
            return new PassportLoader(this, String.valueOf(userid));
        }
        else if (id == IHC_EVENT_LOADER_ID){
            Log.d(TAG, "event loader beginning");
            return new EventLoader(this);
        }
        else
            return null;
    }

    // After each loader is called, parse data into variables for later use
    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Bundle args = new Bundle();
        args.putString(IHC_USER_EMAIL_KEY, email);
        Log.d(TAG, "gotUser = " + gotUser);
        Log.d(TAG, "gotPassport = " + gotPassport);
        Log.d(TAG, "gotEvents = " + gotEvents);

        // First loader to run is UserInfoLoader, once ran, parse through retrieved info into
        // variables to populate the page
        if (!gotUser && !gotPassport && !gotEvents) {
            Log.d(TAG, "user data: " + data);
            if (data != null) {
                try {
                    JSONObject userJSON = new JSONObject(data);
                    String firstname = userJSON.getString("firstname");
                    String lastname = userJSON.getString("lastname");
                    //String email = userJSON.getString("email");
                    int studentid = Integer.parseInt(userJSON.getString("studentid"));
                    String onid = userJSON.getString("onid");
                    int id = Integer.parseInt(userJSON.getString("id"));
                    numStamps = Integer.parseInt(userJSON.getString("stampcount"));
                    int didsurvey = Integer.parseInt(userJSON.getString("didsurvey"));
                    int grade = Integer.parseInt(userJSON.getString("grade"));
                    String birthDateString = userJSON.getString("birthdate");
                    int type = Integer.parseInt(userJSON.getString("type"));

                    SimpleDateFormat sdfBirthDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    Date birthDate = sdfBirthDate.parse(birthDateString);

                    Log.d(TAG, "birthdate: " + birthDate.toString());
                    Log.d(TAG, "grade: " + grade);

                    user = new User(firstname, lastname, email, id, numStamps, didsurvey, grade, birthDate, type);
                    Log.d(TAG, "studentid: " + studentid);
                    Log.d(TAG, "onid.length(): " + onid.length());
                    Log.d(TAG, "type: " + type);

                    if (studentid == -1 || birthDate.toString().equals("Sun Nov 30 00:00:00 PST 2") || type == -1) {
                        Log.d(TAG, "about to get information from user");
                        Intent getUserInfoIntent = new Intent(this, GetUserInfoActivity.class);
                        getUserInfoIntent.putExtra(Constants.EXTRA_USER, user);
                        startActivity(getUserInfoIntent);
                    }
                    
                    else if (didsurvey == 0) {
                        Intent surveyIntent = new Intent(this, SurveyActivity.class);
                        surveyIntent.putExtra(Constants.EXTRA_USER, user);
                        startActivity(surveyIntent);
                    }

                    gotUser = true;
                    args.putInt(IHC_USER_ID_KEY, user.getId());
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
        // Second loader is PassportLoader, so parse through that info next
        else if (gotUser && !gotPassport && !gotEvents) {
            Log.d(TAG, "passport data: " + data);
            // passport stuff
            Log.d(TAG, "got results from loader");
            try {
                StringTokenizer stEvents = new StringTokenizer(data, "\\");
                while (stEvents.hasMoreTokens()) {
                    String eventInfoString = stEvents.nextToken();
                    Log.d(TAG, "eventInfoString: " + eventInfoString);
                    JSONObject passportEventJSON = new JSONObject(eventInfoString);
                    Log.d(TAG, "passportEventJSON: " + passportEventJSON);
                    int eventid = Integer.parseInt(passportEventJSON.getString("eventid"));
                    String eventName = passportEventJSON.getString("name");
                    String eventLocation = passportEventJSON.getString("location");
                    String eventAddress = passportEventJSON.getString("address");
                    String eventStartDT = passportEventJSON.getString("startdt");
                    String eventEndDT = passportEventJSON.getString("enddt");
                    String eventDescription = passportEventJSON.getString("description");
                    String eventImageName = passportEventJSON.getString("image");
                    String eventLink1 = passportEventJSON.getString("link1");
                    String eventLink2 = passportEventJSON.getString("link2");
                    String eventLink3 = passportEventJSON.getString("link3");
                    int eventPin = Integer.parseInt(passportEventJSON.getString("pin"));

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

                    SimpleDateFormat sdfEvent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    Date eventStartDate = sdfEvent.parse(eventStartDT);
                    Date eventEndDate = sdfEvent.parse(eventEndDT);

                    SimpleDateFormat _24HourFormat = new SimpleDateFormat("HH:mm", Locale.US);
                    SimpleDateFormat _12HourFormat = new SimpleDateFormat("hh:mm a", Locale.US);

                    Date _24HourEventTime = _24HourFormat.parse(eventStartTime);
                    eventStartTime = _12HourFormat.format(_24HourEventTime);
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

                    /*int monthInt = Integer.parseInt(eventStartMonth);
                    eventStartMonth = monthShortNames[monthInt - 1];*/

                    _24HourEventTime = _24HourFormat.parse(eventEndTime);
                    eventEndTime = _12HourFormat.format(_24HourEventTime);
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

                    /*monthInt = Integer.parseInt(eventEndMonth);
                    eventEndMonth = monthShortNames[monthInt - 1];*/

                    String eventImagePath = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/images/events/" + eventImageName;

                    Event retrievedEvent = new Event(eventid, eventName, eventLocation, eventAddress,
                            eventStartDate, eventEndDate, eventStartTime, eventEndTime,
                            eventStartMonth, eventStartDay, eventStartYear,
                            eventEndMonth, eventEndDay, eventEndYear,
                            eventDescription, eventImagePath, eventLink1, eventLink2, eventLink3, eventPin);

                    completedEventList.add(retrievedEvent);
                }


                sortEventsByDate();

                if (completedEventList.size() > 0) {
                    mPassportAdapter.addEventToPassport(completedEventList.get(completedEventList.size() - 1));
                }
                if (completedEventList.size() > 1) {
                    mPassportAdapter.addEventToPassport(completedEventList.get(completedEventList.size() - 2));
                }

                gotPassport = true;
                getSupportLoaderManager().initLoader(IHC_EVENT_LOADER_ID, null, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Finally parse through the info gained from the EventsLoader
        else if (gotUser && gotPassport && !gotEvents) {
            Log.d(TAG, "event data: " + data);

            try {
                StringTokenizer stEvents = new StringTokenizer(data, "\\");
                while (stEvents.hasMoreTokens()) {
                    String eventInfoString = stEvents.nextToken();
                    JSONObject eventJSON = new JSONObject(eventInfoString);
                    Log.d(TAG, "eventJSON: " + eventJSON);
                    int eventid = Integer.parseInt(eventJSON.getString("eventid"));
                    String eventName = eventJSON.getString("name");
                    String eventLocation = eventJSON.getString("location");
                    String eventAddress = eventJSON.getString("address");
                    String eventStartDT = eventJSON.getString("startdt");
                    String eventEndDT = eventJSON.getString("enddt");
                    String eventDescription = eventJSON.getString("description");
                    String eventImageName = eventJSON.getString("image");
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

                    SimpleDateFormat sdfEvent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    sdfEvent.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                    Date eventStartDate = sdfEvent.parse(eventStartDT);
                    Date eventEndDate = sdfEvent.parse(eventEndDT);

                    SimpleDateFormat _24HourFormat = new SimpleDateFormat("HH:mm", Locale.US);
                    _24HourFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                    SimpleDateFormat _12HourFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                    _12HourFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

                    Date _24HourEventTime = _24HourFormat.parse(eventStartTime);
                    eventStartTime = _12HourFormat.format(_24HourEventTime);
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

                    _24HourEventTime = _24HourFormat.parse(eventEndTime);
                    eventEndTime = _12HourFormat.format(_24HourEventTime);
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

                    Date currentDate = new Date();
                    Log.d(TAG, "currentDate: " + currentDate);
                    Log.d(TAG, "eventEndDate " + eventEndDate);

                    ArrayList<Integer> completedEventIDs = new ArrayList<Integer>();
                    for (Event event : completedEventList) {
                        completedEventIDs.add(event.getEventid());
                    }

                    String eventImagePath = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/images/events/" + eventImageName;

                    Calendar visibleDateLimit = Calendar.getInstance(TimeZone.getTimeZone("America/LosAngeles"));
                    visibleDateLimit.setTime(eventEndDate);
                    visibleDateLimit.add(Calendar.HOUR_OF_DAY, 2);

                    if (currentDate.before(visibleDateLimit.getTime()) && !completedEventIDs.contains(eventid)) {  // if the event can still be attended and has not already been attended

                        Event retrievedEvent = new Event(eventid, eventName, eventLocation, eventAddress,
                                eventStartDate, eventEndDate, eventStartTime, eventEndTime,
                                eventStartMonth, eventStartDay, eventStartYear,
                                eventEndMonth, eventEndDay, eventEndYear,
                                eventDescription, eventImagePath, eventLink1, eventLink2, eventLink3, eventPin);

                        eventList.add(retrievedEvent);
                    }
                }

                sortEventsByDate();

                if (eventList.size() > 0) {
                    mEventListAdapter.addEvent(eventList.get(0));
                }
                if (eventList.size() > 1) {
                    mEventListAdapter.addEvent(eventList.get(1));
                }

                gotEvents = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initProgIndicator();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_CALLING_ACTIVITY_ID)) {
            String callingActivity = (String) intent.getSerializableExtra(Constants.EXTRA_CALLING_ACTIVITY_ID);
            if (callingActivity.equals(EventPINActivity.class.getSimpleName())
                    && user.getStampCount() == getResources().getInteger(R.integer.bronzeThreshold)
                    || user.getStampCount() == getResources().getInteger(R.integer.silverThreshold)
                    || user.getStampCount() == getResources().getInteger(R.integer.goldThreshold)) {
                Intent surveyIntent = new Intent (this, SurveyActivity.class);
                surveyIntent.putExtra(Constants.EXTRA_USER, user);
                startActivity(surveyIntent);
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
        String stampCountMessage = "STAMPS: " + String.valueOf(user.getStampCount());
        mDashStampCountTV.setText(stampCountMessage);
        if (user.getStampCount() >= getResources().getInteger(R.integer.bronzeThreshold)
                && user.getStampCount() < getResources().getInteger(R.integer.silverThreshold)) {
            progColor = getResources().getColor(R.color.eventBronze);
            eventsToGo = getResources().getInteger(R.integer.silverThreshold) - user.getStampCount();
            if (eventsToGo == 1) {
                message = "Only " + eventsToGo + " event away from reaching silver status!\nCLICK HERE TO VIEW PRIZES";
            }
            else {
                message = "Only " + eventsToGo + " events away from reaching silver status!\nCLICK HERE TO VIEW PRIZES";
            }
        }
        else if (user.getStampCount() >= getResources().getInteger(R.integer.silverThreshold)
                && user.getStampCount() < getResources().getInteger(R.integer.goldThreshold)) {
            eventsToGo = getResources().getInteger(R.integer.goldThreshold) - user.getStampCount();
            progColor = getResources().getColor(R.color.eventSilver);
            if (eventsToGo == 1) {
                message = "Only " + eventsToGo + " event away from reaching gold status!\nCLICK HERE TO VIEW PRIZES";
            }
            else {
                message = "Only " + eventsToGo + " events away from reaching gold status!\nCLICK HERE TO VIEW PRIZES";
            }
        }
        else if (user.getStampCount() >= getResources().getInteger(R.integer.goldThreshold)) {
            progColor = getResources().getColor(R.color.eventGold);
            message = "Congratulations on achieving gold status!\nCLICK HERE TO VIEW PRIZES";
        }
        else {
            eventsToGo = getResources().getInteger(R.integer.bronzeThreshold) - user.getStampCount();
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
        //session = new Session(getApplicationContext());
        HashMap<String, String> userHash = session.getUserDetails();
        String name = userHash.get(Session.KEY_NAME);
        String email = userHash.get(Session.KEY_EMAIL);
        mProfilePictureIV = (ImageView) findViewById(R.id.iv_profile_picture);
        getProfilePicture();

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
        if (user != null) {
            if (id == R.id.nav_dash || id == R.id.bottom_nav_dash) {
                onBackPressed();
            } else {
                if (id == R.id.nav_events) {
                    Intent intent = new Intent(this, EventsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_passport) {
                    Intent intent = new Intent(this, PassportActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_prizes) {
                    Intent intent = new Intent(this, PrizesActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_leaderboard) {
                    Intent intent = new Intent(this, LeaderboardActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_resources) {
                    Intent intent = new Intent(this, ResourcesActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_aboutus) {
                    Intent intent = new Intent(this, AboutUsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_settings) {
                    Intent intent = new Intent(this, SettingsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_logout) {
                    session.logoutUser();
                }
            }

            //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
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
