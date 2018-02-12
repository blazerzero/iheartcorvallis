package edu.oregonstate.studentlife.ihcv2;

/**
 * Created by Omeed on 12/20/17.
 */
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class PassportActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView mPassportRecyclerView;
    private PassportAdapter mPassportAdapter;

    private Event[] completedEventList = {
            new Event("OSU Men's Basketball vs. USC", "Gill Coliseum", "", "5:00 PM", "January", "20", "2018", "", "", "", ""),
            new Event("Blazers vs. Dallas", "Moda Center", "", "7:00 PM", "January", "20", "2018", "", "", "", "")
    };
    SessionActivity session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);
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

        mPassportRecyclerView = (RecyclerView) findViewById(R.id.rv_passport_list);
        mPassportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPassportRecyclerView.setHasFixedSize(true);

        mPassportAdapter = new PassportAdapter();
        mPassportRecyclerView.setAdapter(mPassportAdapter);

        for (Event event : completedEventList) {
            mPassportAdapter.addEventToPassport(event);
        }
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

            //Event retrievedEvent = new Event(eventName, eventLocation, eventTime, eventMonth, eventDay, eventYear, eventDescription);

            //mPassportAdapter.addEventToPassport(retrievedEvent);
        }

    }

    class CompletedEventReceiver extends AsyncTask {

        private Context context;
        final static String IHC_GET_COMPLETED_EVENTS_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/get_completed_events.php";

        public CompletedEventReceiver(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                URL url = new URL(IHC_GET_COMPLETED_EVENTS_URL);

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
            PassportActivity.this.onBackgroundTaskDataObtained(resultString);
        }
    }
}