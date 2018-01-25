package edu.oregonstate.studentlife.ihcv2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Calendar;
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

    /*private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private int currentViewMode = 0;

    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;*/

    private Event[] eventList = {
            new Event("OSU Men's Basketball vs. USC", "Gill Coliseum", "5:00pm","January", "20", "2018"),
            new Event("Blazers vs. Dallas", "Moda Center", "7:00pm", "January", "20", "2018"),
            new Event("Blazers vs. Minnesota", "Moda Center", "7:00pm", "January", "24", "2018"),
            new Event("OSU Men's Basketball @ Oregon", "Matthew Knight Arena", "5:00pm", "January", "27", "2018"),
            new Event("60th Grammy Awards", "Madison Square Garden", "4:30pm","January", "28", "2018"),
            new Event("Blazers vs. Chicago", "Moda Center", "7:00pm", "January", "31", "2018"),
            new Event("OSU Men's Basketball vs. WSU", "Gill Coliseum", "7:30pm", "February", "8", "2018"),
            new Event("OSU Men's Basketball vs. UW", "Gill Coliseum", "7:00pm", "February", "10", "2018"),
    };

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
                Intent intent = new Intent(EventsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        //new EventReceiver(this).execute();

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

        for (Event event : eventList) {
            mEventListAdapter.addEvent(event);
            mEventCardAdapter.addEvent(event);
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

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
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
        StringTokenizer stJSON = new StringTokenizer(result, "**||**");
        while (stJSON.hasMoreTokens()) {
            String eventJSON = stJSON.nextToken();
            Toast.makeText(this, eventJSON, Toast.LENGTH_LONG).show();
            try { Thread.sleep(2000); } catch (Exception e) {}
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
                    sb.append("**||**");
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
}