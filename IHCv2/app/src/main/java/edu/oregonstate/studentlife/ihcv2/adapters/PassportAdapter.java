package edu.oregonstate.studentlife.ihcv2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.R;

/**
 * Created by Omeed on 1/21/18.
 */

public class PassportAdapter extends RecyclerView.Adapter<PassportAdapter.PassportViewHolder> {

    private ArrayList<Event> mCompletedEvents;

    public PassportAdapter() {
        mCompletedEvents = new ArrayList<Event>();
    }

    public void addEventToPassport(Event event) {
        mCompletedEvents.add(event);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCompletedEvents.size();
    }

    class PassportViewHolder extends RecyclerView.ViewHolder {

        //private ImageView mPassportStampImageView;
        private TextView mEventNameTV;
        private TextView mEventInfoTV;
        private LinearLayout mPassportListingLL;

        public PassportViewHolder(View itemView) {
            super(itemView);
            //mPassportStampImageView = (ImageView) itemView.findViewById(R.id.iv_passport_stamp);
            mEventNameTV = (TextView) itemView.findViewById(R.id.tv_event_name);
            mEventInfoTV = (TextView) itemView.findViewById(R.id.tv_passport_event_info);
            mPassportListingLL = (LinearLayout) itemView.findViewById(R.id.ll_passport_listing);
        }

        void bind(Event event) {
            mEventNameTV.setText(event.getName());
            if (event.getStartMonth().length() <= 3) {
                mEventInfoTV.setText(event.getStartMonth() + " " + event.getStartDay() + ", " + event.getStartYear() + ", " + event.getLocation());
            }
            else {
                mEventInfoTV.setText(event.getStartMonth().substring(0, 3) + ". " + event.getStartDay() + ", " + event.getStartYear() + ", " + event.getLocation());
            }
        }

    }

    @Override
    public PassportViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.passport_item, viewGroup, false);
        PassportViewHolder viewHolder = new PassportViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PassportViewHolder holder, int position) {
        Event event = mCompletedEvents.get(position);
        holder.bind(event);
    }

}
