package edu.oregonstate.studentlife.ihcv2;

/**
 * Created by Omeed on 12/25/17.
 */

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.*;
import java.util.HashMap;

public class PrizesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SessionActivity session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prizes);
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

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.goldPrizeList);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.silverPrizeList);
        LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.bronzePrizeList);
        for (int i = 1; i <= 3; i++) {
            TextView textView = new TextView(this);
            textView.setText("Gold Prize #" + i);
            textView.setTextSize(getResources().getDimension(R.dimen.prize_listing));
            textView.setTextColor(Color.BLACK);
            linearLayout1.addView(textView);
        }
        for (int i = 1; i <= 3; i++) {
            TextView textView = new TextView(this);
            textView.setText("Silver Prize #" + i);
            textView.setTextSize(getResources().getDimension(R.dimen.prize_listing));
            textView.setTextColor(Color.BLACK);
            linearLayout2.addView(textView);
        }
        for (int i = 1; i <= 3; i++) {
            TextView textView = new TextView(this);
            textView.setText("Bronze Prize #" + i);
            textView.setTextSize(getResources().getDimension(R.dimen.prize_listing));
            textView.setTextColor(Color.BLACK);
            linearLayout3.addView(textView);
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
            Intent intent = new Intent(PrizesActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(PrizesActivity.this, EventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_passport) {
            Intent intent = new Intent(PrizesActivity.this, PassportActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_prizes) {
            onBackPressed();
        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(PrizesActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_resources) {
            Intent intent = new Intent(PrizesActivity.this, ResourcesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(PrizesActivity.this, AboutUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(PrizesActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}