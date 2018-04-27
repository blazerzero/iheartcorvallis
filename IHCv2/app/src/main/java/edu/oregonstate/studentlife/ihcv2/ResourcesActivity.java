package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.adapters.ResourceAdapter;
import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBContract;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBHelper;
import edu.oregonstate.studentlife.ihcv2.data.Resource;
import edu.oregonstate.studentlife.ihcv2.data.Session;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.ResourceLoader;

/**
 * Created by Omeed on 12/20/17.
 */

public class ResourcesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ResourceAdapter.OnResourceClickListener,
        LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = ResourcesActivity.class.getSimpleName();

    Session session;

    private ImageView mProfilePictureIV;

    private RecyclerView mResourceRV;
    private ResourceAdapter mResourceAdapter;
    private ArrayList<Resource> resourceList;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private User user;
    private SQLiteDatabase mDB;

    private final static int IHC_RESOURCE_LOADER_ID = 0;

    private Bitmap profilePictureBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
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
                    if (id == R.id.bottom_nav_resources) {
                        // Do nothing, you're already here

                    } else {
                        //recycleBitmap();
                        if (id == R.id.bottom_nav_dash) {
                            Intent intent = new Intent(ResourcesActivity.this, DashboardActivity.class);
                            intent.putExtra(Constants.EXTRA_USER, user);
                            startActivity(intent);
                        } else if (id == R.id.bottom_nav_events) {
                            Intent intent = new Intent(ResourcesActivity.this, EventsActivity.class);
                            intent.putExtra(Constants.EXTRA_USER, user);
                            startActivity(intent);
                        } else if (id == R.id.bottom_nav_passport) {
                            Intent intent = new Intent(ResourcesActivity.this, PassportActivity.class);
                            intent.putExtra(Constants.EXTRA_USER, user);
                            startActivity(intent);
                        } else if (id == R.id.bottom_nav_aboutus) {
                            Intent intent = new Intent(ResourcesActivity.this, AboutUsActivity.class);
                            intent.putExtra(Constants.EXTRA_USER, user);
                            startActivity(intent);
                        }
                    }
                }
                return false;
            }
        });

        resourceList = new ArrayList<Resource>();

        mResourceRV = (RecyclerView) findViewById(R.id.rv_resource_list);
        mResourceRV.setLayoutManager(new LinearLayoutManager(this));
        mResourceRV.setHasFixedSize(true);

        mResourceAdapter = new ResourceAdapter(this);
        mResourceRV.setAdapter(mResourceAdapter);

        TextView resourceMapBtn = (TextView)findViewById(R.id.mapbtn2);
        resourceMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    Intent intent = new Intent(ResourcesActivity.this, ResourceMapActivity.class);
                    startActivity(intent);
                }
                else {
                    showNoInternetConnectionMsg();
                }
            }
        });

        getSupportLoaderManager().initLoader(IHC_RESOURCE_LOADER_ID, null, this);

    }

    @Override
    public void onResourceClick(Resource resource) {
        String resourceURL = "http:" + resource.getResourceLink();
        Uri resourceURI = Uri.parse(resourceURL);
        Log.d(TAG, "resource URI: " + resourceURI);
        Intent linkIntent = new Intent(Intent.ACTION_VIEW, resourceURI);
        if (linkIntent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "About to go to resource link.");
            startActivity(linkIntent);
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

        if (user != null) {
            if (id == R.id.nav_resources) {
                onBackPressed();
            } else {
                //recycleBitmap();
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
                } else if (id == R.id.nav_leaderboard) {
                    Intent intent = new Intent(this, LeaderboardActivity.class);
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
        return new ResourceLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "got results from loader");
        try {
            StringTokenizer stResources = new StringTokenizer(data, "\\");
            while (stResources.hasMoreTokens()) {
                String resourceString = stResources.nextToken();
                JSONObject resourceJSON = new JSONObject(resourceString);
                int resourceID = Integer.parseInt(resourceJSON.getString("id"));
                String resourceTitle = resourceJSON.getString("title");
                String resourceDescription = resourceJSON.getString("description");
                String resourceLink = resourceJSON.getString("link");

                Resource retrievedResource = new Resource(resourceID, resourceTitle, resourceDescription, resourceLink);
                resourceList.add(retrievedResource);
            }
            for (Resource resource : resourceList) {
                mResourceAdapter.addResource(resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void showNoInternetConnectionMsg() {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else {
            builder = new android.app.AlertDialog.Builder(this);
        }
        builder.setTitle("No Internet Connection");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close alert. User can try action again.
            }
        });
        builder.setMessage(getResources().getString(R.string.no_internet_connection_msg));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    private void getProfilePicture() {
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
            Log.d(TAG, "path of image: " + fileUri.toString());
            if (fileUri.toString().contains(".jpg") || fileUri.toString().contains(".jpeg") || fileUri.toString().contains(".png")) {
                Picasso.with(this)
                        .load(fileUri.toString())
                        .into(mProfilePictureIV);
            }
        }
    }

    /*public void recycleBitmap() {
        if (profilePictureBitmap != null && !profilePictureBitmap.isRecycled()) {
            profilePictureBitmap.recycle();
            profilePictureBitmap = null;
        }
    }*/
}