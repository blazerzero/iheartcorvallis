package edu.oregonstate.studentlife.ihcv2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Omeed on 12/20/17.
 */

public class EventsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mEventListRecyclerView;
    private RecyclerView mEventCardRecyclerView;
    private EventListAdapter mEventListAdapter;
    private EventCardAdapter mEventCardAdapter;

    SessionActivity session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(0,0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button mapLink = (Button)findViewById(R.id.mapbtn);
        mapLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    Intent intent = new Intent(EventsActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
                else {
                    showNoInternetConnectionMsg();
                }
            }
        });

        mEventListRecyclerView = (RecyclerView) findViewById(R.id.rv_event_list);
        mEventListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEventListRecyclerView.setHasFixedSize(true);

        mEventListAdapter = new EventListAdapter();
        mEventListRecyclerView.setAdapter(mEventListAdapter);

        mEventCardRecyclerView = (RecyclerView) findViewById(R.id.rv_event_card);
        mEventCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEventCardRecyclerView.setHasFixedSize(true);

        mEventCardAdapter = new EventCardAdapter();
        mEventCardRecyclerView.setAdapter(mEventCardAdapter);

        //eventList = mergeSortEventList(eventList);

        mEventCardRecyclerView.setVisibility(View.GONE);

        if (isNetworkAvailable()) {
            new EventReceiver(this).execute();
        }
        else {
            showNoInternetConnectionMsg();
        }

    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    public Event[] mergeSortEventList(Event[] eventList) {
        int half_len;
        Event[] sortedEventList;
        Event tempEvent;
        switch (eventList.length) {
            case 1: {
                sortedEventList = eventList;
                break;
            }
            default: {
                half_len = eventList.length / 2;
                Event[] lowEventList = Arrays.copyOfRange(eventList, 0, half_len-1);
                Event[] highEventList = Arrays.copyOfRange(eventList, half_len, eventList.length - 1);
                lowEventList = mergeSortEventList(lowEventList);
                highEventList = mergeSortEventList(highEventList);
                sortedEventList = mergeLists(lowEventList, highEventList);
            }
        }
        return sortedEventList;
    }

    public Event[] mergeLists(Event[] lowEventList, Event[] highEventList) {
        Event[] sortedEventList = new Event[lowEventList.length + highEventList.length];
        int i = 0;
        int j = 0;
        int count = 0;
        char[] time1 = new char[7];
        char[] time2 = new char[7];
        while (i < lowEventList.length && j < highEventList.length) {
            if (Integer.parseInt(lowEventList[i].getYear()) == Integer.parseInt(highEventList[j].getYear())) {
                if (monthValue(lowEventList[i].getMonth()) == monthValue(highEventList[j].getMonth())) {
                    if (Integer.parseInt(lowEventList[i].getDay()) == Integer.parseInt(highEventList[j].getDay())) {
                        lowEventList[i].getTime().getChars(lowEventList[i].getTime().length()-2, lowEventList[i].getTime().length(), time1, 0);
                        highEventList[j].getTime().getChars(highEventList[i].getTime().length()-2, highEventList[i].getTime().length(), time2, 0);
                        if (time1.equals("am") && time2.equals("pm")) {
                            sortedEventList[count] = lowEventList[i];
                            i++;
                            count++;
                        }
                        else if (time1.equals("pm") && time2.equals("am")) {
                            sortedEventList[count] = highEventList[j];
                            j++;
                            count++;
                        }
                    }
                    else if (Integer.parseInt(lowEventList[i].getDay()) < Integer.parseInt(highEventList[j].getDay())) {
                        sortedEventList[count] = lowEventList[i];
                        i++;
                        count++;
                    }
                    else {
                        sortedEventList[count] = highEventList[j];
                        j++;
                        count++;
                    }
                }
                else if (monthValue(lowEventList[i].getMonth()) < monthValue(highEventList[j].getMonth())) {
                    sortedEventList[count] = lowEventList[i];
                    i++;
                    count++;
                }
                else {
                    sortedEventList[count] = highEventList[j];
                    j++;
                    count++;
                }
            }
            else if (Integer.parseInt(lowEventList[i].getYear()) < Integer.parseInt(highEventList[j].getYear())) {
                sortedEventList[count] = lowEventList[i];
                i++;
                count++;
            }
            else {
                sortedEventList[count] = highEventList[j];
                j++;
                count++;
            }
        }
        if (i == lowEventList.length) {
            while (j < highEventList.length) {
                sortedEventList[count] = highEventList[j];
                count++;
            }
        }
        else if (j == highEventList.length) {
            while (i < lowEventList.length) {
                sortedEventList[count] = lowEventList[i];
                count++;
            }
        }
        return sortedEventList;
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
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        if (mEventListRecyclerView.getVisibility() == View.VISIBLE && mEventCardRecyclerView.getVisibility() == View.GONE) {
            mEventListRecyclerView.setVisibility(View.GONE);
            mEventCardRecyclerView.setVisibility(View.VISIBLE);
        }
        else if (mEventListRecyclerView.getVisibility() == View.GONE && mEventCardRecyclerView.getVisibility() == View.VISIBLE) {
            mEventCardRecyclerView.setVisibility(View.GONE);
            mEventListRecyclerView.setVisibility(View.VISIBLE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setTitle(R.string.action_switch_view);

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
            Intent intent = new Intent(EventsActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_events) {
            onBackPressed();
        } else if (id == R.id.nav_passport) {
            Intent intent = new Intent(EventsActivity.this, PassportActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_prizes) {
            Intent intent = new Intent(EventsActivity.this, PrizesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(EventsActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_resources) {
            Intent intent = new Intent(EventsActivity.this, ResourcesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(EventsActivity.this, AboutUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(EventsActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onBackgroundTaskDataObtained(String result) {
        StringTokenizer stFeed = new StringTokenizer(result, ";");
        while (stFeed.hasMoreTokens()) {
            String[] eventTokens = new String[5];
            String eventJSON = stFeed.nextToken();
            StringTokenizer stEvent = new StringTokenizer(eventJSON, "\\");
            for (int i = 0; stEvent.hasMoreTokens(); i++) {
                eventTokens[i] = stEvent.nextToken();
            }
            String eventName = eventTokens[0];
            String eventLocation = eventTokens[1];
            String eventDateAndTime = eventTokens[2];
            String eventDescription = eventTokens[3];
            /*InputStream eventImage = null;
            try {
                eventImage = new ByteArrayInputStream(eventTokens[4].getBytes(StandardCharsets.UTF_8.name()));
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            StringTokenizer dateTimeTokenizer = new StringTokenizer(eventDateAndTime);
            String eventYear = dateTimeTokenizer.nextToken("-");
            String eventMonth = dateTimeTokenizer.nextToken("-");
            String eventDay = dateTimeTokenizer.nextToken(" ");
            String eventTime = dateTimeTokenizer.nextToken();

            try {
                SimpleDateFormat _24HourFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourFormat = new SimpleDateFormat("hh:mm a");
                Date _24HourEventTime = _24HourFormat.parse(eventTime);
                eventTime = _12HourFormat.format(_24HourEventTime).toString();
                if (eventTime.charAt(0) == '0') {
                    eventTime = eventTime.substring(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            eventDay = eventDay.substring(1);
            if (eventDay.charAt(0) == '0') {
                eventDay = eventDay.substring(1);
            }

            Event retrievedEvent = new Event(eventName, eventLocation, eventTime, eventMonth, eventDay, eventYear, eventDescription);

            if (isNetworkAvailable()) {
                mEventListAdapter.addEvent(retrievedEvent);
                mEventCardAdapter.addEvent(retrievedEvent);
            }
            else {
                showNoInternetConnectionMsg();
            }
        }

    }

    class EventReceiver extends AsyncTask {

        private Context context;
        final static String IHC_GET_EVENTS_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/getevents.php";

        public EventReceiver(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                URL url = new URL(IHC_GET_EVENTS_URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            } catch (Exception e) { return new String("Exception: " + e.getMessage()); }
        }


        @Override
        protected void onPostExecute(Object result) {
            String resultString = (String) result;
            EventsActivity.this.onBackgroundTaskDataObtained(resultString);
        }
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