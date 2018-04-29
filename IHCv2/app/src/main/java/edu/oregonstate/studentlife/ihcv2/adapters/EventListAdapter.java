package edu.oregonstate.studentlife.ihcv2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import edu.oregonstate.studentlife.ihcv2.R;
import edu.oregonstate.studentlife.ihcv2.data.Event;

/**
 * Created by Omeed on 1/18/18.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

   private ArrayList<Event> mEventList;
   private OnEventClickListener mOnEventClickListener;
   private final static String TAG = EventListAdapter.class.getSimpleName();

   public EventListAdapter(OnEventClickListener onEventClickListener) {
       mEventList = new ArrayList<Event>();
       mOnEventClickListener = onEventClickListener;
   }

   public void addEvent(Event event) {
       mEventList.add(event);
       notifyDataSetChanged();
   }

   @Override
   public int getItemCount() {
       return mEventList.size();
   }

   public interface OnEventClickListener {
       void onEventClick(Event event);
   }

   @Override
   public EventListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
       LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
       View view = inflater.inflate(R.layout.event_item_listview, viewGroup, false);
       EventListViewHolder viewHolder = new EventListViewHolder(view);
       return viewHolder;
   }

   @Override
   public void onBindViewHolder(EventListViewHolder holder, int position) {
       Event event = mEventList.get(position);
       holder.bind(event);
   }

   class EventListViewHolder extends RecyclerView.ViewHolder {

       private LinearLayout mEventListingLL;
       private TextView mEventMonthTV;
       private TextView mEventDayTV;
       private TextView mEventNameTV;
       private TextView mEventLocationTV;
       private TextView mEventTimeTV;

       private String[] monthShortNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

       public EventListViewHolder(View itemView) {
           super(itemView);
           mEventListingLL = (LinearLayout) itemView.findViewById(R.id.ll_event_listing);
           mEventMonthTV = (TextView) itemView.findViewById(R.id.tv_event_month);
           mEventDayTV = (TextView) itemView.findViewById(R.id.tv_event_day);
           mEventNameTV = (TextView) itemView.findViewById(R.id.tv_event_name);
           mEventLocationTV = (TextView) itemView.findViewById(R.id.tv_event_location);
           mEventTimeTV = (TextView) itemView.findViewById(R.id.tv_event_time);

           mEventListingLL.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   mOnEventClickListener.onEventClick(mEventList.get(getAdapterPosition()));
               }
           });
       }

       void bind(Event event) {
           mEventNameTV.setText(event.getName());
           mEventLocationTV.setText(event.getLocation());
           Log.d(TAG, "start dt: " + event.getStartDay() + "-" + event.getStartMonth() + "-" + event.getStartYear() + " " + event.getStartTime());
           Log.d(TAG, "end dt: " + event.getEndDay() + "-" + event.getEndMonth() + "-" + event.getEndYear() + " " + event.getEndTime());
           if (event.getStartDay().equals("1") && event.getStartMonth().equals("01") && event.getStartYear().equals("1900") && event.getStartTime().equals("12:00 AM")
                   && event.getEndDay().equals("31") && event.getEndMonth().equals("12") && event.getEndYear().equals("2099") && event.getEndTime().equals("11:59 PM")) {
               mEventMonthTV.setText("ANY");
               mEventDayTV.setText("TIME");
               mEventTimeTV.setText("Anytime");
           }
           else {
               int monthInt = Integer.parseInt(event.getStartMonth()) - 1;
               mEventMonthTV.setText(monthShortNames[monthInt]);
               mEventDayTV.setText(event.getStartDay());
               String dtRange = event.getStartTime() + " - " + event.getEndTime();
               mEventTimeTV.setText(dtRange);
           }
       }

   }

}
