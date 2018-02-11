package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dylan on 1/21/2018.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private ArrayList<User.LeaderboardUser> mLeaderboardUserAccounts;

    public LeaderboardAdapter() {
        mLeaderboardUserAccounts = new ArrayList<User.LeaderboardUser>();
    }

    public void addUserToLeaderboard(User.LeaderboardUser leaderboardUser) {
        mLeaderboardUserAccounts.add(leaderboardUser);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mLeaderboardUserAccounts.size();
    }

    class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        //private ImageView mLeaderboardStampImageView;
        private TextView mUserNameTextView;
        private TextView mStampCountTextView;
        private Constants constants;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);
            //mPassportStampImageView = (ImageView) itemView.findViewById(R.id.iv_passport_stamp);
            mUserNameTextView = (TextView) itemView.findViewById(R.id.tv_user_name);
            mStampCountTextView = (TextView) itemView.findViewById(R.id.tv_user_stamp_count);
        }

        void bind(User.LeaderboardUser leaderboardUser) {
            // set to grab username?
            mUserNameTextView.setText(leaderboardUser.getFirstName() + " " + leaderboardUser.getLastName());
            // set way to grab number of stamps
            mStampCountTextView.setText(leaderboardUser.getStampCount());

        }

    }

    @Override
    public LeaderboardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.leaderboard_item, viewGroup, false);
        LeaderboardViewHolder viewHolder = new LeaderboardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LeaderboardViewHolder holder, int position) {
        User.LeaderboardUser leaderboardUser = mLeaderboardUserAccounts.get(position);
        holder.bind(leaderboardUser);
    }
}
