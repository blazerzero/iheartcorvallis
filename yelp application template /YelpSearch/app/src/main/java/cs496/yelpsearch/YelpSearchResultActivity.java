package cs496.yelpsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import cs496.yelpsearch.utils.YelpUtils;

public class YelpSearchResultActivity extends AppCompatActivity {

    private TextView mSearchResultNameTV;
    private TextView mSearchResultRatingTV;
    private TextView mSearchResultPhoneTV;
    private TextView mSearchResultAddressTV;
    private TextView mSearchResultUrlTV;
    private YelpUtils.SearchResult mSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp_search_result);

        mSearchResultNameTV = (TextView)findViewById(R.id.tv_search_result_name);
        mSearchResultRatingTV = (TextView)findViewById(R.id.tv_search_result_rating);
        mSearchResultPhoneTV = (TextView)findViewById(R.id.tv_search_result_phone);
        mSearchResultAddressTV = (TextView) findViewById(R.id.tv_search_result_address);
        mSearchResultUrlTV = (TextView) findViewById(R.id.tv_search_result_url);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(YelpUtils.SearchResult.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (YelpUtils.SearchResult)intent.getSerializableExtra(YelpUtils.SearchResult.EXTRA_SEARCH_RESULT);
            mSearchResultNameTV.setText(mSearchResult.name);
            mSearchResultRatingTV.setText(mSearchResult.rating + " (" + mSearchResult.review_count + " reviews)");
            mSearchResultPhoneTV.setText(mSearchResult.phone);

            String img = "<a href='" + mSearchResult.image_url + "'>Image</a>";
            mSearchResultUrlTV.setClickable(true);
            mSearchResultUrlTV.setMovementMethod(LinkMovementMethod.getInstance());
            mSearchResultUrlTV.setText(Html.fromHtml(img));

            mSearchResultAddressTV.setText(mSearchResult.realAddress);

        }
    }

}
