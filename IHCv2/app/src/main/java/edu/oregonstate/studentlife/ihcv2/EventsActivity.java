package edu.oregonstate.studentlife.ihcv2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Omeed on 12/20/17.
 */

public class EventsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mEventListRecyclerView;
    private EventAdapter mEventAdapter;

    private Event[] eventList = {
            new Event("January", "20", "2018", "5:00PM", "OSU Men's Basketball vs. USC", "Gill Coliseum"),
            new Event("January", "20", "2018","7:00PM", "Blazers vs. Dallas", "Moda Center"),
            new Event("January", "24", "2018", "7:00PM", "Blazers vs. Minnesota", "Moda Center"),
            new Event("January", "27", "2018", "5:00PM", "OSU Men's Basketball @ Oregon", "Matthew Knight Arena"),
            new Event("January", "28", "2018", "4:30PM", "60th Grammy Awards", "Madison Square Garden"),
            new Event("January", "31", "2018", "7:00PM", "Blazers vs. Chicago", "Moda Center"),
            new Event("February", "8", "2018", "7:30PM", "OSU Men's Basketball vs. WSU", "Gill Coliseum"),
            new Event("February", "10", "2018", "7:00PM", "OSU Men's Basketball vs. UW", "Gill Coliseum"),
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

        mEventListRecyclerView = (RecyclerView) findViewById(R.id.rv_event_list);
        mEventListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEventListRecyclerView.setHasFixedSize(true);

        mEventAdapter = new EventAdapter();
        mEventListRecyclerView.setAdapter(mEventAdapter);

        //eventList = mergeSortEventList(eventList);

        for (Event event : eventList) {
            mEventAdapter.addEvent(event);
        }

    }

    public Event[] mergeSortEventList(Event[] eventList) {
        int half_len;
        Event[] sortedEventList;
        switch (eventList.length) {
            case 1: {
                sortedEventList = eventList;
                break;
            }
            default: {
                half_len = eventList.length / 2;
                Event[] lowEventList = Arrays.copyOfRange(eventList, 0, half_len - 1);
                Event[] highEventList = Arrays.copyOfRange(eventList, half_len, eventList.length - 1);
                lowEventList = mergeSortEventList(lowEventList);
                highEventList = mergeSortEventList(highEventList);
                sortedEventList = mergeLists(lowEventList, highEventList);
;            }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}