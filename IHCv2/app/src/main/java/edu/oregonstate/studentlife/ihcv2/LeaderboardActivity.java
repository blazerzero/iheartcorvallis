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
import android.content.ContentProvider;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class LeaderboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView mLeaderboardRecyclerView;
    private LeaderboardAdapter mLeaderboardAdapter;

    private ArrayList<User> userList;
    SessionActivity session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
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

        userList = new ArrayList<User>();

        mLeaderboardRecyclerView = (RecyclerView) findViewById(R.id.rv_leaderboard_list);
        mLeaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLeaderboardRecyclerView.setHasFixedSize(true);

        mLeaderboardAdapter = new LeaderboardAdapter();
        mLeaderboardRecyclerView.setAdapter(mLeaderboardAdapter);

        new StampCountReceiver(this).execute();

        /*for (User user : userList) {
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
            Intent intent = new Intent(LeaderboardActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(LeaderboardActivity.this, EventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_passport) {
            Intent intent = new Intent(LeaderboardActivity.this, PassportActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_prizes) {
            Intent intent = new Intent(LeaderboardActivity.this, PrizesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_leaderboard) {
            onBackPressed();
        } else if (id == R.id.nav_resources) {
            Intent intent = new Intent(LeaderboardActivity.this, ResourcesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(LeaderboardActivity.this, AboutUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(LeaderboardActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onBackgroundTaskDataObtained(String result) {
        StringTokenizer stFeed = new StringTokenizer(result, ";");
        while (stFeed.hasMoreTokens()) {
            String[] eventTokens = new String[3];
            String eventJSON = stFeed.nextToken();
            StringTokenizer stEvent = new StringTokenizer(eventJSON, "\\");
            for (int i = 0; stEvent.hasMoreTokens(); i++) {
                eventTokens[i] = stEvent.nextToken();
            }
            String userFirstName = eventTokens[0];
            String userLastName = eventTokens[1];
            String userStampCount = eventTokens[2];

            User retrievedUser = new User(userFirstName, userLastName, " ", " ", userStampCount);
            userList.add(retrievedUser);
        }
        sortLeaderboard();

        for (User user : userList) {
            mLeaderboardAdapter.addUserToLeaderboard(user);
        }
    }

    public void sortLeaderboard() {
        User holder;
        int indexLeft = 0;
        int indexRight = 1;
        int pointer = 0;

        for (int i = 0; i < userList.size() - 1; i++) {
            while (indexLeft >= 0 && Integer.parseInt(userList.get(indexLeft).getStampCount()) < Integer.parseInt(userList.get(indexRight).getStampCount())) {
                holder = userList.get(indexLeft);
                userList.set(indexLeft, userList.get(indexRight));
                userList.set(indexRight, holder);
                indexLeft--;
                indexRight--;
            }
            pointer++;
            indexLeft = pointer;
            indexRight = pointer + 1;
        }
    }

    class StampCountReceiver extends AsyncTask {

        private Context context;
        final static String IHC_GET_STAMPCOUNT_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/getusers_stampcount.php";

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
    }
}