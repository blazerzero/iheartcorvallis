package edu.oregonstate.studentlife.ihcv2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2.data.Event;
import edu.oregonstate.studentlife.ihcv2.R;

/**
 * Created by Omeed on 1/21/18.
 * Manages the creation and setup of each element in the Event Page's card view RecyclerView
 */

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.EventCardViewHolder> {

    private final static String TAG = EventCardAdapter.class.getSimpleName();

    private ArrayList<Event> mEventList;
    private OnEventClickListener mOnEventClickListener;
    private Context context;

    public EventCardAdapter(Context context, OnEventClickListener onEventClickListener) {
        this.context = context;
        mOnEventClickListener = onEventClickListener;
        mEventList = new ArrayList<Event>();
    }

    /* ADD AN EVENT TO THE LIST OF DISPLAYED EVENTS */
    public void addEvent(Event event) {
        mEventList.add(event);
        notifyDataSetChanged();
    }

    /* GET THE NUMBER OF EVENTS DISPLAYED */
    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    /* DECLARATION OF THE EVENT CARD CLICK LISTENER INTERFACE */
    public interface OnEventClickListener {
        void onEventClick(Event event);
    }

    /* CLASS MANAGING EACH ELEMENT OF THE EVENT'S CARD */
    class EventCardViewHolder extends RecyclerView.ViewHolder {

        private ImageView mEventImageIV;
        private TextView mEventNameTV;
        private TextView mEventDateTimeTV;
        private TextView mEventLocationTV;
        private TextView mEventLearnMoreTV;

        private String[] monthLongNames = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        public EventCardViewHolder(View itemView) {
            super(itemView);
            mEventImageIV = (ImageView) itemView.findViewById(R.id.cv_event_imageview);
            mEventNameTV = (TextView) itemView.findViewById(R.id.tv_event_name);
            mEventDateTimeTV = (TextView) itemView.findViewById(R.id.tv_event_cv_datetime);
            mEventLocationTV = (TextView) itemView.findViewById(R.id.tv_event_location);
            mEventLearnMoreTV = (TextView) itemView.findViewById(R.id.tv_event_cv_learn_more);
            mEventLearnMoreTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnEventClickListener.onEventClick(mEventList.get(getAdapterPosition()));
                }
            });
        }

        /* ADD EVENT INFORMATION TO THE EVENT'S CARD */
        void bind(Event event) {
            int startMonthInt = Integer.parseInt(event.getStartMonth()) - 1;
            int endMonthInt = Integer.parseInt(event.getEndMonth()) - 1;
            Log.d(TAG, "image path: " + event.getImagePath());
            try {
                URL url = new URL(event.getImagePath());
                if (event.getImagePath().contains(".jpg") || event.getImagePath().contains(".jpeg") || event.getImagePath().contains(".png")) {
                    Picasso.with(context)
                            .load(event.getImagePath())
                            .into(mEventImageIV);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mEventNameTV.setText(event.getName());
            mEventLocationTV.setText(event.getLocation());
            Log.d(TAG, "start dt: " + event.getStartDT());
            Log.d(TAG, "end dt: " + event.getEndDT());
            String eventDateTimeText;
            if (event.getStartDay().equals("1") && event.getStartMonth().equals("1") && event.getStartYear().equals("1900") && event.getStartTime().equals("12:00 AM")
                    && event.getEndDay().equals("31") && event.getEndMonth().equals("12") && event.getEndYear().equals("2099") && event.getEndTime().equals("11:59 PM")) {
                eventDateTimeText = "This event can be completed anytime!";
            }
            else {
                eventDateTimeText = "BEGINS: " + monthLongNames[startMonthInt] + " " + event.getStartDay() + ", "
                        + event.getStartYear() + ", " + event.getStartTime() + "\nENDS: "
                        + monthLongNames[endMonthInt] + " " + event.getEndDay() + ", "
                        + event.getEndYear() + ", " + event.getEndTime();
            }
            mEventDateTimeTV.setText(eventDateTimeText);
        }

    }

    /* CREATE THE EVENT CARD'S VIEWHOLDER */
    @Override
    public EventCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.event_item_cardview, viewGroup, false);
        EventCardViewHolder viewHolder = new EventCardViewHolder(view);
        return viewHolder;
    }

    /* GET THE EVENT'S INFORMATION AND BIND IT TO THE EVENT'S CARD */
    @Override
    public void onBindViewHolder(EventCardViewHolder holder, int position) {
        Event event = mEventList.get(position);
        holder.bind(event);
    }

}
