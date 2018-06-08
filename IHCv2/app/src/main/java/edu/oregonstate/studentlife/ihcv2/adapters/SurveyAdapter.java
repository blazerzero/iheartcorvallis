package edu.oregonstate.studentlife.ihcv2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2.R;
import edu.oregonstate.studentlife.ihcv2.SurveyActivity;
import edu.oregonstate.studentlife.ihcv2.data.Survey;

/**
 * Created by Omeed on 4/3/18.
 * Manages the creation and setup of each element in the survey's RecyclerView
 */

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder> {

    private final static String TAG = SurveyAdapter.class.getSimpleName();

    private ArrayList<Survey> mSurveyContents;
    private OnSurveyListingClickListener mOnSurveyListingClickListener;
    private Context context;

    public SurveyAdapter(Context context, OnSurveyListingClickListener onSurveyContentClickListener) {
        this.context = context;
        mOnSurveyListingClickListener = onSurveyContentClickListener;
        mSurveyContents = new ArrayList<Survey>();
    }

    /* ADD A SURVEY QUESTION TO THE LIST OF DISPLAYED SURVEY QUESTIONS */
    public void addSurveyListing(Survey listing) {
        mSurveyContents.add(listing);
        notifyDataSetChanged();
    }

    /* GET THE NUMBER OF SURVEY QUESTIONS DISPLAYED */
    @Override
    public int getItemCount() {
        return mSurveyContents.size();
    }

    /* DECLARATION OF THE SURVEY QUESTION CLICK LISTENER INTERFACE */
    public interface OnSurveyListingClickListener {
        void onSurveyListingClick(Survey listing);
    }

    /* CLASS MANAGING EACH ELEMENT OF THE SURVEY QUESTION LISTING */
    class SurveyViewHolder extends RecyclerView.ViewHolder {

        private TextView mSurveyQuestionTV;
        private Spinner mSurveyChoicesSP;

        public SurveyViewHolder(View itemView) {
            super(itemView);
            mSurveyQuestionTV = (TextView) itemView.findViewById(R.id.tv_survey_question);
            mSurveyChoicesSP = (Spinner) itemView.findViewById(R.id.sp_survey_choices);
        }

        /* ADD SURVEY QUESTION INFORMATION TO THE SURVEY QUESTION LISTING */
        void bind(final Survey listing) {
            Log.d(TAG, "question: " + listing.getQuestion());
            mSurveyQuestionTV.setText(listing.getQuestion());

            ArrayList<String> choices = listing.getChoices();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    context, android.R.layout.simple_spinner_item, choices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSurveyChoicesSP.setAdapter(adapter);
            mSurveyChoicesSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    listing.setResponse(mSurveyChoicesSP.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    listing.setResponse(" ");
                }
            });
        }
    }

    /* CREATE THE SURVEY QUESTION LISTING'S VIEWHOLDER */
    @Override
    public SurveyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.survey_item, viewGroup, false);
        SurveyViewHolder viewHolder = new SurveyViewHolder(view);
        return viewHolder;
    }

    /* GET THE SURVEY QUESTION'S INFORMATION AND BIND IT TO THE SURVEY QUESTION'S LISTING */
    @Override
    public void onBindViewHolder(SurveyViewHolder holder, int position) {
        Survey listing = mSurveyContents.get(position);
        holder.bind(listing);
    }

    public Survey getListing(int position) {
        return mSurveyContents.get(position);
    }

    public int getQuestionID(int position) {
        return mSurveyContents.get(position).getId();
    }

    public String getResponse(int position) {
        return mSurveyContents.get(position).getResponse();
    }
}
