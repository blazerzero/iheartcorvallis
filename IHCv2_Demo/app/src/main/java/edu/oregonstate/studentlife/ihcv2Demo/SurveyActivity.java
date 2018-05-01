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

import edu.oregonstate.studentlife.ihcv2Demo.adapters.SurveyAdapter;
import edu.oregonstate.studentlife.ihcv2Demo.data.Constants;


public class SurveyActivity extends AppCompatActivity
    {

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

    private ArrayList<Integer> questionIDs;
    private ArrayList<String> responses;

    private Boolean gotSurvey = false;
    private Boolean recordedResponses = false;
    private Boolean recordedFeedback = false;
    private Boolean skipSurvey = false;

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


        questionIDs = new ArrayList<Integer>();
        responses = new ArrayList<String>();

        mSurveyHeaderTV = (TextView) findViewById(R.id.tv_survey_header);


        mAppRatingRB = (RatingBar) findViewById(R.id.rb_app_rating);
        mAppCommentET = (EditText) findViewById(R.id.et_app_comment);
        mSubmitSurveyBtn = (Button) findViewById(R.id.btn_submit_survey);
        mSubmitSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mSurveyAdapter.getItemCount(); i++) {
                    responses.add(mSurveyAdapter.getResponse(i));
                    questionIDs.add(mSurveyAdapter.getQuestionID(i));
                }
                if (responses.contains(" ")) {
                    Toast.makeText(SurveyActivity.this, "Must respond to every question before continuing!", Toast.LENGTH_LONG).show();
                }
                Bundle args = new Bundle();
                args.putIntegerArrayList(IHC_QUESTIONIDS_KEY, questionIDs);
                args.putStringArrayList(IHC_RESPONSES_KEY, responses);
                Log.d(TAG, "before init loader");
                Log.d(TAG, "after init loader");
            }
        });

        mSkipSurveyTV = (TextView) findViewById(R.id.tv_skip_survey);
        mSkipSurveyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mSurveyContentsRV = (RecyclerView) findViewById(R.id.rv_survey_contents);
        mSurveyContentsRV.setLayoutManager(new LinearLayoutManager(this));
        mSurveyContentsRV.setHasFixedSize(true);

    }

    @Override
    public void onBackPressed() {
        // Do nothing... (user should not be able to go back)
    }


}
