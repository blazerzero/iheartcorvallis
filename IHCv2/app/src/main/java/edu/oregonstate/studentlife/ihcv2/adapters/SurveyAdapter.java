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

    public void addSurveyListing(Survey listing) {
        mSurveyContents.add(listing);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSurveyContents.size();
    }

    public interface OnSurveyListingClickListener {
        void onSurveyListingClick(Survey listing);
    }

    class SurveyViewHolder extends RecyclerView.ViewHolder {

        private TextView mSurveyQuestionTV;
        private Spinner mSurveyChoicesSP;

        public SurveyViewHolder(View itemView) {
            super(itemView);
            mSurveyQuestionTV = (TextView) itemView.findViewById(R.id.tv_survey_question);
            mSurveyChoicesSP = (Spinner) itemView.findViewById(R.id.sp_survey_choices);
        }

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
    @Override
    public SurveyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.survey_item, viewGroup, false);
        SurveyViewHolder viewHolder = new SurveyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SurveyViewHolder holder, int position) {
        Survey listing = mSurveyContents.get(position);
        holder.bind(listing);
    }

    public Survey getListing(int position) {
        return mSurveyContents.get(position);
    }

    public String getResponse(int position) {
        return mSurveyContents.get(position).getResponse();
    }
}
