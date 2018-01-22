package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
        private TextView mEventNameTextView;
        private TextView mEventInfoTextView;

        public PassportViewHolder(View itemView) {
            super(itemView);
            //mPassportStampImageView = (ImageView) itemView.findViewById(R.id.iv_passport_stamp);
            mEventNameTextView = (TextView) itemView.findViewById(R.id.tv_event_name);
            mEventInfoTextView = (TextView) itemView.findViewById(R.id.tv_passport_event_info);
        }

        void bind(Event event) {
            mEventNameTextView.setText(event.getName());
            if (event.getMonth().length() <= 3) {
                mEventInfoTextView.setText(event.getMonth() + " " + event.getDay() + ", " + event.getLocation());
            }
            else {
                mEventInfoTextView.setText(event.getMonth().substring(0, 3) + ". " + event.getDay() + ", " + event.getLocation());
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
