package edu.oregonstate.studentlife.ihcv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import edu.oregonstate.studentlife.ihcv2.data.Event;

public class EventPINActivity extends AppCompatActivity {

    private Event event;

    private EditText mEventPINET;
    private ImageView mPINStatusIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_pin);

        mEventPINET = (EditText) findViewById(R.id.et_event_pin);
        mPINStatusIV = (ImageView) findViewById(R.id.iv_pin_status);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EventDetailActivity.EXTRA_EVENT_DETAILED)) {
            event = (Event) intent.getSerializableExtra(EventDetailActivity.EXTRA_EVENT_DETAILED);

            mEventPINET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().equals(String.valueOf(event.getPin()))) {
                        mPINStatusIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_right_pin));
                        /*try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
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
        }
    }
}
