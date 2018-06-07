package edu.oregonstate.studentlife.ihcv2;

/**
 * Created by Omeed on 12/20/17.
 * This is the Leaderboard page, which acts as a simple leaderboard that displays a list of all of the users
 * that have at least 1 stamp sorted by their stamp count.
 *
 * Uses the LeaderboardLoader to get every user's stampcount, firstname, and lastname from database
 *
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
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.adapters.LeaderboardAdapter;
import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBContract;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBHelper;
import edu.oregonstate.studentlife.ihcv2.data.Session;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.LeaderboardLoader;

public class LeaderboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = LeaderboardActivity.class.getSimpleName();
    private final static int IHC_PASSPORT_LOADER_ID = 0;

    private ImageView mProfilePictureIV;

    private RecyclerView mLeaderboardRV;
    private LeaderboardAdapter mLeaderboardAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ArrayList<User.LeaderboardUser> leaderboardUserList;
    Session session;
    private User user;

    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        IHCDBHelper dbHelper = new IHCDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        overridePendingTransition(0,0);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_USER)) {
            user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            Log.d(TAG, "User ID: " + user.getId());
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
                if (user != null) {
                    if (id == R.id.bottom_nav_dash) {
                        Intent intent = new Intent(LeaderboardActivity.this, DashboardActivity.class);
                        intent.putExtra(Constants.EXTRA_USER, user);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_events) {
                        Intent intent = new Intent(LeaderboardActivity.this, EventsActivity.class);
                        intent.putExtra(Constants.EXTRA_USER, user);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_passport) {
                        Intent intent = new Intent(LeaderboardActivity.this, PassportActivity.class);
                        intent.putExtra(Constants.EXTRA_USER, user);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_resources) {
                        Intent intent = new Intent(LeaderboardActivity.this, ResourcesActivity.class);
                        intent.putExtra(Constants.EXTRA_USER, user);
                        startActivity(intent);
                    } else if (id == R.id.bottom_nav_aboutus) {
                        Intent intent = new Intent(LeaderboardActivity.this, AboutUsActivity.class);
                        intent.putExtra(Constants.EXTRA_USER, user);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        leaderboardUserList = new ArrayList<User.LeaderboardUser>();

        mLeaderboardRV = (RecyclerView) findViewById(R.id.rv_leaderboard_list);
        mLeaderboardRV.setLayoutManager(new LinearLayoutManager(this));
        mLeaderboardRV.setHasFixedSize(true);

        mLeaderboardAdapter = new LeaderboardAdapter();
        mLeaderboardRV.setAdapter(mLeaderboardAdapter);

        getSupportLoaderManager().initLoader(IHC_PASSPORT_LOADER_ID, null, this);

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
        HashMap<String, String> userHash = session.getUserDetails();
        String name = userHash.get(Session.KEY_NAME);
        String email = userHash.get(Session.KEY_EMAIL);
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

        if (user != null) {
            if (id == R.id.nav_leaderboard) {
                onBackPressed();
            } else {
                if (id == R.id.nav_dash) {
                    Intent intent = new Intent(this, DashboardActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_events) {
                    Intent intent = new Intent(this, EventsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_passport) {
                    Intent intent = new Intent(this, PassportActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_prizes) {
                    Intent intent = new Intent(this, PrizesActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_resources) {
                    Intent intent = new Intent(this, ResourcesActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_aboutus) {
                    Intent intent = new Intent(this, AboutUsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_settings) {
                    Intent intent = new Intent(this, SettingsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                } else if (id == R.id.nav_logout) {
                    session.logoutUser();
                }
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new LeaderboardLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        try {
            StringTokenizer stUser = new StringTokenizer(data, "\\");
            while (stUser.hasMoreTokens()) {
                String leaderboardUserString = stUser.nextToken();
                JSONObject leaderboardUserJSON = new JSONObject(leaderboardUserString);
                String userFirstName = leaderboardUserJSON.getString("firstname");
                String userLastName = leaderboardUserJSON.getString("lastname");
                String userStampCount = leaderboardUserJSON.getString("stampcount");

                User.LeaderboardUser retrievedUser = new User.LeaderboardUser(userFirstName, userLastName, userStampCount);
                leaderboardUserList.add(retrievedUser);
            }
            sortLeaderboard();

            for (User.LeaderboardUser leaderboardUser : leaderboardUserList) {
                mLeaderboardAdapter.addUserToLeaderboard(leaderboardUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    public void sortLeaderboard() {
        User.LeaderboardUser holder;
        int indexLeft = 0;
        int indexRight = 1;
        int pointer = 0;

        for (int i = 0; i < leaderboardUserList.size() - 1; i++) {
            while (indexLeft >= 0 && Integer.parseInt(leaderboardUserList.get(indexLeft).getStampCount()) < Integer.parseInt(leaderboardUserList.get(indexRight).getStampCount())) {
                holder = leaderboardUserList.get(indexLeft);
                leaderboardUserList.set(indexLeft, leaderboardUserList.get(indexRight));
                leaderboardUserList.set(indexRight, holder);
                indexLeft--;
                indexRight--;
            }
            pointer++;
            indexLeft = pointer;
            indexRight = pointer + 1;
        }
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