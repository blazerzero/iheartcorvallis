package edu.oregonstate.studentlife.ihcv2;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBContract;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBHelper;
import edu.oregonstate.studentlife.ihcv2.data.Session;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.SettingsUpdateLoader;

/**
 * Created by Omeed on 12/20/17.
 * Uses the SettingsUpdateLoader to allow the user to upload additional information about themselves,
 * such as their birthdate, user type, etc.
 * Also allows the user to take a picture from their profile or use one from their gallery. (These
 * images are not uploaded to the database and are stored on the user's device).
 */

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<String> {

    Session session;

    private ImageView mProfilePictureIV;

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    SettingsFragment fragment;
    private final static int IHC_SETTINGS_LOADER_ID = 0;
    public final static String IHC_USER_ID_KEY = "IHC_USER_ID";
    public final static String IHC_USER_FIRST_NAME_KEY = "IHC_USER_FIRST_NAME";
    public final static String IHC_USER_LAST_NAME_KEY = "IHC_USER_LAST_NAME";
    public final static String IHC_USER_EMAIL_KEY = "IHC_USER_EMAIL";
    public final static String IHC_USER_TYPE_KEY = "IHC_USER_TYPE";
    public final static String IHC_USER_GRADE_KEY = "IHC_USER_GRADE";
    public final static String IHC_USER_BIRTHDATE_KEY = "IHC_USER_BIRTHDATE";
    public final static String IHC_USER_BD_DAY_KEY = "IHC_USER_BD_DAY";
    public final static String IHC_USER_BD_MONTH_KEY = "IHC_USER_BD_MONTH_DAY";
    public final static String IHC_USER_BD_YEAR_KEY = "IHC_USER_BD_YEAR_DAY";

    private final static String TAG = SettingsActivity.class.getSimpleName();

    private User user;
    private String email;
    private SQLiteDatabase mDB;
    private String mCurrentPhotoPath;
    private File file;
    private int ACTIVITYRESULT_ID;
    private static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        IHCDBHelper dbHelper = new IHCDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        overridePendingTransition(0,0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        session = new Session(getApplicationContext());
        HashMap<String, String> userBasics = session.getUserDetails();
        email = userBasics.get(Session.KEY_EMAIL);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_USER)) {
            user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            Log.d(TAG, "User ID: " + user.getId());
        }

        Date birthDate = user.getBirthDate();
        int bdDay = Integer.valueOf((String) DateFormat.format("d", birthDate));
        int bdMonth = Integer.valueOf((String) DateFormat.format("M", birthDate));
        int bdYear = Integer.valueOf((String) DateFormat.format("yyyy", birthDate));

        Bundle args = new Bundle();
        args.putString(IHC_USER_FIRST_NAME_KEY, user.getFirstName());
        args.putString(IHC_USER_LAST_NAME_KEY, user.getLastName());
        args.putString(IHC_USER_EMAIL_KEY, user.getEmail());
        args.putInt(IHC_USER_TYPE_KEY, user.getType());
        args.putInt(IHC_USER_GRADE_KEY, user.getGrade());
        args.putInt(IHC_USER_BD_DAY_KEY, bdDay);
        args.putInt(IHC_USER_BD_MONTH_KEY, bdMonth);
        args.putInt(IHC_USER_BD_YEAR_KEY, bdYear);
        fragment = new SettingsFragment();
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.settings_frame, fragment).commit();
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
            if (id == R.id.nav_settings) {
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
                } else if (id == R.id.nav_leaderboard) {
                    Intent intent = new Intent(this, LeaderboardActivity.class);
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
                } else if (id == R.id.nav_logout) {
                    session.logoutUser();
                }
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    /* GET THE USER'S ACCOUNT INFOMRATION FROM THE SETTINGS FRAGMENT AND
     * UPDATE THE USER'S ACCOUNT INFORMATION
     */
    public void onDataPass(Bundle args) {
        args.putString(IHC_USER_ID_KEY, String.valueOf(user.getId()));
        getSupportLoaderManager().restartLoader(IHC_SETTINGS_LOADER_ID, args, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new SettingsUpdateLoader(args, this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "got return message from loader: " + data);
        if (data.equals("UPDATEERROR")) {       // error updating the user's account information
            Toast.makeText(this, "There was an error updating your information.", Toast.LENGTH_LONG).show();    // show error message
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    /* RETRIEVE THE USER'S PROFILE PICTURE */
    public void getProfilePicture() {
        Cursor cursor = mDB.query(
                IHCDBContract.SavedImages.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                IHCDBContract.SavedImages.COLUMN_TIMESTAMP + " DESC"
        );      // get all profile pictures saved to the device

        Uri fileUri = null;
        if (cursor.moveToNext()) {      // if there are any saved profile pictures
            fileUri = Uri.parse(cursor.getString(
                    cursor.getColumnIndex(IHCDBContract.SavedImages.COLUMN_IMAGE)
            ));     // get the Uri of the latest profile picture
        }
        if (fileUri != null) {      // if there is a saved profile picture
            String filePath = fileUri.toString();
            Log.d(TAG, "path of image: " + filePath);
            if (filePath.contains(".jpg") || filePath.contains(".jpeg") || filePath.contains(".png")) {
                if (!filePath.contains("file://")) {
                    filePath = "file://" + filePath;
                }       // get the filepath of the profile picture
                Log.d(TAG, "updated file path: " + filePath);
                Picasso.with(this)
                        .load(filePath)
                        .into(mProfilePictureIV);       // load the profile picture into its ImageView
            }
        }
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(ACTIVITYRESULT_ID) {
            case 1:     // the user took a new profile picture
                if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                    file = new File(mCurrentPhotoPath);
                    if (file.exists()) {
                        Uri uri;
                        uri = Uri.fromFile(file);
                        String filePath = uri.toString();
                        if (addImageToDB(filePath) != -1) {     // save the new profile picture to the device's local database
                            Toast.makeText(this, "Profile picture changed!", Toast.LENGTH_LONG).show();
                            getProfilePicture();        // retrieve the new profile picture
                        }
                        else {      // error saving the new profile picture to the device's local database
                            Toast.makeText(this, "There was an error saving your profile picture!", Toast.LENGTH_LONG).show();  // show error message
                        }
                    }

                }
                break;

            case 2:     // the user chose a new profile picture
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String filePath = getPath(this, selectedImage);
                    if (addImageToDB(filePath) != -1) {     // save the new profile picture to the device's local database
                        Toast.makeText(this, "Profile picture changed!", Toast.LENGTH_LONG).show();
                        /* TO DO: update ImageView for profile picture to have the new profile picture */
                    }
                    else {      // error saving the new profile picture to the device's local database
                        Toast.makeText(this, "There was an error saving your profile picture!", Toast.LENGTH_LONG).show();  // show error message
                    }
                }
                break;
        }
        getProfilePicture();
    }

    /* INITIALIZE IMAGE FILE AND LET USER TAKE PHOTO */
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                try {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "edu.oregonstate.studentlife.ihcv2.fileprovider",
                            photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* INITIALIZE IMAGE FILE */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /* GET THE FILEPATH OF THE PROFILE PICTURE */
    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            Log.d(TAG, "File not found!");
        }
        return result;
    }

    /* ADD THE NEW PROFILE PICTURE TO THE DATABASE */
    private long addImageToDB(String url) {
        ContentValues row = new ContentValues();
        row.put(IHCDBContract.SavedImages.COLUMN_IMAGE, url);
        return mDB.insert(IHCDBContract.SavedImages.TABLE_NAME, null, row);
    }

    /* WHAT TO DO WHEN THE USER PRESSES ON AN OPTION IN THE PROFILE PICTURE OPTION MENU */
    public void onProfilePictureMenuItemClick(int id) {
        ACTIVITYRESULT_ID = id;
        if (id == 1) {
            dispatchTakePictureIntent();
        }
        else if (id == 2) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
        }
    }

}
