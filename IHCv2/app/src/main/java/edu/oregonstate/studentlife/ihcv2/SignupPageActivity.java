package edu.oregonstate.studentlife.ihcv2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.Session;
import edu.oregonstate.studentlife.ihcv2.loaders.SignupLoader;
import edu.oregonstate.studentlife.ihcv2.loaders.StudentSignupLoader;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * The signup page for a user to create an account. Uses the SignupLoader or the StudentSignupLoader
 * depending on if the users claims if they're a student to upload their entered information
 * into the database.
 */
public class SignupPageActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    Session session;
    public final static String IHC_FIRSTNAME_KEY = "firstname";
    public final static String IHC_LASTNAME_KEY = "lastname";
    public final static String IHC_EMAIL_KEY = "email";
    public final static String IHC_PASSWORD_KEY = "password";
    public final static String IHC_STATUS_KEY = "signup";
    private final static int IHC_NONSTUDENT_SIGNUP_LOADER_ID = 0;
    private final static int IHC_STUDENT_SIGNUP_LOADER_ID = 1;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mStudentLoginLinkTV;
    private WebView mONIDSignupWV;

    private final static String TAG = SignupPageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);

        getSupportActionBar().hide();

        overridePendingTransition(0,0);

        mONIDSignupWV = (WebView) findViewById(R.id.wv_cas_signup);
        mONIDSignupWV.setVisibility(View.GONE);

        mStudentLoginLinkTV = (TextView) findViewById(R.id.studentlink);
        mStudentLoginLinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(SignupPageActivity.this, DashboardActivity.class);
                startActivity(intent);*/
                //Intent intent = new Intent(SignupPageActivity.this, CASWebViewActivity.class);
                //startActivity(intent);
                Log.d(TAG, new Date().toString());
                mONIDSignupWV.setVisibility(View.VISIBLE);
                mONIDSignupWV.loadUrl("https://login.oregonstate.edu/idp/profile/cas/login?service=http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/studentsignup.php");
                getSupportLoaderManager().initLoader(IHC_STUDENT_SIGNUP_LOADER_ID, null, SignupPageActivity.this);
            }
        });

        mFirstNameView = (EditText) findViewById(R.id.firstnamefield);
        mLastNameView = (EditText) findViewById(R.id.lastnamefield);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.emailfield);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.passwordfield);
        mPasswordConfirmView = (EditText) findViewById(R.id.passwordconfirmfield);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.create_account_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        mLoginFormView.startAnimation(myanim);
        mFirstNameView.startAnimation(myanim);
        mLastNameView.startAnimation(myanim);
        mEmailView.startAnimation(myanim);
        mPasswordView.startAnimation(myanim);
        mEmailSignInButton.startAnimation(myanim);
    }

    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.push_left,R.anim.push_right);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        //getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        char ch;
        boolean hasUppercase = false;
        boolean hasLowercase = false;

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String firstname = mFirstNameView.getText().toString();
        String lastname = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordconfirm = mPasswordConfirmView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid first name.
        if (TextUtils.isEmpty(firstname)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

        // Check for a valid last name.
        if (TextUtils.isEmpty(lastname)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }

        // Check if password field is empty.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        //Check if the second input password is empty
        if (TextUtils.isEmpty(passwordconfirm)) {
            mPasswordConfirmView.setError("This field is required!");
            focusView = mPasswordConfirmView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && password.length() < 7) {
            mPasswordView.setError(getString(R.string.error_short_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check if both of the entered passwords are equal
        if (!Objects.equals(password, passwordconfirm)) {
            mPasswordConfirmView.setError("Your Passwords do not match");
            focusView = mPasswordConfirmView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (isNetworkAvailable()) {
                View view = this.getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Bundle args = new Bundle();
                args.putString(IHC_FIRSTNAME_KEY, firstname);
                args.putString(IHC_LASTNAME_KEY, lastname);
                args.putString(IHC_EMAIL_KEY, email);
                args.putString(IHC_PASSWORD_KEY, password);
                getSupportLoaderManager().initLoader(IHC_NONSTUDENT_SIGNUP_LOADER_ID, args, this);
                //new SignupAuthProcess(this).execute(firstname, lastname, email, password);
            }
            else {
                showNoInternetConnectionMsg();
            }
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
      if (id == IHC_NONSTUDENT_SIGNUP_LOADER_ID) {
        return new SignupLoader(this, args);
      }
      else if (id == IHC_STUDENT_SIGNUP_LOADER_ID) {
        return new StudentSignupLoader(this);
      }
      else {
          return null;
      }
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data.contains("SIGNUPSUCCESS")) {
            try {
                StringTokenizer stRes = new StringTokenizer(data);
                String name = stRes.nextToken("\\");
                String email = stRes.nextToken("\\");
                String tid = stRes.nextToken("\\");
                //int id = Integer.parseInt(tid);
                session = new Session(getApplicationContext());
                session.createLoginSession(name, email, tid);
                Intent intent = new Intent(SignupPageActivity.this, DashboardActivity.class);
                intent.putExtra(Constants.EXTRA_CALLING_ACTIVITY_ID, SignupPageActivity.class.getSimpleName());
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else {
            mPasswordView.setText("");
            mPasswordConfirmView.setText("");
            showProgress(false);
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }
            else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Sign Up Error");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Try signing up again
                }
            });
            if (data.equals("DUPACCOUNTERROR")) {
                builder.setMessage("An account with this email already exists.");
            }
            else if (data.equals("SIGNUPERROR")) {
                builder.setMessage("Error signing up.");
            }
            else {
                builder.setMessage(data);
            }
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    /*private void onBackgroundTaskDataObtained(String result) {
        if (result.contains("SIGNUPSUCCESS")) {
            try {
                StringTokenizer stRes = new StringTokenizer(result);
                String name = stRes.nextToken("\\");
                String email = stRes.nextToken("\\");
                String tid = stRes.nextToken("\\");
                //int id = Integer.parseInt(tid);
                session = new Session(getApplicationContext());
                session.createLoginSession(name, email, tid);
                Intent intent = new Intent(SignupPageActivity.this, DashboardActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else {
            mPasswordView.setText("");
            showProgress(false);
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }
            else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Sign Up Error");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Try signing up again
                }
            });
            if (result.equals("DUPACCOUNTERROR")) {
                builder.setMessage("An account with this email already exists.");
            }
            else if (result.equals("SIGNUPERROR")) {
                builder.setMessage("Error signing up.");
            }
            else {
                builder.setMessage(result);
            }
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
        }

    }*/

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        char ch;
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
                break;
            }
        }
        for (int i = 0; i < password.length(); i++) {
            ch = password.charAt(i);
            if (Character.isLowerCase(ch)) {
                hasLowercase = true;
                break;
            }
        }
        for (int i = 0; i < password.length(); i++) {
            ch = password.charAt(i);
            if (Character.isDigit(ch)) {
                hasDigit = true;
                break;
            }
        }
        return (hasUppercase && hasLowercase && hasDigit);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /*@Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }*/

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignupPageActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /*class SignupAuthProcess extends AsyncTask {

        private Context context;
        private String firstname;
        private String lastname;
        private String email;
        private String password;
        final static String IHC_SIGNUP_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/signup.php";

        public SignupAuthProcess(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            firstname = (String) objects[0];
            lastname = (String) objects[1];
            email = (String) objects[2];
            password = (String) objects[3];
            PBKDF2 pHash;



            try {
                pHash = new PBKDF2();
                pHash.createPBKDF2hash(password);

                URL url = new URL(IHC_SIGNUP_URL);
                String data = URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(firstname, "UTF-8");
                data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(lastname, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pHash.hPassword, "UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = null;

                sb.append(firstname + " " + lastname + "\\" + email + "\\");

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            } catch (Exception e) { return new String("Exception: " + e.getMessage()); }
        }


        @Override
        protected void onPostExecute(Object result) {
            String resultString = (String) result;
            SignupPageActivity.this.onBackgroundTaskDataObtained(resultString);
        }
    }*/

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

}
