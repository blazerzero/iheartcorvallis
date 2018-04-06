package edu.oregonstate.studentlife.ihcv2;

/**
 * Created by Omeed on 12/20/17.
 */
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.TextView;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.adapters.LeaderboardAdapter;
import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.Session;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.LeaderboardLoader;

public class LeaderboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = LeaderboardActivity.class.getSimpleName();
    private final static int IHC_PASSPORT_LOADER_ID = 0;

    private RecyclerView mLeaderboardRecyclerView;
    private LeaderboardAdapter mLeaderboardAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ArrayList<User.LeaderboardUser> leaderboardUserList;
    Session session;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
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
                if (id == R.id.bottom_nav_dash) {
                    Intent intent = new Intent(LeaderboardActivity.this, DashboardActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);                }
                else if (id == R.id.bottom_nav_events) {
                    Intent intent = new Intent(LeaderboardActivity.this, EventsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_passport) {
                    Intent intent = new Intent(LeaderboardActivity.this, PassportActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_resources) {
                    Intent intent = new Intent(LeaderboardActivity.this, ResourcesActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                else if (id == R.id.bottom_nav_aboutus) {
                    Intent intent = new Intent(LeaderboardActivity.this, AboutUsActivity.class);
                    intent.putExtra(Constants.EXTRA_USER, user);
                    startActivity(intent);
                }
                return false;
            }
        });

        leaderboardUserList = new ArrayList<User.LeaderboardUser>();

        mLeaderboardRecyclerView = (RecyclerView) findViewById(R.id.rv_leaderboard_list);
        mLeaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLeaderboardRecyclerView.setHasFixedSize(true);

        mLeaderboardAdapter = new LeaderboardAdapter();
        mLeaderboardRecyclerView.setAdapter(mLeaderboardAdapter);

        //new StampCountReceiver(this).execute();

        getSupportLoaderManager().initLoader(IHC_PASSPORT_LOADER_ID, null, this);

        /*for (User user : leaderboardUserList) {
            mLeaderboardAdapter.addUserToLeaderboard(user);
        }*/
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
            Intent intent = new Intent(LeaderboardActivity.this, DashboardActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_events || id == R.id.bottom_nav_events) {
            Intent intent = new Intent(LeaderboardActivity.this, EventsActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_passport) {
            Intent intent = new Intent(LeaderboardActivity.this, PassportActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_prizes) {
            Intent intent = new Intent(LeaderboardActivity.this, PrizesActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_leaderboard) {
            onBackPressed();
        } else if (id == R.id.nav_resources || id == R.id.bottom_nav_resources) {
            Intent intent = new Intent(LeaderboardActivity.this, ResourcesActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus || id == R.id.bottom_nav_aboutus) {
            Intent intent = new Intent(LeaderboardActivity.this, AboutUsActivity.class);
            intent.putExtra(Constants.EXTRA_USER, user);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(LeaderboardActivity.this, SettingsActivity.class);
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

    /*private void onBackgroundTaskDataObtained(String result) {
        try {
            StringTokenizer stUser = new StringTokenizer(result, "\\");
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
    }*/

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

    /*class StampCountReceiver extends AsyncTask {

        private Context context;
        final static String IHC_GET_STAMPCOUNT_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getusers_stampcount.php";

        public StampCountReceiver(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                URL url = new URL(IHC_GET_STAMPCOUNT_URL);

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
            LeaderboardActivity.this.onBackgroundTaskDataObtained(resultString);
        }
    }*/
}