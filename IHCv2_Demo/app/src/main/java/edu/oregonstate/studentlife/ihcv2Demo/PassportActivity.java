package edu.oregonstate.studentlife.ihcv2Demo;

/**
 * Created by Omeed on 12/20/17.
 */
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import edu.oregonstate.studentlife.ihcv2Demo.adapters.PassportAdapter;
import edu.oregonstate.studentlife.ihcv2Demo.data.Constants;
import edu.oregonstate.studentlife.ihcv2Demo.data.Event;
import edu.oregonstate.studentlife.ihcv2Demo.data.IHCDBContract;
import edu.oregonstate.studentlife.ihcv2Demo.data.IHCDBHelper;
import edu.oregonstate.studentlife.ihcv2Demo.data.Session;
import edu.oregonstate.studentlife.ihcv2Demo.data.User;

public class PassportActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private final static String TAG = PassportActivity.class.getSimpleName();
    //private final static int IHC_GETUSERINFO_ID = 0;
    private final static int IHC_GETCOMPLETEDEVENTS_ID = 0;
    private final static String IHC_USER_EMAIL_KEY = "ihcUserEmail";
    private boolean gotUser = false;
    private int numStamps;

    private ImageView mProfilePictureIV;

    private TextView progIndicatorTV;
    private RecyclerView mPassportRecyclerView;
    private PassportAdapter mPassportAdapter;

    private ArrayList<Event> completedEventList;
    private String email;
    private int userid;
    Session session;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        IHCDBHelper dbHelper = new IHCDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        overridePendingTransition(0,0);

        completedEventList = new ArrayList<Event>();
        try {
            completedEventList.add(
                    new Event(1, "Impact", "College of Public Health and Human Sciences",
                            "College of Public Health and Human Sciences", "160 SW 26th St, Corvallis, OR 97331",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("1900-01-01 00:00:00"),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("2099-12-31 23:59:59"),
                            "12:00 AM", "11:59 PM", "1", "1", "1900",
                            "12", "31", "2099", getResources().getString(R.string.hc_impact_event_description),
                            "https://health.oregonstate.edu/impact", "", "", 1234)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

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

                if (id == R.id.bottom_nav_passport) {
                    // Do nothing, you're already here
                } else {
                    if (id == R.id.bottom_nav_dash) {
                        Intent intent = new Intent(PassportActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_events) {
                        Intent intent = new Intent(PassportActivity.this, EventsActivity.class);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_resources) {
                        Intent intent = new Intent(PassportActivity.this, ResourcesActivity.class);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_aboutus) {
                        Intent intent = new Intent(PassportActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        progIndicatorTV = (TextView) findViewById(R.id.tv_current_user_stamp_count);

        progIndicatorTV.setText("STAMPS: 1");

        mPassportRecyclerView = (RecyclerView) findViewById(R.id.rv_passport_list);
        mPassportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPassportRecyclerView.setHasFixedSize(true);

        mPassportAdapter = new PassportAdapter();
        mPassportRecyclerView.setAdapter(mPassportAdapter);

        for (Event event : completedEventList) {
            mPassportAdapter.addEventToPassport(event);
        }

        session = new Session(getApplicationContext());
        //HashMap<String, String> user = session.getUserDetails();
        //email = user.get(Session.KEY_EMAIL);
        //Bundle args = new Bundle();
        //args.putString(IHC_USER_EMAIL_KEY, email);
        //new CompletedEventReceiver(this).execute(email);
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
        session = new Session(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(Session.KEY_NAME);
        String email = user.get(Session.KEY_EMAIL);
        mProfilePictureIV = (ImageView) findViewById(R.id.iv_profile_picture);
        getProfilePicture();

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

        if (id == R.id.nav_passport) {
            onBackPressed();
        } else {
            if (id == R.id.nav_dash) {
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_events) {
                Intent intent = new Intent(this, EventsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_prizes) {
                Intent intent = new Intent(this, PrizesActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_leaderboard) {
                Intent intent = new Intent(this, LeaderboardActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_resources) {
                Intent intent = new Intent(this, ResourcesActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_aboutus) {
                Intent intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                session.logoutUser();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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