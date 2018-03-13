package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.content.DialogInterface;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.adapters.EventListAdapter;
import edu.oregonstate.studentlife.ihcv2.adapters.PassportAdapter;
import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.EventLoader;
import edu.oregonstate.studentlife.ihcv2.loaders.PassportLoader;
import edu.oregonstate.studentlife.ihcv2.loaders.UserInfoLoader;

import static edu.oregonstate.studentlife.ihcv2.EventsActivity.EXTRA_EVENT;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EventListAdapter.OnEventClickListener,
        LoaderManager.LoaderCallbacks<String> {

    private User currentUser;
    private String email;
    private int numStamps;
    public TextView progIndicator;
    public static final String EXTRA_USER = "User";
    private final static String IHC_USER_EMAIL_KEY = "IHC_USER_EMAIL";
    private final static int IHC_USER_LOADER_ID = 0;
    private final static int IHC_PASSPORT_LOADER_ID = 1;
    private final static int IHC_EVENT_LOADER_ID = 2;

    private TextView dashStampCountTV;

    private String[] monthShortNames = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."};
    private ArrayList<Event> completedEventList;
    private PassportAdapter mPassportAdapter;

    private ArrayList<Event> eventList;
    private EventListAdapter mEventListAdapter;
    private RecyclerView mEventListRecyclerView;

    private RecyclerView mPassportRecyclerView;


    boolean gotUser = false;
    boolean gotPassport = false;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final String TAG = DashboardActivity.class.getSimpleName();


    SessionActivity session;
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

        session = new SessionActivity(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionActivity.KEY_EMAIL);

        eventList = new ArrayList<Event>();

        //new UserInfoReceiver(this).execute(email);

        getSupportLoaderManager().initLoader(IHC_USER_LOADER_ID, null, this);
        getUserInfo();

        dashStampCountTV = (TextView)findViewById(R.id.tv_dash_stamp_count);
        progIndicator = (TextView)findViewById(R.id.progIndicator);

        progIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PrizesActivity.class);
                intent.putExtra(DashboardActivity.EXTRA_USER, currentUser);
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
<<<<<<< HEAD
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {
        if (!gotUser && !gotPassport) {
            if (data != null) {
                try {
                    JSONObject userJSON = new JSONObject(data);
                    String firstname = userJSON.getString("firstname");
                    String lastname = userJSON.getString("lastname");
                    String email = userJSON.getString("email");
                    int id = Integer.parseInt(userJSON.getString("id"));
                    String stampcount = userJSON.getString("stampcount");
                    currentUser = new User(firstname, lastname, email, id, stampcount);
                    numStamps = Integer.parseInt(stampcount);
                    progIndicator = initProgIndicator(numStamps, progIndicator);

                    dashStampCountTV.setText("STAMPS: " + String.valueOf(numStamps));

                    gotUser = true;
                    getSupportLoaderManager().initLoader(IHC_PASSPORT_LOADER_ID, null,this);

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
        else if (gotUser && !gotPassport) {
            // passport stuff
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
                    String eventDateAndTime = eventJSON.getString("dateandtime");
                    String eventDescription = eventJSON.getString("description");
                    String eventLink1 = eventJSON.getString("link1");
                    String eventLink2 = eventJSON.getString("link2");
                    String eventLink3 = eventJSON.getString("link3");
                    int eventPin = Integer.parseInt(eventJSON.getString("pin"));

                    StringTokenizer dateTimeTokenizer = new StringTokenizer(eventDateAndTime);
                    String eventYear = dateTimeTokenizer.nextToken("-");
                    String eventMonth = dateTimeTokenizer.nextToken("-");
                    String eventDay = dateTimeTokenizer.nextToken(" ");
                    String eventTime = dateTimeTokenizer.nextToken();

                    SimpleDateFormat sdfEvent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date eventDate = sdfEvent.parse(eventDateAndTime);

                    if (eventTime.charAt(0) == '0') {
                        eventTime = eventTime.substring(1);
                    }

                    eventDay = eventDay.substring(1);
                    if (eventDay.charAt(0) == '0') {
                        eventDay = eventDay.substring(1);
                    }

                    if (eventMonth.charAt(0) == '0') {
                        eventMonth = eventMonth.substring(1);
                    }

                    int monthInt = Integer.parseInt(eventMonth);
                    eventMonth = monthShortNames[monthInt - 1];

                    Event retrievedEvent = new Event(eventid, eventName, eventLocation, eventAddress,
                            eventDate, eventTime, eventMonth, eventDay, eventYear,
                            eventDescription, eventLink1, eventLink2, eventLink3, eventPin);

                    completedEventList.add(retrievedEvent);

                    if (isNetworkAvailable()) {
                        mPassportAdapter.addEventToPassport(retrievedEvent);
                        gotUser = true;
                        getSupportLoaderManager().initLoader(IHC_EVENT_LOADER_ID, null, this);
                    } else {
                        showNoInternetConnectionMsg();
                    }
                }
=======
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data != null) {
            try {
                JSONObject userJSON = new JSONObject(data);
                String firstname = userJSON.getString("firstname");
                String lastname = userJSON.getString("lastname");
                String email = userJSON.getString("email");
                int id = Integer.parseInt(userJSON.getString("id"));
                String stampcount = userJSON.getString("stampcount");
                int grade = Integer.parseInt(userJSON.getString("grade"));
                int age = Integer.parseInt(userJSON.getString("age"));
                currentUser = new User(firstname, lastname, email, id, stampcount, grade, age);
                numStamps = Integer.parseInt(stampcount);
                progIndicator = initProgIndicator(numStamps, progIndicator);
>>>>>>> master
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            // event stuff
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
                    String eventDateAndTime = eventJSON.getString("dateandtime");
                    String eventDescription = eventJSON.getString("description");
                    String eventLink1 = eventJSON.getString("link1");
                    String eventLink2 = eventJSON.getString("link2");
                    String eventLink3 = eventJSON.getString("link3");
                    int eventPin = Integer.parseInt(eventJSON.getString("pin"));

                    StringTokenizer dateTimeTokenizer = new StringTokenizer(eventDateAndTime);
                    String eventYear = dateTimeTokenizer.nextToken("-");
                    String eventMonth = dateTimeTokenizer.nextToken("-");
                    String eventDay = dateTimeTokenizer.nextToken(" ");
                    String eventTime = dateTimeTokenizer.nextToken();

                    SimpleDateFormat sdfEvent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date eventDate = sdfEvent.parse(eventDateAndTime);

                    SimpleDateFormat _24HourFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat _12HourFormat = new SimpleDateFormat("hh:mm a");
                    Date _24HourEventTime = _24HourFormat.parse(eventTime);
                    eventTime = _12HourFormat.format(_24HourEventTime).toString();
                    if (eventTime.charAt(0) == '0') {
                        eventTime = eventTime.substring(1);
                    }

                    eventDay = eventDay.substring(1);
                    if (eventDay.charAt(0) == '0') {
                        eventDay = eventDay.substring(1);
                    }

                    Event retrievedEvent = new Event(eventid, eventName, eventLocation, eventAddress,
                            eventDate, eventTime, eventMonth, eventDay, eventYear,
                            eventDescription, eventLink1, eventLink2, eventLink3, eventPin);

                    eventList.add(retrievedEvent);

                }

                sortEventsByDate();

                for (Event event : eventList) {
                    if (isNetworkAvailable()) {
                        mEventListAdapter.addEvent(event);
                        //mEventCardAdapter.addEvent(event);
                    } else {
                        showNoInternetConnectionMsg();
                    }
                }
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

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    public int compareDates(Event eLeft, Event eRight) {
        return eLeft.getDateTime().compareTo(eRight.getDateTime());
    }

    public void getUserInfo() {
        Bundle args = new Bundle();
        args.putString(IHC_USER_EMAIL_KEY, email);
        //Toast.makeText(this, email, Toast.LENGTH_LONG).show();
        getSupportLoaderManager().restartLoader(IHC_USER_LOADER_ID, args, this);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    public TextView initProgIndicator(int numStamps, TextView progIndicator) {
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
        progIndicator.setBackgroundColor(progColor);
        progIndicator.setText(message);
        progIndicator.setTextColor(getResources().getColor(R.color.maroon));
        return progIndicator;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dash) {
            onBackPressed();
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_passport) {
            Intent intent = new Intent(DashboardActivity.this, PassportActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_prizes) {
            Intent intent = new Intent(DashboardActivity.this, PrizesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(DashboardActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_resources) {
            Intent intent = new Intent(DashboardActivity.this, ResourcesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(DashboardActivity.this, AboutUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    // possibly change to just navigate to the events page
    @Override
    public void onEventClick(Event event) {
        Intent eventDetailActivityIntent = new Intent(this, EventDetailActivity.class);
        eventDetailActivityIntent.putExtra(EXTRA_EVENT, event);
        eventDetailActivityIntent.putExtra(EXTRA_USER, currentUser);
        startActivity(eventDetailActivityIntent);
    }

    /*private void onBackgroundTaskDataObtained(String result) {
        if (result != null) {
            try {
                JSONObject userJSON = new JSONObject(result);
                String firstname = userJSON.getString("firstname");
                String lastname = userJSON.getString("lastname");
                String email = userJSON.getString("email");
                String id = userJSON.getString("id");
                String stampcount = userJSON.getString("stampcount");
                currentUser = new User(firstname, lastname, email, id, stampcount);
                numStamps = Integer.parseInt(stampcount);
                progIndicator = initProgIndicator(numStamps, progIndicator);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            android.app.AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }
            else {
                builder = new android.app.AlertDialog.Builder(this);
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

    class UserInfoReceiver extends AsyncTask {

        private Context context;
        private String email;
        final static String IHC_GETUSERINFO_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getuserinfo.php";

        public UserInfoReceiver(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {}

        @Override
        protected Object doInBackground(Object[] objects) {
            email = (String) objects[0];

            try {
                URL url = new URL(IHC_GETUSERINFO_URL);
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            } catch (Exception e) { e.printStackTrace(); return null; }
        }


        @Override
        protected void onPostExecute(Object result) {
            String resultString = (String) result;
            DashboardActivity.this.onBackgroundTaskDataObtained(resultString);
        }
    }*/
}
