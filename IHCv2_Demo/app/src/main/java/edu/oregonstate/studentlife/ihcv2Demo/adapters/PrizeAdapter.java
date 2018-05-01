package edu.oregonstate.studentlife.ihcv2Demo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2Demo.data.Prize;
import edu.oregonstate.studentlife.ihcv2Demo.R;

/**
 * Created by Omeed on 2/12/18.
 */

public class PrizeAdapter extends RecyclerView.Adapter<PrizeAdapter.PrizeViewHolder> {

    private ArrayList<Prize> mPrizeList;
    private OnPrizeClickListener mOnPrizeClickListener;

    public PrizeAdapter(OnPrizeClickListener onPrizeClickListener) {
        mPrizeList = new ArrayList<Prize>();
        mOnPrizeClickListener = onPrizeClickListener;
    }

    public void addPrize(Prize prize) {
        mPrizeList.add(prize);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPrizeList.size();
    }

    public interface OnPrizeClickListener {
        void onPrizeClick(Prize prize);
    }

    @Override
    public PrizeAdapter.PrizeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.prize_item, viewGroup, false);
        PrizeAdapter.PrizeViewHolder viewHolder = new PrizeAdapter.PrizeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PrizeViewHolder holder, int position) {
        Prize prize = mPrizeList.get(position);
        holder.bind(prize);
    }

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
