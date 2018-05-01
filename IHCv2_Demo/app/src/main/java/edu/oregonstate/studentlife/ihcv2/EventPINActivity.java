package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.AddCompletedEventLoader;

public class EventPINActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private Event event;
    private User user;
    private int eventid;
    private int userid;

    private TextView mEventNameTV;
    private EditText mEventPINET;
    private ImageView mPINStatusIV;
    private RatingBar mEventRatingRB;
    private EditText mEventCommentET;
    private Button mSubmitEventPinBtn;

    private static final int IHC_COMPLETED_EVENTS_ID = 0;
    private final static String TAG = EventPINActivity.class.getSimpleName();

    public final static String IHC_COMPLETED_EVENT_USERID_KEY = "Completed Event User ID";
    public final static String IHC_COMPLETED_EVENT_EVENTID_KEY = "Completed Event Event ID";
    public final static String IHC_COMPLETED_EVENT_RATING_KEY = "Completed Event Rating";
    public final static String IHC_COMPLETED_EVENT_COMMENT_KEY = "Completed Event Comment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_pin);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mEventNameTV = (TextView) findViewById(R.id.tv_pin_event_name);
        mEventPINET = (EditText) findViewById(R.id.et_event_pin);
        mPINStatusIV = (ImageView) findViewById(R.id.iv_pin_status);
        mEventRatingRB = (RatingBar) findViewById(R.id.rb_event_rating);
        mEventCommentET = (EditText) findViewById(R.id.et_event_comment);
        mSubmitEventPinBtn = (Button) findViewById(R.id.btn_submit_event_pin);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_EVENT_DETAILED) && intent.hasExtra(Constants.EXTRA_USER)) {
            event = (Event) intent.getSerializableExtra(Constants.EXTRA_EVENT_DETAILED);
            user = (User) intent.getSerializableExtra(Constants.EXTRA_USER);
            eventid = event.getEventid();
            userid = user.getId();
            Log.d(TAG, "event ID: " + String.valueOf(eventid));
            Log.d(TAG, "user ID: " + String.valueOf(userid));

            mEventNameTV.setText(event.getName());

            mEventPINET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().equals(String.valueOf(event.getPin()))) {
                        mPINStatusIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_right_pin));
                        //try {
                        View view = EventPINActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                            //Thread.sleep(1000);
                            //getSupportLoaderManager().initLoader(IHC_COMPLETED_EVENTS_ID, null, EventPINActivity.this);

                        /*} catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    }
                    else {
                        mPINStatusIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_wrong_pin));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mSubmitEventPinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mEventPINET.getText().toString().equals(String.valueOf(event.getPin()))) {
                        Toast.makeText(EventPINActivity.this, "Must enter correct event PIN.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Bundle args = new Bundle();
                        args.putInt(IHC_COMPLETED_EVENT_USERID_KEY, userid);
                        args.putInt(IHC_COMPLETED_EVENT_EVENTID_KEY, eventid);
                        args.putInt(IHC_COMPLETED_EVENT_RATING_KEY, (int)mEventRatingRB.getRating());
                        args.putString(IHC_COMPLETED_EVENT_COMMENT_KEY, mEventCommentET.getText().toString());
                        getSupportLoaderManager().initLoader(IHC_COMPLETED_EVENTS_ID, args, EventPINActivity.this);
                    }
                }
            });
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AddCompletedEventLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data.equals("COMPLETED EVENT ADDED")) {
            Toast.makeText(this, "Congratulations! Another event done!", Toast.LENGTH_SHORT).show();
            Intent returnToDashboardIntent = new Intent(this, DashboardActivity.class);
            returnToDashboardIntent.putExtra(Constants.EXTRA_CALLING_ACTIVITY_ID, EventPINActivity.class.getSimpleName());
            startActivity(returnToDashboardIntent);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
