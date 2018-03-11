package edu.oregonstate.studentlife.ihcv2;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.content.DialogInterface;

import org.json.JSONObject;

import java.util.HashMap;

import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.UserInfoLoader;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<String> {

    private User currentUser;
    private String email;
    private int numStamps;
    public TextView progIndicator;
    public static final String EXTRA_USER = "User";
    private final static String IHC_USER_EMAIL_KEY = "IHC_USER_EMAIL";
    private final static int IHC_USER_LOADER_ID = 0;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final String TAG = DashboardActivity.class.getSimpleName();


    SessionActivity session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        overridePendingTransition(0,0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        session = new SessionActivity(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionActivity.KEY_EMAIL);

        //new UserInfoReceiver(this).execute(email);

        getSupportLoaderManager().initLoader(IHC_USER_LOADER_ID, null, this);
        getUserInfo();

        progIndicator = (TextView)findViewById(R.id.progIndicator);

        progIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PrizesActivity.class);
                intent.putExtra(DashboardActivity.EXTRA_USER, currentUser);
                startActivity(intent);
            }
        });

        Button button1 = (Button)findViewById(R.id.eventbtn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
                intent.putExtra(DashboardActivity.EXTRA_USER, currentUser);
                startActivity(intent);
            }
        });


        Button button2 = (Button)findViewById(R.id.passportbtn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PassportActivity.class);
                intent.putExtra(DashboardActivity.EXTRA_USER, currentUser);
                startActivity(intent);
            }
        });

        Button button3 = (Button)findViewById(R.id.leaderbtn);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, LeaderboardActivity.class);
                intent.putExtra(DashboardActivity.EXTRA_USER, currentUser);
                startActivity(intent);
            }
        });

        Button button4 = (Button)findViewById(R.id.resourcebtn);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ResourcesActivity.class);
                intent.putExtra(DashboardActivity.EXTRA_USER, currentUser);
                startActivity(intent);
            }
        });

        Button button5 = (Button)findViewById(R.id.aboutbtn);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AboutUsActivity.class);
                intent.putExtra(DashboardActivity.EXTRA_USER, currentUser);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        if (args != null) {
            email = args.getString(IHC_USER_EMAIL_KEY);
        }
        return new UserInfoLoader(this, email);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data != null) {
            try {
                JSONObject userJSON = new JSONObject(data);
                String firstname = userJSON.getString("firstname");
                String lastname = userJSON.getString("lastname");
                String email = userJSON.getString("email");
                int id = Integer.parseInt(userJSON.getString("id"));
                String stampcount = userJSON.getString("stampcount");
                int grade = Integer.parseInt(userJSON.getString("grade"));
                int age = Integer.parseInt(userJSON.getString("age"));
                currentUser = new User(firstname, lastname, email, id, stampcount, grade, age);
                numStamps = Integer.parseInt(stampcount);
                progIndicator = initProgIndicator(numStamps, progIndicator);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }
            else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Error");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Close alert dialog
                }
            });
            builder.setMessage("Error retrieving user info.");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    public void getUserInfo() {
        Bundle args = new Bundle();
        args.putString(IHC_USER_EMAIL_KEY, email);
        //Toast.makeText(this, email, Toast.LENGTH_LONG).show();
        getSupportLoaderManager().restartLoader(IHC_USER_LOADER_ID, args, this);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    public TextView initProgIndicator(int numStamps, TextView progIndicator) {
        String message;
        int eventsToGo;
        int progColor;
        if (numStamps >= getResources().getInteger(R.integer.bronzeThreshold)
                && numStamps < getResources().getInteger(R.integer.silverThreshold)) {
            progColor = getResources().getColor(R.color.eventBronze);
            eventsToGo = getResources().getInteger(R.integer.silverThreshold) - numStamps;
            if (eventsToGo == 1) {
                message = "Only " + eventsToGo + " event away from reaching silver status!\nCLICK HERE TO VIEW PRIZES";
            }
            else {
                message = "Only " + eventsToGo + " events away from reaching silver status!\nCLICK HERE TO VIEW PRIZES";
            }
        }
        else if (numStamps >= getResources().getInteger(R.integer.silverThreshold)
                && numStamps < getResources().getInteger(R.integer.goldThreshold)) {
            eventsToGo = getResources().getInteger(R.integer.goldThreshold) - numStamps;
            progColor = getResources().getColor(R.color.eventSilver);
            if (eventsToGo == 1) {
                message = "Only " + eventsToGo + " event away from reaching gold status!\nCLICK HERE TO VIEW PRIZES";
            }
            else {
                message = "Only " + eventsToGo + " events away from reaching gold status!\nCLICK HERE TO VIEW PRIZES";
            }
        }
        else if (numStamps >= getResources().getInteger(R.integer.goldThreshold)) {
            progColor = getResources().getColor(R.color.eventGold);
            message = "Congratulations on achieving gold status!\nCLICK HERE TO VIEW PRIZES";
        }
        else {
            eventsToGo = getResources().getInteger(R.integer.bronzeThreshold) - numStamps;
            progColor = getResources().getColor(R.color.colorAccent);
            if (eventsToGo == 1) {
                message = "Only " + eventsToGo + " event away from reaching bronze status!\nCLICK HERE TO VIEW PRIZES";
            }
            else {
                message = "Only " + eventsToGo + " events away from reaching bronze status!\nCLICK HERE TO VIEW PRIZES";
            }
        }
        progIndicator.setBackgroundColor(progColor);
        progIndicator.setText(message);
        progIndicator.setTextColor(getResources().getColor(R.color.maroon));
        return progIndicator;
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
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
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        // session information is retrieved and displayed on nav menu
        //session = new SessionActivity(getApplicationContext());
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
            onBackPressed();
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_passport) {
            Intent intent = new Intent(DashboardActivity.this, PassportActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_prizes) {
            Intent intent = new Intent(DashboardActivity.this, PrizesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(DashboardActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_resources) {
            Intent intent = new Intent(DashboardActivity.this, ResourcesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(DashboardActivity.this, AboutUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*private void onBackgroundTaskDataObtained(String result) {
        if (result != null) {
            try {
                JSONObject userJSON = new JSONObject(result);
                String firstname = userJSON.getString("firstname");
                String lastname = userJSON.getString("lastname");
                String email = userJSON.getString("email");
                String id = userJSON.getString("id");
                String stampcount = userJSON.getString("stampcount");
                currentUser = new User(firstname, lastname, email, id, stampcount);
                numStamps = Integer.parseInt(stampcount);
                progIndicator = initProgIndicator(numStamps, progIndicator);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            android.app.AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }
            else {
                builder = new android.app.AlertDialog.Builder(this);
            }
            builder.setTitle("Error");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Close alert dialog
                }
            });
            builder.setMessage("Error retrieving user info.");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
        }

    }

    class UserInfoReceiver extends AsyncTask {

        private Context context;
        private String email;
        final static String IHC_GETUSERINFO_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/getuserinfo.php";

        public UserInfoReceiver(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {}

        @Override
        protected Object doInBackground(Object[] objects) {
            email = (String) objects[0];

            try {
                URL url = new URL(IHC_GETUSERINFO_URL);
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            } catch (Exception e) { e.printStackTrace(); return null; }
        }


        @Override
        protected void onPostExecute(Object result) {
            String resultString = (String) result;
            DashboardActivity.this.onBackgroundTaskDataObtained(resultString);
        }
    }*/
}
