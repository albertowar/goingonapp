package es.uma.goingonapp.news_list.holders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import es.uma.goingonapp.R;
import es.uma.goingonapp.common.entities.NewsThumbnail;
import es.uma.goingonapp.detailed_news.activities.DetailedNewsActivity;
import es.uma.goingonapp.news_list.adapter.NewsListAdapter;

/**
 * Manages the display and population of a single card of the list.
 * Holds the references to the view.
 */
public class NewsListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mHotNewsTitleTextView;
    private TextView mHotNewsContentTextView;
    private ImageView mHotNewsImageView;

    private NewsThumbnail mNewsThumbnail;

    private Context mContext;

    private NewsListAdapter mAdapter;

    public NewsListItemHolder(View itemView, NewsListAdapter adapter, Context context) {
        super(itemView);

        itemView.setOnClickListener(this);

        this.mAdapter = adapter;
        this.mContext = context;

        this.mHotNewsTitleTextView = (TextView)itemView.findViewById(R.id.hot_news_title);
        this.mHotNewsContentTextView = (TextView)itemView.findViewById(R.id.hot_news_content);
        this.mHotNewsImageView = (ImageView)itemView.findViewById(R.id.hot_news_image);
    }

    public void setHotNewsThumbnail(NewsThumbnail news) {
        this.mNewsThumbnail = news;

        this.mHotNewsTitleTextView.setText(this.mNewsThumbnail.getTitle());
        this.mHotNewsContentTextView.setText(this.mNewsThumbnail.getThumbnailContent());
    }

    @Override
    public void onClick(View v) {
        if (this.mNewsThumbnail != null) {
            Intent intent = new Intent(mContext, DetailedNewsActivity.class);
            intent.putExtra(NewsThumbnail.NEWS_EXTRA, this.mNewsThumbnail.getHrefUri());

            Log.d("Holder", "Request to: " + this.mNewsThumbnail.getHrefUri());

            mContext.startActivity(intent);
        }
    }
}