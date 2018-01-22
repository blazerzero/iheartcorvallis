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

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.EventCardViewHolder> {

    private ArrayList<Event> mEventList;

    public EventCardAdapter() {
        mEventList = new ArrayList<Event>();
    }

    public void addEvent(Event event) {
        mEventList.add(event);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    class EventCardViewHolder extends RecyclerView.ViewHolder {

        private ImageView mEventImageView;
        private TextView mEventNameTextView;
        private TextView mEventInfoTextView;

        public EventCardViewHolder(View itemView) {
            super(itemView);
            mEventImageView = (ImageView) itemView.findViewById(R.id.cv_event_imageview);
            mEventNameTextView = (TextView) itemView.findViewById(R.id.tv_event_name);
            mEventInfoTextView = (TextView) itemView.findViewById(R.id.tv_event_cv_info);
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
    public EventCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.event_item_cardview, viewGroup, false);
        EventCardViewHolder viewHolder = new EventCardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventCardViewHolder holder, int position) {
        Event event = mEventList.get(position);
        holder.bind(event);
    }

}
