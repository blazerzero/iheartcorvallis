package edu.oregonstate.studentlife.ihcv2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2.R;
import edu.oregonstate.studentlife.ihcv2.data.Event;

/**
 * Created by Omeed on 1/18/18.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

   private ArrayList<Event> mEventList;
   private OnEventClickListener mOnEventClickListener;

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

       private String[] monthShortNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

       public EventListViewHolder(View itemView) {
           super(itemView);
           mEventListingLL = (LinearLayout) itemView.findViewById(R.id.ll_event_listing);
           mEventMonthTV = (TextView) itemView.findViewById(R.id.tv_event_month);
           mEventDayTV = (TextView) itemView.findViewById(R.id.tv_event_day);
           mEventNameTV = (TextView) itemView.findViewById(R.id.tv_event_name);
           mEventLocationTV = (TextView) itemView.findViewById(R.id.tv_event_location);

           mEventListingLL.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   mOnEventClickListener.onEventClick(mEventList.get(getAdapterPosition()));
               }
           });
       }

       void bind(Event event) {
           int monthInt = Integer.parseInt(event.getMonth()) - 1;
           mEventMonthTV.setText(monthShortNames[monthInt]);
           mEventDayTV.setText(event.getDay());
           mEventNameTV.setText(event.getName());
           mEventLocationTV.setText(event.getLocation());
       }

   }

}
