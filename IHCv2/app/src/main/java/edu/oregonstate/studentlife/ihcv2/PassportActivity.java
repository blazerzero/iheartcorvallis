package edu.oregonstate.studentlife.ihcv2;

/**
 * Created by Omeed on 12/20/17.
 */
import android.content.Intent;
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

public class PassportActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView mPassportRecyclerView;
    private PassportAdapter mPassportAdapter;

    private Event[] completedEventList = {
            new Event("January", "20", "2018", "5:00PM", "OSU Men's Basketball vs. USC", "Gill Coliseum", ""),
            new Event("January", "20", "2018", "7:00PM", "Blazers vs. Dallas", "Moda Center", "")
    };

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
}