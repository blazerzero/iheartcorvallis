package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.util.Log;

import edu.oregonstate.studentlife.ihcv2.data.User;

/**
 * Created by Omeed on 3/9/18.
 */

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String TAG = SettingsFragment.class.getSimpleName();
    private ListPreference userGradePref;
    private EditTextPreference userAgePref;
    private int userGrade;
    private int userAge;
    public final static String IHC_USER_GRADE_KEY = "IHC_USER_GRADE";
    public final static String IHC_USER_AGE_KEY = "IHC_USER_AGE";

    public interface OnDataPass {
        void onDataPass(User data);
    }

    public OnDataPass dataPasser;

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
        userGradePref = (ListPreference) findPreference(getString(R.string.pref_user_grade_key));
        userAgePref = (EditTextPreference) findPreference(getString(R.string.pref_user_age_key));
        //getLoaderManager().initLoader(IHC_SETTINGS_LOADER_ID, null, new LoaderManager.LoaderCallbacks<String>());
        //getActivity().getLoaderManager().initLoader(IHC_SETTINGS_LOADER_ID, null, );
        //if (savedInstanceState != null) {
        Bundle args = getArguments();
        userGrade = args.getInt(SettingsActivity.IHC_USER_GRADE_KEY);
        userAge = args.getInt(SettingsActivity.IHC_USER_AGE_KEY);

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

        userAgePref.setSummary(String.valueOf(userAge));
        userAgePref.setText(String.valueOf(userAge));
        if (args == null) {
            Log.d(TAG, "args is null");
        }
        /*}
        else {
            Log.d(TAG, "savedInstanceState is null");
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dataPasser = (OnDataPass) context;
        } catch (ClassCastException e) {
            Log.d(TAG, context.toString() + " must implement OnDataPass");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_user_age_key))) {
            userAgePref.setSummary(userAgePref.getText());
        }
        else if (key.equals(getString(R.string.pref_user_grade_key))) {
            userGradePref.setSummary(userGradePref.getEntry());
        }
        // SEND UPDATED USER INFO TO DATABASE

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
