package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Omeed on 1/18/18.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

   private ArrayList<Event> mEventList;

   public EventListAdapter() {
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

       private TextView mEventMonthTextView;
       private TextView mEventDayTextView;
       private TextView mEventNameTextView;
       private TextView mEventLocationTextView;

       public EventListViewHolder(View itemView) {
           super(itemView);
           mEventMonthTextView = (TextView) itemView.findViewById(R.id.tv_event_month);
           mEventDayTextView = (TextView) itemView.findViewById(R.id.tv_event_day);
           mEventNameTextView = (TextView) itemView.findViewById(R.id.tv_event_name);
           mEventLocationTextView = (TextView) itemView.findViewById(R.id.tv_event_location);
       }

       void bind(Event event) {
           mEventMonthTextView.setText(event.getMonth().substring(0, 3).toUpperCase());
           mEventDayTextView.setText(event.getDay());
           mEventNameTextView.setText(event.getName());
           mEventLocationTextView.setText(event.getLocation());
       }

   }

}
