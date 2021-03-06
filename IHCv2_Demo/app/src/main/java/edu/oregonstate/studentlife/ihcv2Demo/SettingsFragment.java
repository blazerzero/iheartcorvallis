package edu.oregonstate.studentlife.ihcv2Demo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Omeed on 3/9/18.
 */

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String TAG = SettingsFragment.class.getSimpleName();
    private EditTextPreference userFirstNamePref;
    private EditTextPreference userLastNamePref;
    private EditTextPreference userEmailPref;
    private ListPreference userTypePref;
    private ListPreference userGradePref;
    private Preference userBirthDatePref;
    private Preference userProfilePicturePref;
    private DatePicker birthDatePicker;

    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private int userType;
    private int userGrade;
    private int userBdDay;
    private int userBdMonth;
    private int userBdYear;

    public final static String IHC_USER_FIRST_NAME_KEY = "IHC_USER_FIRST_NAME";
    public final static String IHC_USER_LAST_NAME_KEY = "IHC_USER_LAST_NAME";
    public final static String IHC_USER_EMAIL_KEY = "IHC_USER_EMAIL";
    public final static String IHC_USER_TYPE_KEY = "IHC_USER_TYPE";
    public final static String IHC_USER_GRADE_KEY = "IHC_USER_GRADE";
    public final static String IHC_USER_AGE_KEY = "IHC_USER_AGE";
    public final static String IHC_USER_BD_DAY_KEY = "IHC_USER_BIRTHDATE_DAY";
    public final static String IHC_USER_BD_MONTH_KEY = "IHC_USER_BIRTHDATE_MONTH";
    public final static String IHC_USER_BD_YEAR_KEY = "IHC_USER_BIRTHDATE_YEAR";
    public final static String IHC_USER_PROFILE_PICTURE_KEY = "IHC_USER_PROFILE_PICTURE";

    private int ACTIVITYRESULT_ID;


    //public final static String IHC_USER_GRADE_KEY = "IHC_USER_GRADE";
    //public final static String IHC_USER_AGE_KEY = "IHC_USER_AGE";

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(IHC_USER_GRADE_KEY, 4);
        args.putInt(IHC_USER_AGE_KEY, 21);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceScreen screen = getPreferenceScreen();
        userFirstNamePref = (EditTextPreference) findPreference(getString(R.string.pref_user_firstname_key));
        userLastNamePref = (EditTextPreference) findPreference(getString(R.string.pref_user_lastname_key));
        userEmailPref = (EditTextPreference) findPreference(getString(R.string.pref_user_email_key));

        userTypePref = (ListPreference) findPreference(getString(R.string.pref_user_type_key));
        userGradePref = (ListPreference) findPreference(getString(R.string.pref_user_grade_key));
        userBirthDatePref = (Preference) findPreference(getString(R.string.pref_user_birthdate_key));
        userProfilePicturePref = (Preference) findPreference(getString(R.string.pref_user_profile_picture_key));

        Bundle args = getArguments();

        userFirstName = args.getString(SettingsActivity.IHC_USER_FIRST_NAME_KEY);
        userLastName = args.getString(SettingsActivity.IHC_USER_LAST_NAME_KEY);
        userEmail = args.getString(SettingsActivity.IHC_USER_EMAIL_KEY);
        userType = args.getInt(SettingsActivity.IHC_USER_TYPE_KEY);
        userGrade = args.getInt(SettingsActivity.IHC_USER_GRADE_KEY);
        userBdDay = args.getInt(SettingsActivity.IHC_USER_BD_DAY_KEY);
        userBdMonth = args.getInt(SettingsActivity.IHC_USER_BD_MONTH_KEY);
        userBdYear = args.getInt(SettingsActivity.IHC_USER_BD_YEAR_KEY);

        userBirthDatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new DatePickerDialog(getActivity(), R.style.DialogTheme, birthDateListener, userBdYear, userBdMonth, userBdDay).show();
                return true;
            }
        });

        userFirstNamePref.setSummary(userFirstName);
        userFirstNamePref.setText(userFirstName);
        userLastNamePref.setSummary(userLastName);
        userLastNamePref.setText(userLastName);
        userEmailPref.setSummary(userEmail);
        userEmailPref.setText(userEmail);
        userProfilePicturePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showMenu();
                return true;
            }
        });
        setBirthDateSummary(userBdDay, userBdMonth - 1, userBdYear);

        if (userType < 2) {
            userFirstNamePref.setVisible(false);
            userLastNamePref.setVisible(false);
            userEmailPref.setVisible(false);
            userTypePref.setEntries(R.array.pref_user_type_entries_student);
            userTypePref.setEntryValues(R.array.pref_user_type_values_student);
            if (userType == 0) {
                userTypePref.setSummary("Domestic Student");
            } else if (userType == 1) {
                userTypePref.setSummary("International Student");
            }
            userTypePref.setValue(String.valueOf(userType));

            if (userGrade == 1) {
                userGradePref.setSummary("Freshman");
            } else if (userGrade == 2) {
                userGradePref.setSummary("Sophomore");
            } else if (userGrade == 3) {
                userGradePref.setSummary("Junior");
            } else if (userGrade == 4) {
                userGradePref.setSummary("Senior");
            } else if (userGrade == 5) {
                userGradePref.setSummary("Graduate Student");
            }
            userGradePref.setValue(String.valueOf(userGrade));
        }
        else {
            userTypePref.setEntries(R.array.pref_user_type_entries_nonstudent);
            userTypePref.setEntries(R.array.pref_user_type_entries_nonstudent);
            if (userType == 2) {
                userTypePref.setSummary("Resident");
            } else if (userType == 3) {
                userTypePref.setSummary("Visitor");
            }
            userTypePref.setValue(String.valueOf(userType));
            screen.removePreference(userGradePref);
        }

    }

    private DatePickerDialog.OnDateSetListener birthDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setBirthDateSummary(dayOfMonth, month, year);
            userBdDay = dayOfMonth;
            userBdMonth = month;
            userBdYear = year;
            Bundle args = createBundle();
            ((SettingsActivity)getActivity()).onDataPass(args);
            ((SettingsActivity)getActivity()).getProfilePicture();
        }
    };

    public Bundle createBundle() {
        Bundle args = new Bundle();
        args.putString(IHC_USER_FIRST_NAME_KEY, userFirstNamePref.getText());
        args.putString(IHC_USER_LAST_NAME_KEY, userLastNamePref.getText());
        args.putString(IHC_USER_EMAIL_KEY, userEmailPref.getText());
        args.putString(IHC_USER_TYPE_KEY, userTypePref.getValue());
        args.putString(IHC_USER_GRADE_KEY, userGradePref.getValue());
        args.putInt(IHC_USER_BD_DAY_KEY, userBdDay);
        args.putInt(IHC_USER_BD_MONTH_KEY, userBdMonth);
        args.putInt(IHC_USER_BD_YEAR_KEY, userBdYear);
        return args;
    }

    public void showMenu() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        }
        else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Change Profile Picture");
        builder.setPositiveButton("Take Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ACTIVITYRESULT_ID = 1;
                ((SettingsActivity)getActivity()).onProfilePictureMenuItemClick(ACTIVITYRESULT_ID);
            }
        });
        builder.setNegativeButton("Choose Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ACTIVITYRESULT_ID = 2;
                ((SettingsActivity)getActivity()).onProfilePictureMenuItemClick(ACTIVITYRESULT_ID);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close dialog
            }
        });
        builder.show();

    }

    public void setBirthDateSummary(int dayOfMonth, int month, int year) {
        Calendar cal = Calendar.getInstance();
        userBdDay = dayOfMonth;
        userBdMonth = month;
        userBdYear = year;
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat sdfBirthDate = new SimpleDateFormat("MM/dd/yy", Locale.US);
        userBirthDatePref.setSummary(sdfBirthDate.format(cal.getTime()));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_user_firstname_key))) {
            userFirstNamePref.setSummary(userFirstNamePref.getText());
        }
        else if (key.equals(getString(R.string.pref_user_lastname_key))) {
            userLastNamePref.setSummary(userLastNamePref.getText());
        }
        else if (key.equals(getString(R.string.pref_user_email_key))) {
            userEmailPref.setSummary(userEmailPref.getText());
        }
        else if (key.equals(getString(R.string.pref_user_type_key))) {
            userTypePref.setSummary(userTypePref.getEntry());
        }
        else if (key.equals(getString(R.string.pref_user_grade_key))) {
            userGradePref.setSummary(userGradePref.getEntry());
        }
        Bundle args = createBundle();
        ((SettingsActivity)getActivity()).onDataPass(args);

    }

    @Override
    public void onResume() {
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

}
