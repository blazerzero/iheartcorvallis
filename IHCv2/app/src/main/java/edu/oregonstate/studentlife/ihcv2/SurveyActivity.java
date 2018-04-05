package edu.oregonstate.studentlife.ihcv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import edu.oregonstate.studentlife.ihcv2.adapters.SurveyAdapter;
import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.data.Survey;
import edu.oregonstate.studentlife.ihcv2.loaders.SurveyLoader;
import edu.oregonstate.studentlife.ihcv2.loaders.RecordSurveyResponseLoader;

public class SurveyActivity extends AppCompatActivity
    implements SurveyAdapter.OnSurveyListingClickListener,
        LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = SurveyActivity.class.getSimpleName();

    //SessionActivity session;
    private RecyclerView mSurveyContentsRV;
    private SurveyAdapter mSurveyAdapter;
    private Button submitSurveyBtn;

    private int userID;
    private ArrayList<Integer> questionIDs;
    private ArrayList<String> responses;

    private Boolean gotSurvey = false;

    private final static int IHC_SURVEY_LOADER_ID = 0;
    private final static int IHC_UPDATE_ANSWERS_LOADER_ID = 1;

    public final static String IHC_USERID_KEY = "userid";
    public final static String IHC_QUESTIONIDS_KEY = "questionids";
    public final static String IHC_RESPONSES_KEY = "responses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        getSupportActionBar().hide();

        overridePendingTransition(0, 0);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_USER_ID)) {
            userID = (int) intent.getSerializableExtra(Constants.EXTRA_USER_ID);
            Log.d(TAG, "User ID: " + userID);
        }

        questionIDs = new ArrayList<Integer>();
        responses = new ArrayList<String>();

        submitSurveyBtn = (Button) findViewById(R.id.btn_submit_survey);
        submitSurveyBtn.setOnClickListener(new View.OnClickListener() {
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
                args.putInt(IHC_USERID_KEY, userID);
                args.putIntegerArrayList(IHC_QUESTIONIDS_KEY, questionIDs);
                args.putStringArrayList(IHC_RESPONSES_KEY, responses);
                Log.d(TAG, "before init loader");
                getSupportLoaderManager().initLoader(IHC_UPDATE_ANSWERS_LOADER_ID, args, SurveyActivity.this);
                Log.d(TAG, "after init loader");
            }
        });

        mSurveyContentsRV = (RecyclerView) findViewById(R.id.rv_survey_contents);
        mSurveyContentsRV.setLayoutManager(new LinearLayoutManager(this));
        mSurveyContentsRV.setHasFixedSize(true);

        mSurveyAdapter = new SurveyAdapter(this, this);
        mSurveyContentsRV.setAdapter(mSurveyAdapter);

        getSupportLoaderManager().initLoader(IHC_SURVEY_LOADER_ID, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        if (id == IHC_SURVEY_LOADER_ID) {
            return new SurveyLoader(this);
        }
        else if (id == IHC_UPDATE_ANSWERS_LOADER_ID) {
            return new RecordSurveyResponseLoader(this, args);
        }
        else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (!gotSurvey) {
            Log.d(TAG, "got survey questions from loader");
            Log.d(TAG, "data: " + data);
            try {
                StringTokenizer stSurvey = new StringTokenizer(data, "\\");
                while (stSurvey.hasMoreTokens()) {
                    String surveyListingString = stSurvey.nextToken();
                    JSONObject surveyJSON = new JSONObject(surveyListingString);
                    int id = Integer.valueOf(surveyJSON.getString("id"));
                    String question = surveyJSON.getString("question");
                    Log.d(TAG, "question: " + question);
                    String choices = surveyJSON.getString("choices");
                    Log.d(TAG, "choices: " + choices);
                    ArrayList<String> choiceList = new ArrayList<String>();
                    StringTokenizer stChoices = new StringTokenizer(choices, ",");
                    while (stChoices.hasMoreTokens()) {
                        String choice = stChoices.nextToken();
                        choiceList.add(choice);
                    }
                    Survey s = new Survey(id, question, choiceList);
                    //response.add(s);
                    mSurveyAdapter.addSurveyListing(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            gotSurvey = true;
        }
        else {
            // check return message from update survey loader
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }
            else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Error");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Close alert. User can try action again.
                }
            });
            builder.setIcon(android.R.drawable.ic_dialog_alert);

            if (data.equals("ADDERROR")) {
                builder.setMessage(getResources().getString(R.string.add_survey_responses_error));
                builder.show();
            } else if (data.equals("TRACKERROR")) {
                builder.setMessage(getResources().getString(R.string.track_survey_status_error));
            }
            else if (data.equals("ADDSUCCESS")) {
                Intent dashIntent = new Intent(this, DashboardActivity.class);
                startActivityForResult(dashIntent, 1);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    @Override
    public void onBackPressed() {
        // Do nothing... (user should not be able to go back)
    }

    @Override
    public void onSurveyListingClick(Survey listing) {

    }
}
