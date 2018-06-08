package edu.oregonstate.studentlife.ihcv2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2.data.Constants;
import edu.oregonstate.studentlife.ihcv2.R;
import edu.oregonstate.studentlife.ihcv2.data.User;

/**
 * Created by dylan on 1/21/2018.
 * Manages the creation and setup of each element in the Leaderboard Page's RecyclerView
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private ArrayList<User.LeaderboardUser> mLeaderboardUserAccounts;

    public LeaderboardAdapter() {
        mLeaderboardUserAccounts = new ArrayList<User.LeaderboardUser>();
    }

    /* ADD A USER TO THE LEADERBOARD */
    public void addUserToLeaderboard(User.LeaderboardUser leaderboardUser) {
        mLeaderboardUserAccounts.add(leaderboardUser);
        notifyDataSetChanged();
    }

    /* GET THE NUMBER OF USERS DISPLAYED ON THE LEADERBOARD */
    @Override
    public int getItemCount() {
        return mLeaderboardUserAccounts.size();
    }

    /* CLASS MANAGING EACH ELEMENT OF THE LEADERBOARD LISTING */
    class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        private TextView mUserNameTextView;
        private TextView mStampCountTextView;
        private Constants constants;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);
            mUserNameTextView = (TextView) itemView.findViewById(R.id.tv_user_name);
            mStampCountTextView = (TextView) itemView.findViewById(R.id.tv_user_stamp_count);
        }

        /* BIND THE USER'S INFORMATION TO A LEADERBOARD LISTING */
        void bind(User.LeaderboardUser leaderboardUser) {
            mUserNameTextView.setText(leaderboardUser.getFirstName() + " " + leaderboardUser.getLastName());
            mStampCountTextView.setText(leaderboardUser.getStampCount());
        }

    }

    /* CREATE THE LEADERBOARD'S VIEWHOLDER */
    @Override
    public LeaderboardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.leaderboard_item, viewGroup, false);
        LeaderboardViewHolder viewHolder = new LeaderboardViewHolder(view);
        return viewHolder;
    }

    /* GET THE USER'S INFORMATION AND BIND IT TO THEIR LEADERBOARD LISTING */
    @Override
    public void onBindViewHolder(LeaderboardViewHolder holder, int position) {
        User.LeaderboardUser leaderboardUser = mLeaderboardUserAccounts.get(position);
        holder.bind(leaderboardUser);
    }
}
