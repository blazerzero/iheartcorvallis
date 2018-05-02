package edu.oregonstate.studentlife.ihcv2;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBContract;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBHelper;
import edu.oregonstate.studentlife.ihcv2.data.Session;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.ExtraUserInfoLoader;

public class GetUserInfoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = GetUserInfoActivity.class.getSimpleName();
    private SQLiteDatabase mDB;

    Session session;

    private TextView mUserBirthDateTV;
    private CheckBox mIsUserOSUCB;
    private EditText mUserStudentIDET;
    private EditText mUserONIDET;
    private TextView mUserTypeTV;
    private Spinner mUserTypeSP;
    private TextView mUserGradeTV;
    private Spinner mUserGradeSP;
    private TextView mUserProfilePicTV;
    private Button mSubmitUserInfoBtn;

    private int userBdDay = 1;
    private int userBdMonth = 0;
    private int userBdYear = 2000;
    private String userType;
    private int userTypeValue;
    private String userGrade;
    private int userGradeValue = 1;
    private String userBirthdate;

    // For deciding what to do in onActivityResult
    private int ACTIVITYRESULT_ID;
    private String userStatus = null;
    private String sesUsername;
    private String sesEmail;
    private String sesID;

    private User user;
    private final static int IHC_USER_GETEXTRAINFO_ID = 0;
    public final static String IHC_USER_ID_KEY = "User ID";
    public final static String IHC_USER_STUDENTID_KEY = "User Student ID";
    public final static String IHC_USER_ONID_KEY = "User ONID Username";
    public final static String IHC_USER_BD_KEY = "User Birthdate";
    public final static String IHC_USER_TYPE_KEY = "User Type";
    public final static String IHC_USER_GRADE_KEY = "User Grade";

    // For taking photos
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    public static final String EXTRA_TAKE_PHOTO = "Take Photo";
    private File file;
    private final static int PERMISSIONS_REQUEST_CAMERA = 1;
    private final static int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private boolean mLocationPermissionGranted = false;

    private String[] typeChoices;
    private String[] typeValueChoices;
    private String[] gradeChoices;
    private String[] gradeValueChoices;

    private DatePickerDialog.OnDateSetListener birthDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //try {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.d(TAG, "cal birthdate: " + cal.getTime());
                SimpleDateFormat sdfEvent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                sdfEvent.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                userBirthdate = sdfEvent.format(cal.getTime());
                Log.d(TAG, "birthdate: " + userBirthdate);
                userBdYear = year;
                userBdMonth = month;
                userBdDay = dayOfMonth;
                mUserBirthDateTV.setText(createDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
            //} catch (Exception e) { e.printStackTrace(); }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info);

        getSupportActionBar().hide();

        IHCDBHelper dbHelper = new IHCDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        mUserBirthDateTV = (TextView) findViewById(R.id.tv_user_age);
        mIsUserOSUCB = (CheckBox) findViewById(R.id.cb_is_osu);
        mUserStudentIDET = (EditText) findViewById(R.id.et_user_studentid);
        mUserONIDET = (EditText) findViewById(R.id.et_user_onid);
        mUserTypeTV = (TextView) findViewById(R.id.tv_user_type);
        mUserTypeSP = (Spinner) findViewById(R.id.sp_user_type);
        mUserGradeTV = (TextView) findViewById(R.id.tv_user_grade);
        mUserGradeSP = (Spinner) findViewById(R.id.sp_user_grade);
        mUserProfilePicTV = (TextView) findViewById(R.id.tv_pick_profilepic);
        mSubmitUserInfoBtn = (Button) findViewById(R.id.btn_submit_userinfo);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constants.EXTRA_USER)) {
                user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            }
            if (intent.hasExtra(Constants.EXTRA_USER_NAME)) {
                sesUsername = intent.getStringExtra(Constants.EXTRA_USER_NAME);
            }
            if (intent.hasExtra(Constants.EXTRA_USER_EMAIL)) {
                sesEmail = intent.getStringExtra(Constants.EXTRA_USER_EMAIL);
            }
            if (intent.hasExtra(Constants.EXTRA_USER_ID)) {
                sesID = intent.getStringExtra(Constants.EXTRA_USER_ID);
            }
            if (intent.hasExtra(Constants.EXTRA_MSG)) {
                String extraMsg = intent.getStringExtra(Constants.EXTRA_MSG);
                if (extraMsg.equals(getResources().getString(R.string.extra_info_already_received))) {
                    mUserBirthDateTV.setVisibility(View.GONE);
                    mIsUserOSUCB.setVisibility(View.GONE);
                    mUserStudentIDET.setVisibility(View.GONE);
                    mUserONIDET.setVisibility(View.GONE);
                    mUserTypeTV.setVisibility(View.GONE);
                    mUserTypeSP.setVisibility(View.GONE);
                    mUserGradeTV.setVisibility(View.GONE);
                }
            }
        }

        mUserStudentIDET.setVisibility(View.GONE);
        mUserONIDET.setVisibility(View.GONE);
        mUserGradeTV.setVisibility(View.GONE);
        mUserGradeSP.setVisibility(View.GONE);
        typeChoices = getResources().getStringArray(R.array.pref_user_type_entries_nonstudent);
        typeValueChoices = getResources().getStringArray(R.array.pref_user_type_values_nonstudent);
        initializeUserTypeSpinner();
        mUserTypeTV.setText(getResources().getString(R.string.nonstudent_choose_type));

        mIsUserOSUCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        mUserStudentIDET.setVisibility(View.VISIBLE);
                        mUserONIDET.setVisibility(View.VISIBLE);
                        mUserGradeTV.setVisibility(View.VISIBLE);
                        mUserGradeSP.setVisibility(View.VISIBLE);
                        typeChoices = getResources().getStringArray(R.array.pref_user_type_entries_student);
                        typeValueChoices = getResources().getStringArray(R.array.pref_user_type_values_student);
                        mUserTypeTV.setText(getResources().getString(R.string.student_choose_type));
                        Log.d(TAG, "grade value: " + userGradeValue);
                    }
                    else {
                        mUserStudentIDET.getText().clear();
                        mUserONIDET.getText().clear();
                        mUserStudentIDET.setVisibility(View.GONE);
                        mUserONIDET.setVisibility(View.GONE);
                        mUserGradeTV.setVisibility(View.GONE);
                        mUserGradeSP.setVisibility(View.GONE);
                        typeChoices = getResources().getStringArray(R.array.pref_user_type_entries_nonstudent);
                        typeValueChoices = getResources().getStringArray(R.array.pref_user_type_values_nonstudent);
                        mUserTypeTV.setText(getResources().getString(R.string.nonstudent_choose_type));
                        userGradeValue = 0;
                    }
                    initializeUserTypeSpinner();
                }
            }
        });

        mSubmitUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mUserBirthDateTV.getText().toString().contains("Birthdate: ") ||
                        (mIsUserOSUCB.isChecked() && (mUserStudentIDET.getText().toString().length() == 0
                                || mUserONIDET.getText().toString().length() == 0))) {
                    Toast.makeText(GetUserInfoActivity.this, "Must fill all fields!", Toast.LENGTH_LONG).show();
                }
                else {
                    Bundle args = new Bundle();
                    args.putInt(IHC_USER_ID_KEY, user.getId());
                    args.putString(IHC_USER_STUDENTID_KEY, mUserStudentIDET.getText().toString());
                    args.putString(IHC_USER_ONID_KEY, mUserONIDET.getText().toString());
                    args.putInt(IHC_USER_GRADE_KEY, userGradeValue);
                    args.putInt(IHC_USER_TYPE_KEY, userTypeValue);
                    args.putString(IHC_USER_BD_KEY, userBirthdate);
                    getSupportLoaderManager().initLoader(IHC_USER_GETEXTRAINFO_ID, args, GetUserInfoActivity.this);
                }
            }
        });

        mUserBirthDateTV.setText(getResources().getString(R.string.enter_birthdate));
        mUserTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userType = mUserTypeSP.getSelectedItem().toString();
                userTypeValue = Integer.valueOf(typeValueChoices[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do...
            }
        });

        gradeChoices = getResources().getStringArray(R.array.pref_user_grade_entries);
        gradeValueChoices = getResources().getStringArray(R.array.pref_user_grade_values);
        ArrayAdapter<String> mUserGradeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, gradeChoices);
        mUserGradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserGradeSP.setAdapter(mUserGradeAdapter);
        mUserGradeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userGrade = mUserGradeSP.getSelectedItem().toString();
                userGradeValue = Integer.valueOf(gradeValueChoices[position]);
                Log.d(TAG, "grade value: " + userGradeValue);
                Log.d(TAG, "gradeValueChoices: " + gradeValueChoices);
                Log.d(TAG, "position: " + position);
                Log.d(TAG, "gradeValueChoices[position]: " + gradeValueChoices[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do...
            }
        });

        mUserBirthDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GetUserInfoActivity.this, R.style.DialogTheme, birthDateListener, userBdYear, userBdMonth, userBdDay).show();
            }
        });

        mUserProfilePicTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new ExtraUserInfoLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "got return message from loader: " + data);
        if (data.equals("ADDERROR")) {
            Toast.makeText(this, "There was an error adding this information to your profile.", Toast.LENGTH_LONG).show();
        }
        else {
            Intent surveyIntent = new Intent(this, SurveyActivity.class);
            surveyIntent.putExtra(Constants.EXTRA_USER, user);
            startActivity(surveyIntent);
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    public String createDate(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        userBdDay = dayOfMonth;
        userBdMonth = month;
        userBdYear = year;
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat sdfBirthDate = new SimpleDateFormat("MM/dd/yy");
        return "Birthdate: " + sdfBirthDate.format(cal.getTime());
    }

    public void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.i_take_photo) {
                    ACTIVITYRESULT_ID = 1;
                    if (ContextCompat.checkSelfPermission(GetUserInfoActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(GetUserInfoActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        }
                        else {
                            builder = new AlertDialog.Builder(GetUserInfoActivity.this);
                        }
                        builder.setTitle("Use Camera")
                                .setMessage("I Heart Corvallis needs access to your camera to save your profile picture.")
                                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(GetUserInfoActivity.this,
                                                new String[]{Manifest.permission.CAMERA},
                                                PERMISSIONS_REQUEST_CAMERA);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Nothing to do...
                                    }
                                })
                                .create()
                                .show();
                        //ActivityCompat.requestPermissions(GetUserInfoActivity.this, new String[] {Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                    }
                    else {
                        dispatchTakePictureIntent();
                    }
                }
                else if (id == R.id.i_choose_photo) {
                    ACTIVITYRESULT_ID = 2;
                    if (ContextCompat.checkSelfPermission(GetUserInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(GetUserInfoActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        }
                        else {
                            builder = new AlertDialog.Builder(GetUserInfoActivity.this);
                        }
                        builder.setTitle("Use Photo Gallery")
                                .setMessage("I Heart Corvallis needs access to your photo gallery to save your profile picture.")
                                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(GetUserInfoActivity.this,
                                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Nothing to do...
                                    }
                                })
                                .create()
                                .show();
                        //ActivityCompat.requestPermissions(GetUserInfoActivity.this, new String[] {Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                    }
                }
                return true;
            }
        });
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.choose_profilepic, popupMenu.getMenu());
        popupMenu.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    dispatchTakePictureIntent();
                }
            }
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = true;
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                    }
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(ACTIVITYRESULT_ID) {
            case 1:
                if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                    file = new File(mCurrentPhotoPath);
                    if (file.exists()) {
                        Uri uri;
                        uri = Uri.fromFile(file);
                        String filePath = uri.toString();
                        if (addImageToDB(filePath) != -1) {
                            mUserProfilePicTV.setText(getResources().getString(R.string.profilepic_chosen_msg));
                        }
                        else {
                            Toast.makeText(this, "There was an error saving your profile picture!", Toast.LENGTH_LONG).show();
                        }
                    }

                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String filePath = getPath(this, selectedImage);

                    if (addImageToDB(filePath) != -1) {
                        mUserProfilePicTV.setText(getResources().getString(R.string.profilepic_chosen_msg));
                    }
                    else {
                        Toast.makeText(this, "There was an error saving your profile picture!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        /*Intent surveyIntent = new Intent(this, SurveyActivity.class);
        startActivity(surveyIntent);*/
    }

    /* INITIALIZE IMAGE FILE AND LET USER TAKE PHOTO */
    private void dispatchTakePictureIntent() {
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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

    private long addImageToDB(String url) {
        ContentValues row = new ContentValues();
        row.put(IHCDBContract.SavedImages.COLUMN_IMAGE, url);
        return mDB.insert(IHCDBContract.SavedImages.TABLE_NAME, null, row);
    }

    public void initializeUserTypeSpinner() {
        ArrayAdapter<String> mUserTypeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, typeChoices);
        mUserTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserTypeSP.setAdapter(mUserTypeAdapter);
    }

    @Override
    public void onBackPressed() {
        // Do nothing...
    }

}
