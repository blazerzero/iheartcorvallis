package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

/**
 * Created by Omeed on 3/9/18.
 */

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String TAG = SettingsFragment.class.getSimpleName();
    private ListPreference userTypePref;
    private ListPreference userGradePref;
    private EditTextPreference userAgePref;
    private int userType;
    private int userGrade;
    private int userAge;
    //public final static String IHC_USER_GRADE_KEY = "IHC_USER_GRADE";
    //public final static String IHC_USER_AGE_KEY = "IHC_USER_AGE";

    /*public static SettingsFragment newInstance(User user) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(IHC_USER_GRADE_KEY, user.getGrade());
        args.putInt(IHC_USER_AGE_KEY, user.getAge());
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceScreen screen = getPreferenceScreen();
        userTypePref = (ListPreference) findPreference(getString(R.string.pref_user_type_key));
        userGradePref = (ListPreference) findPreference(getString(R.string.pref_user_grade_key));
        userAgePref = (EditTextPreference) findPreference(getString(R.string.pref_user_age_key));

        Bundle args = getArguments();
        userType = args.getInt(SettingsActivity.IHC_USER_TYPE_KEY);
        userGrade = args.getInt(SettingsActivity.IHC_USER_GRADE_KEY);
        userAge = args.getInt(SettingsActivity.IHC_USER_AGE_KEY);

        if (userType < 2) {
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
            userTypePref.setEntries(R.array.pref_user_type_entries_resident);
            userTypePref.setEntries(R.array.pref_user_type_entries_resident);
            if (userType == 2) {
                userTypePref.setSummary("Resident");
            } else if (userType == 3) {
                userTypePref.setSummary("Visitor");
            }
            userTypePref.setValue(String.valueOf(userType));
            screen.removePreference(userGradePref);
        }

        userAgePref.setSummary(String.valueOf(userAge));
        userAgePref.setText(String.valueOf(userAge));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_user_type_key))) {
            userTypePref.setSummary(userTypePref.getEntry());
        }
        if (key.equals(getString(R.string.pref_user_age_key))) {
            userAgePref.setSummary(userAgePref.getText());
        }
        else if (key.equals(getString(R.string.pref_user_grade_key))) {
            userGradePref.setSummary(userGradePref.getEntry());
        }
        ((SettingsActivity)getActivity()).onDataPass(String.valueOf(userTypePref.getValue()), String.valueOf(userGradePref.getValue()), userAgePref.getText());

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
