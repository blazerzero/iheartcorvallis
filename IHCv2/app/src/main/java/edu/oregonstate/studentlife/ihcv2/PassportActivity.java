package edu.oregonstate.studentlife.ihcv2;

/**
 * Created by Omeed on 12/20/17.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.adapters.PassportAdapter;
import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.PassportLoader;
import edu.oregonstate.studentlife.ihcv2.loaders.UserInfoLoader;

public class PassportActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = PassportActivity.class.getSimpleName();
    private final static int IHC_GETUSERINFO_ID = 0;
    private final static int IHC_GETCOMPLETEDEVENTS_ID = 1;
    private final static String IHC_USER_EMAIL_KEY = "ihcUserEmail";
    private boolean gotUser;
    private User currentUser;
    private int numStamps;

    private TextView progIndicatorTV;
    private RecyclerView mPassportRecyclerView;
    private PassportAdapter mPassportAdapter;
    private String[] monthShortNames = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."};

    private ArrayList<Event> completedEventList;
    private String email;
    SessionActivity session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(0,0);

        completedEventList = new ArrayList<Event>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progIndicatorTV = (TextView) findViewById(R.id.tv_current_user_stamp_count);

        mPassportRecyclerView = (RecyclerView) findViewById(R.id.rv_passport_list);
        mPassportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPassportRecyclerView.setHasFixedSize(true);

        mPassportAdapter = new PassportAdapter();
        mPassportRecyclerView.setAdapter(mPassportAdapter);

        session = new SessionActivity(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionActivity.KEY_EMAIL);
        Bundle args = new Bundle();
        args.putString(IHC_USER_EMAIL_KEY, email);
        //new CompletedEventReceiver(this).execute(email);
        getSupportLoaderManager().initLoader(IHC_GETCOMPLETEDEVENTS_ID, args, this);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);

        // session information is retrieved and displayed on nav menu
        session = new SessionActivity(getApplicationContext());
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
            Intent intent = new Intent(PassportActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(PassportActivity.this, EventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_passport) {
            onBackPressed();
        } else if (id == R.id.nav_prizes) {
            Intent intent = new Intent(PassportActivity.this, PrizesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(PassportActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_resources) {
            Intent intent = new Intent(PassportActivity.this, ResourcesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(PassportActivity.this, AboutUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(PassportActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        if (args != null) {
            email = args.getString(IHC_USER_EMAIL_KEY);
        }
        if (id == IHC_GETCOMPLETEDEVENTS_ID) {
            return new PassportLoader(this, email);
        }
        else if (id == IHC_GETUSERINFO_ID) {
            return new UserInfoLoader(this, email);
        }
        else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (!gotUser) {
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
                        getSupportLoaderManager().initLoader(IHC_GETUSERINFO_ID, null, this);
                    } else {
                        showNoInternetConnectionMsg();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
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
                    progIndicatorTV.setText("STAMPS: " + String.valueOf(numStamps));
                    if (numStamps >= getResources().getInteger(R.integer.bronzeThreshold)
                            && numStamps < getResources().getInteger(R.integer.silverThreshold)) {
                        progIndicatorTV.setBackgroundColor(getResources().getColor(R.color.eventBronze));
                    }
                    else if (numStamps >= getResources().getInteger(R.integer.silverThreshold)
                            && numStamps < getResources().getInteger(R.integer.goldThreshold)) {
                        progIndicatorTV.setBackgroundColor(getResources().getColor(R.color.eventSilver));
                    }
                    else if (numStamps >= getResources().getInteger(R.integer.goldThreshold)) {
                        progIndicatorTV.setBackgroundColor(getResources().getColor(R.color.eventGold));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                }
                else {
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
}