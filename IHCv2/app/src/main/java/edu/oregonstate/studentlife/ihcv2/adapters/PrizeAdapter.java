package edu.oregonstate.studentlife.ihcv2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2.data.Prize;
import edu.oregonstate.studentlife.ihcv2.R;

/**
 * Created by Omeed on 2/12/18.
 * Manages the creation and setup of each element in the Prize page's RecyclerView
 */

public class PrizeAdapter extends RecyclerView.Adapter<PrizeAdapter.PrizeViewHolder> {

    private ArrayList<Prize> mPrizeList;
    private OnPrizeClickListener mOnPrizeClickListener;

    public PrizeAdapter(OnPrizeClickListener onPrizeClickListener) {
        mPrizeList = new ArrayList<Prize>();
        mOnPrizeClickListener = onPrizeClickListener;
    }

    /* ADD A PRIZE TO THE LIST OF PRIZE */
    public void addPrize(Prize prize) {
        mPrizeList.add(prize);
        notifyDataSetChanged();
    }

    /* GET THE NUMBER OF PRIZES DISPLAYED */
    @Override
    public int getItemCount() {
        return mPrizeList.size();
    }

    /* DECLARATION OF THE PRIZE CLICK LISTENER INTERFACE */
    public interface OnPrizeClickListener {
        void onPrizeClick(Prize prize);
    }

    /* CREATE THE PRIZE'S VIEWHOLDER */
    @Override
    public PrizeAdapter.PrizeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.prize_item, viewGroup, false);
        PrizeAdapter.PrizeViewHolder viewHolder = new PrizeAdapter.PrizeViewHolder(view);
        return viewHolder;
    }

    /* GET THE PRIZE AND BIND IT TO THE PRIZE'S VIEWHOLDER */
    @Override
    public void onBindViewHolder(PrizeViewHolder holder, int position) {
        Prize prize = mPrizeList.get(position);
        holder.bind(prize);
    }

    /* CLASS MANAGING EACH ELEMENT OF THE PRIZE LISTING */
    class PrizeViewHolder extends RecyclerView.ViewHolder {

        private TextView mPrizeNameTV;

        public PrizeViewHolder(View itemView) {
            super(itemView);
            mPrizeNameTV = (TextView) itemView.findViewById(R.id.tv_prize_name);

            mPrizeNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPrizeClickListener.onPrizeClick(mPrizeList.get(getAdapterPosition()));
                }
            });
        }

        void bind(Prize prize) {
            mPrizeNameTV.setText(prize.getName());
        }
    }
}
