package edu.oregonstate.studentlife.ihcv2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.PBKDF2;
import edu.oregonstate.studentlife.ihcv2.data.Session;
import edu.oregonstate.studentlife.ihcv2.loaders.HashReceiverLoader;
import edu.oregonstate.studentlife.ihcv2.loaders.NonStudentAuthLoader;

/**
 * A login screen that offers login via email/password.
 */
public class NonStudentLoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    Session session;
    /**
     * Id to identity READ_CONTACTS permission request.
     */

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_READ_CONTACTS = 0;
    final static String IHC_LOGIN_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/non_student_login.php";
    final static String IHC_PASS_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/gethash.php";
    public static final String EXTRA_USER = "User";
    private final static String NS_LOGIN_PASS_KEY = "IHC_PASS_URL";
    private final static String NS_LOGIN_AUTH_KEY = "IHC_NS_LOGIN_URL";
    private final static int NS_LOGIN_HASH_ID = 0;
    private final static int NS_LOGIN_PASS_ID = 1;
    private boolean isAuth = false;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] CREDENTIALS = new String[]{
            "habibelo@oregonstate.com:Password123",
            "imaib@oregonstate.edu:Password123",
            "tomlinsd@oregonstate.edu:Password123"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_student_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        session = new Session(getApplicationContext());

        overridePendingTransition(0,0);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
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

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
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
        mEmailView.startAnimation(myanim);
        mPasswordView.startAnimation(myanim);

        //getSupportLoaderManager().initLoader(NS_LOGIN_LOADER_ID, null, this);

    }

    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.push_left,R.anim.push_right);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            if (isEmailValid(email)) {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                focusView = mPasswordView;
                cancel = true;
            }
            else if (TextUtils.isEmpty(email)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }
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

            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (isNetworkAvailable()) {
                //new NonStudentAuthProcess(this).execute(email);
                //new HashReceiver(this).execute(email, password);
                getSupportLoaderManager().initLoader(NS_LOGIN_HASH_ID, null, this);
            }
            else {
                showNoInternetConnectionMsg();
            }
        }
    }

    /*public Loader<String> onCreateLoader(int id, final Bundle args) {
        if (id == NS_LOGIN_PASS_ID) {
            return new AsyncTaskLoader<String>(this) {

                Context context;
                private String email;
                private String password;

                @Override
                protected void onStartLoading() {
                    if (args != null) {
                        showProgress(true);
                    }
                }

                @Override
                public String loadInBackground() {
                    if (args != null) {
                        String ihcPassCheckURL = args.getString(NS_LOGIN_PASS_KEY);
                        Log.d(TAG, "checking password");
                    }
                    email = (String) args.getString(EMAIL);
                    password = (String) args.getString(PASS);

                    try{
                        URL url = new URL(IHC_PASS_URL);

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
                    } catch (Exception e) { return new String("Exception: " + e.getMessage()); }

                }

                @Override
                public void deliverResult(String data)
            };

        }
        else return null;
    }*/

    /*private void onBackgroundTaskDataObtained(String result) {
        Log.d("NonStudentLoginActivity", "result: " + result);
        if (!result.equals("AUTHERROR")) {
            try {
                JSONObject userJSON = new JSONObject(result);
                String first = userJSON.getString("firstname");
                String last = userJSON.getString("lastname");
                String name = first + " " + last;
                String email = userJSON.getString("email");
                session.createLoginSession( name , email);
                Intent intent = new Intent(NonStudentLoginActivity.this, DashboardActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            mEmailView.setText("");
            mPasswordView.setText("");
            showProgress(false);
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }
            else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Login Error");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Try logging in again
                }
            });
            if (result.equals("AUTHERROR")) {
                builder.setMessage("Incorrect email/password combination.");
            }
            else if (result.equals("DUPACCOUNTERROR")) {
                builder.setMessage("Unable to authenticate. More than one account exists with this email/password combination.");
            }
            else if (result.equals("SEARCHERROR")) {
                builder.setMessage("Error searching for account.");
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
        //TODO: Replace this with your own logic
        return password.length() > 7;
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

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        if (id == NS_LOGIN_HASH_ID) {
            showProgress(true);
            return new HashReceiverLoader(this, email);
        }
        else if (id == NS_LOGIN_PASS_ID){
            Log.d(TAG, "Hashed password found! Time to log in.");
            return new NonStudentAuthLoader(this, email);
        }
        else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (!isAuth) {
            String resultString = (String) data;

            if (!resultString.equals("NOACCOUNTERROR")) {
                Log.d("HashReceiver", "Account found!");

                try {
                    PBKDF2 pHash = new PBKDF2();
                    pHash.validatePassword(password, resultString);
                    if (pHash.isMatch) {
                        Log.d("HashReceiver", "Password matched!");
                        //new NonStudentAuthProcess(NonStudentLoginActivity.this).execute(email);
                        isAuth = true;
                        getSupportLoaderManager().initLoader(NS_LOGIN_PASS_ID, null, this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                mEmailView.setText("");
                mPasswordView.setText("");
                showProgress(false);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(NonStudentLoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                }
                else {
                    builder = new AlertDialog.Builder(NonStudentLoginActivity.this);
                }
                builder.setTitle("Login Error");
                builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Try logging in again
                    }
                });
                builder.setMessage("Incorrect email/password combination.");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        }
        else {
            Log.d("NonStudentLoginActivity", "result: " + data);
            if (!data.equals("AUTHERROR")) {
                try {
                    JSONObject userJSON = new JSONObject(data);
                    String first = userJSON.getString("firstname");
                    String last = userJSON.getString("lastname");
                    String name = first + " " + last;
                    String email = userJSON.getString("email");
                    String tokeId = userJSON.getString("id");
                    //int id = Integer.parseInt(tokeId);
                    session.createLoginSession(name, email, tokeId);
                    Intent intent = new Intent(NonStudentLoginActivity.this, DashboardActivity.class);
                    intent.putExtra(Constants.EXTRA_CALLING_ACTIVITY_ID, NonStudentLoginActivity.class.getSimpleName());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                mEmailView.setText("");
                mPasswordView.setText("");
                showProgress(false);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                }
                else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Login Error");
                builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Try logging in again
                    }
                });
                builder.setMessage("Unable to authenticate user.");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }


        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /*class NonStudentAuthProcess extends AsyncTask {

        private Context context;
        private String email;
        private String password;
        final static String IHC_NS_LOGIN_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/non_student_login.php";

        public NonStudentAuthProcess(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {}

        @Override
        protected Object doInBackground(Object[] objects) {
            email = (String) objects[0];

            try {
                URL url = new URL(IHC_NS_LOGIN_URL);
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
            } catch (Exception e) { return new String("Exception: " + e.getMessage()); }
        }


        @Override
        protected void onPostExecute(Object result) {
            String resultString = (String) result;
            NonStudentLoginActivity.this.onBackgroundTaskDataObtained(resultString);
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



    /*class HashReceiver extends AsyncTask {

        Context context;
        private String email;
        private String password;

        final static String IHC_PASS_URL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/gethash.php";

        public HashReceiver(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {showProgress(true);}

        @Override
        protected Object doInBackground(Object[] objects) {
            email = (String) objects[0];
            password = (String) objects[1];

            try{
                URL url = new URL(IHC_PASS_URL);

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
            } catch (Exception e) { return new String("Exception: " + e.getMessage()); }

        }
        @Override
        protected void onPostExecute(Object result) {
            String resultString = (String) result;

            if (!resultString.equals("NOACCOUNTERROR")) {
                Log.d("HashReceiver", "Account found!");

                try {
                    PBKDF2 pHash = new PBKDF2();
                    pHash.validatePassword(password, resultString);
                    if (pHash.isMatch) {
                        Log.d("HashReceiver", "Password matched!");
                        new NonStudentAuthProcess(NonStudentLoginActivity.this).execute(email);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                mEmailView.setText("");
                mPasswordView.setText("");
                showProgress(false);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(NonStudentLoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                }
                else {
                    builder = new AlertDialog.Builder(NonStudentLoginActivity.this);
                }
                builder.setTitle("Login Error");
                builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Try logging in again
                    }
                });
                builder.setMessage("Incorrect email/password combination.");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }

        }
    }*/

}

