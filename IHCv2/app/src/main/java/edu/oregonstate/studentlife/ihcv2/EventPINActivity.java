package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.data.User;
import edu.oregonstate.studentlife.ihcv2.loaders.AddCompletedEventLoader;

public class EventPINActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private Event event;
    private User user;
    private int eventid;
    private int userid;

    private EditText mEventPINET;
    private ImageView mPINStatusIV;

    private static final int IHC_COMPLETED_EVENTS_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_pin);

        mEventPINET = (EditText) findViewById(R.id.et_event_pin);
        mPINStatusIV = (ImageView) findViewById(R.id.iv_pin_status);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EventDetailActivity.EXTRA_EVENT_DETAILED) && intent.hasExtra(EventDetailActivity.EXTRA_USER)) {
            event = (Event) intent.getSerializableExtra(EventDetailActivity.EXTRA_EVENT_DETAILED);
            user = (User) intent.getSerializableExtra(EventDetailActivity.EXTRA_USER);
            eventid = event.getEventid();
            userid = user.getId();

            mEventPINET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().equals(String.valueOf(event.getPin()))) {
                        mPINStatusIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_right_pin));
                        try {
                            View view = EventPINActivity.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            Thread.sleep(1000);
                            getSupportLoaderManager().initLoader(IHC_COMPLETED_EVENTS_ID, null, EventPINActivity.this);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        mPINStatusIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_wrong_pin));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AddCompletedEventLoader(this, userid, eventid);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data.equals("COMPLETED EVENT ADDED")) {
            Toast.makeText(this, "Congratulations! Another event done!", Toast.LENGTH_SHORT).show();
            Intent returnToDashboardIntent = new Intent(this, DashboardActivity.class);
            startActivity(returnToDashboardIntent);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }
}