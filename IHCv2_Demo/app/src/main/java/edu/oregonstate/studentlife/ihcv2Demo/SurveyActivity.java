package edu.oregonstate.studentlife.ihcv2Demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import edu.oregonstate.studentlife.ihcv2Demo.adapters.SurveyAdapter;
import edu.oregonstate.studentlife.ihcv2Demo.data.Constants;
import edu.oregonstate.studentlife.ihcv2Demo.data.Survey;


public class SurveyActivity extends AppCompatActivity
        implements SurveyAdapter.OnSurveyListingClickListener {

    private final static String TAG = SurveyActivity.class.getSimpleName();

    //Session session;
    private TextView mSurveyHeaderTV;
    private RatingBar mAppRatingRB;
    private EditText mAppCommentET;
    private RecyclerView mSurveyContentsRV;
    private SurveyAdapter mSurveyAdapter;
    private Button mSubmitSurveyBtn;
    private TextView mSkipSurveyTV;

    private byte[] profilePictureByteArray = null;

    private ArrayList<Survey> surveyListings;

    private final static int IHC_SURVEY_LOADER_ID = 0;
    private final static int IHC_RECORD_RESPONSES_LOADER_ID = 1;
    private final static int IHC_RECORD_FEEDBACK_LOADER_ID = 2;
    private final static int IHC_SKIP_SURVEY_LOADER_ID = 3;

    public final static String IHC_QUESTIONIDS_KEY = "questionids";
    public final static String IHC_RESPONSES_KEY = "responses";
    public final static String IHC_APPRATING_KEY = "app rating";
    public final static String IHC_APPCOMMENT_KEY = "app comment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        getSupportActionBar().hide();

        overridePendingTransition(0, 0);
        Intent intent = getIntent();

        mSurveyHeaderTV = (TextView) findViewById(R.id.tv_survey_header);


        mAppRatingRB = (RatingBar) findViewById(R.id.rb_app_rating);
        mAppCommentET = (EditText) findViewById(R.id.et_app_comment);
        mSubmitSurveyBtn = (Button) findViewById(R.id.btn_submit_survey);
        mSubmitSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashIntent = new Intent(SurveyActivity.this, DashboardActivity.class);
                startActivity(dashIntent);
            }
        });

        mSkipSurveyTV = (TextView) findViewById(R.id.tv_skip_survey);
        mSkipSurveyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashIntent = new Intent(SurveyActivity.this, DashboardActivity.class);
                startActivity(dashIntent);
            }
        });

        mSurveyContentsRV = (RecyclerView) findViewById(R.id.rv_survey_contents);
        mSurveyContentsRV.setLayoutManager(new LinearLayoutManager(this));
        mSurveyContentsRV.setHasFixedSize(true);

        mSurveyAdapter = new SurveyAdapter(this, this);
        mSurveyContentsRV.setAdapter(mSurveyAdapter);

        surveyListings = new ArrayList<Survey>();

        String[] choices1 = {"10", "9", "8", "7", "6", "5", "4", "3", "2", "1"};
        surveyListings.add(
                new Survey(1, "On a scale from 1 to 10, how much do you connect with the greater Corvallis community?", new ArrayList<String>(Arrays.asList(choices1)))
        );
        String[] choices2 = {"Once a week or more", "A couple of times a month", "A couple of times a year", "Once a year", "Less than once a year"};
        surveyListings.add(
                new Survey(2, "How often do you generally attend community events and activities?", new ArrayList<String>(Arrays.asList(choices2)))
        );
        String[] choices3 = {"Absolutely", "Somewhat", "Neutral", "Not much", "Not at all"};
        surveyListings.add(
                new Survey(3, "Do you feel like you are a part of the greater Corvallis community?", new ArrayList<String>(Arrays.asList(choices3)))
        );

    }

    @Override
    public void onBackPressed() {
        // Do nothing... (user should not be able to go back)
    }

    @Override
    public void onSurveyListingClick(Survey listing) {

    }




}
