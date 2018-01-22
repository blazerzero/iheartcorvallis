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
        private TextView mEventDateTimeTextView;
        private TextView mEventLocationTextView;

        public EventCardViewHolder(View itemView) {
            super(itemView);
            mEventImageView = (ImageView) itemView.findViewById(R.id.cv_event_imageview);
            mEventNameTextView = (TextView) itemView.findViewById(R.id.tv_event_name);
            mEventDateTimeTextView = (TextView) itemView.findViewById(R.id.tv_event_cv_datetime);
            mEventLocationTextView = (TextView) itemView.findViewById(R.id.tv_event_location);
        }

        void bind(Event event) {
            mEventNameTextView.setText(event.getName());
            if (event.getMonth().length() <= 3) {
                mEventDateTimeTextView.setText(event.getMonth() + " " + event.getDay()
                        + ", " + event.getYear() + " @ " + event.getTime());
            }
            else {
                mEventDateTimeTextView.setText(event.getMonth().substring(0, 3)
                        + ". " + event.getDay() + ", " + event.getYear() + " @ " + event.getTime());
            }
            mEventLocationTextView.setText(event.getLocation());
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
