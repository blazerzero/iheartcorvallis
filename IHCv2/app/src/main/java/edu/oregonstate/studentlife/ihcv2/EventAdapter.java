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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

   private ArrayList<Event> mEventList;

   public EventAdapter() {
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
   public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
       LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
       View view = inflater.inflate(R.layout.event_item, viewGroup, false);
       EventViewHolder viewHolder = new EventViewHolder(view);
       return viewHolder;
   }

   @Override
   public void onBindViewHolder(EventViewHolder holder, int position) {
       Event event = mEventList.get(position);
       holder.bind(event);
   }

   class EventViewHolder extends RecyclerView.ViewHolder {

       private TextView mEventDateTextView;
       private TextView mEventTimeTextView;
       private TextView mEventNameTextView;
       private TextView mEventLocationTextView;

       public EventViewHolder(View itemView) {
           super(itemView);
           mEventDateTextView = (TextView) itemView.findViewById(R.id.tv_event_date);
           mEventTimeTextView = (TextView) itemView.findViewById(R.id.tv_event_time);
           mEventNameTextView = (TextView) itemView.findViewById(R.id.tv_event_name);
           mEventLocationTextView = (TextView) itemView.findViewById(R.id.tv_event_location);
       }

       void bind(Event event) {
           mEventDateTextView.setText(event.getDate());
           mEventTimeTextView.setText(event.getTime());
           mEventNameTextView.setText(event.getName());
           mEventLocationTextView.setText(event.getLocation());
       }

   }

}
