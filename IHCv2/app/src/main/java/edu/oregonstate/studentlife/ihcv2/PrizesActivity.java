package edu.oregonstate.studentlife.ihcv2;

/**
 * Created by Omeed on 12/25/17.
 */

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.adapters.PrizeAdapter;
import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.Prize;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.PrizeLoader;

public class PrizesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PrizeAdapter.OnPrizeClickListener,
        LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = PrizesActivity.class.getSimpleName();

    private final static int IHC_GETPRIZES_ID = 0;

    SessionActivity session;

    private RecyclerView goldPrizeRV;
    private RecyclerView silverPrizeRV;
    private RecyclerView bronzePrizeRV;

    private PrizeAdapter mGoldPrizeAdapter;
    private PrizeAdapter mSilverPrizeAdapter;
    private PrizeAdapter mBronzePrizeAdapter;

    private ArrayList<Prize> prizeList;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prizes);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        overridePendingTransition(0,0);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_USER)) {
            user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            Log.d(TAG, "User ID: " + user.getId());
        }

        prizeList = new ArrayList<Prize>();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bottom_nav_dash) {
                    Intent intent = new Intent(PrizesActivity.this, DashboardActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);                }
                else if (id == R.id.bottom_nav_events) {
                    Intent intent = new Intent(PrizesActivity.this, EventsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_resources) {
                    Intent intent = new Intent(PrizesActivity.this, ResourcesActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_aboutus) {
                    Intent intent = new Intent(PrizesActivity.this, AboutUsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                return false;
            }
        });

        goldPrizeRV = (RecyclerView) findViewById(R.id.rv_gold_prizes);
        silverPrizeRV = (RecyclerView) findViewById(R.id.rv_silver_prizes);
        bronzePrizeRV = (RecyclerView) findViewById(R.id.rv_bronze_prizes);

        goldPrizeRV.setLayoutManager(new LinearLayoutManager(this));
        silverPrizeRV.setLayoutManager(new LinearLayoutManager(this));
        bronzePrizeRV.setLayoutManager(new LinearLayoutManager(this));

        mGoldPrizeAdapter = new PrizeAdapter(this);
        mSilverPrizeAdapter = new PrizeAdapter(this);
        mBronzePrizeAdapter = new PrizeAdapter(this);

        goldPrizeRV.setAdapter(mGoldPrizeAdapter);
        silverPrizeRV.setAdapter(mSilverPrizeAdapter);
        bronzePrizeRV.setAdapter(mBronzePrizeAdapter);


        //new PrizeReceiver(this).execute();
        getSupportLoaderManager().initLoader(IHC_GETPRIZES_ID, null, this);
    }

    @Override
    public void onPrizeClick(Prize prize) {
        // ON PRIZE CLICK CODE
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

        if (id == R.id.nav_dash || id == R.id.bottom_nav_dash) {
            Intent intent = new Intent(PrizesActivity.this, DashboardActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_events || id == R.id.bottom_nav_events) {
            Intent intent = new Intent(PrizesActivity.this, EventsActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_passport) {
            Intent intent = new Intent(PrizesActivity.this, PassportActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_prizes) {
            onBackPressed();
        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(PrizesActivity.this, LeaderboardActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_resources || id == R.id.bottom_nav_resources) {
            Intent intent = new Intent(PrizesActivity.this, ResourcesActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus || id == R.id.bottom_nav_aboutus) {
            Intent intent = new Intent(PrizesActivity.this, AboutUsActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(PrizesActivity.this, SettingsActivity.class);
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
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new PrizeLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "got results from loader");
        try {
            StringTokenizer stPrizes = new StringTokenizer(data, "\\");
            while (stPrizes.hasMoreTokens()) {
                String prizesString = stPrizes.nextToken();
                JSONObject prizeJSON = new JSONObject(prizesString);
                String prizeName = prizeJSON.getString("name");
                String prizeLevel = prizeJSON.getString("level");
                Log.d("Prize name: ", prizeName);
                Log.d("Prize level: ", prizeLevel);

                Prize retrievedPrize = new Prize(prizeName, prizeLevel);
                prizeList.add(retrievedPrize);

                if (retrievedPrize.getLevel().equals("gold")) {
                    mGoldPrizeAdapter.addPrize(retrievedPrize);
                }
                else if (retrievedPrize.getLevel().equals("silver")) {
                    mSilverPrizeAdapter.addPrize(retrievedPrize);
                }
                else if (retrievedPrize.getLevel().equals("bronze")) {
                    mBronzePrizeAdapter.addPrize(retrievedPrize);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

}