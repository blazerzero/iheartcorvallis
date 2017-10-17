package cs496.yelpsearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cs496.yelpsearch.utils.YelpUtils;

/**
 * Created by beerad on 6/8/17.
 */

public class YelpSearchAdapter extends RecyclerView.Adapter<YelpSearchAdapter.SearchResultViewHolder> {
        private ArrayList<YelpUtils.SearchResult> mSearchResultsList;
        private OnSearchResultClickListener mSearchResultClickListener;

        public YelpSearchAdapter(OnSearchResultClickListener clickListener) {
            mSearchResultClickListener = clickListener;
        }

        public void updateSearchResults(ArrayList<YelpUtils.SearchResult> searchResultsList) {
            mSearchResultsList = searchResultsList;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if (mSearchResultsList != null) {
                return mSearchResultsList.size();
            } else {
                return 0;
            }
        }

        @Override
        public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.search_result_item, parent, false);
            return new SearchResultViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchResultViewHolder holder, int position) {
            holder.bind(mSearchResultsList.get(position));
        }

        public interface OnSearchResultClickListener {
            void onSearchResultClick(YelpUtils.SearchResult searchResult);
        }

        class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mSearchResultTV;
            private TextView mName;

            public SearchResultViewHolder(View itemView) {
                super(itemView);

                mName = (TextView)itemView.findViewById(R.id.tv_name);
                mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
                itemView.setOnClickListener(this);
            }

            public void bind(YelpUtils.SearchResult searchResult) {

                mName.setText(searchResult.name);
                mSearchResultTV.setText(searchResult.realAddress + "\nRating: " + searchResult.rating);
            }

            @Override
            public void onClick(View v) {
                YelpUtils.SearchResult searchResult = mSearchResultsList.get(getAdapterPosition());
                mSearchResultClickListener.onSearchResultClick(searchResult);
            }
        }
    }


