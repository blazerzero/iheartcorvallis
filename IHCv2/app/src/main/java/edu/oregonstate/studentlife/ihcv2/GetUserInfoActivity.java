package edu.oregonstate.studentlife.ihcv2;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBContract;
import edu.oregonstate.studentlife.ihcv2.data.IHCDBHelper;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.ExtraUserInfoLoader;

public class GetUserInfoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = GetUserInfoActivity.class.getSimpleName();
    private SQLiteDatabase mDB;

    private TextView mUserBirthDateTV;
    private TextView mUserTypeTV;
    private Spinner mUserTypeSP;
    private TextView mUserGradeTV;
    private Spinner mUserGradeSP;
    private TextView mUserProfilePicTV;
    private Button mSubmitUserInfoBtn;

    private int userBdDay = 1;
    private int userBdMonth = 1;
    private int userBdYear = 2000;
    private String userType;
    private int userTypeValue;
    private String userGrade;
    private int userGradeValue = 0;
    private String userBirthdate;

    // For deciding what to do in onActivityResult
    private int ACTIVITYRESULT_ID;
    private String userStatus = null;

    private User user;
    private final static int IHC_USER_GETEXTRAINFO_ID = 0;
    public final static String IHC_USER_ID_KEY = "User ID";
    public final static String IHC_USER_BD_KEY = "User Birthdate";
    public final static String IHC_USER_TYPE_KEY = "User Type";
    public final static String IHC_USER_GRADE_KEY = "User Grade";

    // For taking photos
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    public static final String EXTRA_TAKE_PHOTO = "Take Photo";
    private File file;

    private String[] typeChoices;
    private int[] typeValueChoices;
    private String[] gradeChoices;
    private int[] gradeValueChoices;

    private DatePickerDialog.OnDateSetListener birthDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            userBdYear = year;
            userBdMonth = month;
            userBdDay = dayOfMonth;
            mUserBirthDateTV.setText(createDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info);

        getSupportActionBar().hide();

        IHCDBHelper dbHelper = new IHCDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constants.EXTRA_USER_STATUS)) {
                userStatus = (String) intent.getSerializableExtra(Constants.EXTRA_USER_STATUS);
            }
            if (intent.hasExtra(Constants.EXTRA_USER)) {
                user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            }
        }

        mUserBirthDateTV = (TextView) findViewById(R.id.tv_user_age);
        mUserTypeTV = (TextView) findViewById(R.id.tv_user_type);
        mUserTypeSP = (Spinner) findViewById(R.id.sp_user_type);
        mUserGradeTV = (TextView) findViewById(R.id.tv_user_grade);
        mUserGradeSP = (Spinner) findViewById(R.id.sp_user_grade);
        mUserProfilePicTV = (TextView) findViewById(R.id.tv_pick_profilepic);
        mSubmitUserInfoBtn = (Button) findViewById(R.id.btn_submit_userinfo);

        mSubmitUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent dashIntent = new Intent(GetUserInfoActivity.this, SurveyActivity.class);
                startActivity(dashIntent);*/
                Bundle args = new Bundle();
                args.putInt(IHC_USER_ID_KEY, user.getId());
                args.putInt(IHC_USER_GRADE_KEY, userGradeValue);
                args.putInt(IHC_USER_TYPE_KEY, userTypeValue);
                args.putString(IHC_USER_BD_KEY, userBirthdate);
                getSupportLoaderManager().initLoader(IHC_USER_GETEXTRAINFO_ID, args, GetUserInfoActivity.this);
            }
        });

        mUserBirthDateTV.setText(getResources().getString(R.string.enter_birthdate));
/*        if (userStatus.equals("Student")) {
            typeChoices = getResources().getStringArray(R.array.pref_user_type_entries_student);
            typeValueChoices = getResources().getIntArray(R.array.pref_user_type_values_student);
            mUserTypeTV.setText(getResources().getString(R.string.student_choose_type));
        }
        else if (userStatus.equals("Non-Student")) {*/
            mUserGradeTV.setVisibility(View.GONE);
            mUserGradeSP.setVisibility(View.GONE);
            typeChoices = getResources().getStringArray(R.array.pref_user_type_entries_nonstudent);
            typeValueChoices = getResources().getIntArray(R.array.pref_user_type_values_nonstudent);
            mUserTypeTV.setText(getResources().getString(R.string.nonstudent_choose_type));
        //}

        ArrayAdapter<String> mUserTypeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, typeChoices);
        mUserTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserTypeSP.setAdapter(mUserTypeAdapter);
        mUserTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userType = mUserTypeSP.getSelectedItem().toString();
                /*if (userStatus.equals("Student")) {
                    userTypeValue = typeValueChoices[position];
                }
                else if (userStatus.equals("Non-Student")) {*/
                    userTypeValue = typeValueChoices[position] + 2;
                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do...
            }
        });

        gradeChoices = getResources().getStringArray(R.array.pref_user_grade_entries);
        gradeValueChoices = getResources().getIntArray(R.array.pref_user_grade_values);
        ArrayAdapter<String> mUserGradeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, gradeChoices);
        mUserGradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserGradeSP.setAdapter(mUserGradeAdapter);
        mUserGradeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userGrade = mUserGradeSP.getSelectedItem().toString();
                userGradeValue = gradeValueChoices[position];
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
                    dispatchTakePictureIntent();
                }
                else if (id == R.id.i_choose_photo) {
                    ACTIVITYRESULT_ID = 2;
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                }
                return true;
            }
        });
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.choose_profilepic, popupMenu.getMenu());
        popupMenu.show();
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
                        Log.d(TAG, "Row: " + addImageToDB(filePath));
                    }

                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String filePath = getPath(this, selectedImage);
                    addImageToDB(filePath);
                }
                break;
        }
        Intent surveyIntent = new Intent(this, SurveyActivity.class);
        startActivity(surveyIntent);
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

}
