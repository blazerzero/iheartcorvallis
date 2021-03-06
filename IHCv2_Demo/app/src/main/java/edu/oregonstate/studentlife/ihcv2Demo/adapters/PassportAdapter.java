package edu.oregonstate.studentlife.ihcv2Demo.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2Demo.data.Event;
import edu.oregonstate.studentlife.ihcv2Demo.R;

/**
 * Created by Omeed on 1/21/18.
 */

public class PassportAdapter extends RecyclerView.Adapter<PassportAdapter.PassportViewHolder> {

    private ArrayList<Event> mCompletedEvents;
    private final static String TAG = PassportAdapter.class.getSimpleName();
    private String[] monthShortNames = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."};

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
            Log.d(TAG, "start dt: " + event.getStartDay() + "-" + event.getStartMonth() + "-" + event.getStartYear() + " " + event.getStartTime());
            Log.d(TAG, "end dt: " + event.getEndDay() + "-" + event.getEndMonth() + "-" + event.getEndYear() + " " + event.getEndTime());
            String eventDateAndTimeText;
            if (event.getStartDay().equals("1") && event.getStartMonth().equals("1") && event.getStartYear().equals("1900") && event.getStartTime().equals("12:00 AM")
                    && event.getEndDay().equals("31") && event.getEndMonth().equals("12") && event.getEndYear().equals("2099") && event.getEndTime().equals("11:59 PM")) {
                eventDateAndTimeText = "Anytime, " + event.getLocation();
            }
            else {
                int startMonthInt = Integer.parseInt(event.getStartMonth()) - 1;
                String startMonthString = monthShortNames[startMonthInt];
                eventDateAndTimeText = startMonthString + " " + event.getStartDay() + ", " + event.getStartYear() + ", " + event.getLocation();
            }
            mEventInfoTV.setText(eventDateAndTimeText);
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
