package es.uma.goingonapp.detailed_news.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.ParseException;

import es.uma.goingonapp.R;
import es.uma.goingonapp.common.entities.News;
import es.uma.goingonapp.common.entities.NewsThumbnail;
import es.uma.goingonapp.common.network.NewsProxy;

/**
 * Created by Alb_Erc on 18/04/2015.
 */
public class DetailedNewsFragment extends Fragment implements Response.Listener<News>, Response.ErrorListener{
    private ImageView mNewsImageView;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private TextView mAuthorTextView;
    private TextView mDateTextView;

    private String mNewsUri;

    private NewsProxy mNewsProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mNewsUri = this.getActivity().getIntent().getExtras().getString(NewsThumbnail.NEWS_EXTRA);

        if (this.mNewsUri == null) {
            throw new IllegalArgumentException("The fragment should have been created with an URI");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_news, container, false);

        this.mNewsImageView = (ImageView)view.findViewById(R.id.detailed_news_image);
        this.mTitleTextView = (TextView)view.findViewById(R.id.detailed_news_title);
        this.mContentTextView = (TextView)view.findViewById(R.id.detailed_news_content);
        this.mAuthorTextView = (TextView)view.findViewById(R.id.detailed_news_author);
        this.mDateTextView = (TextView)view.findViewById(R.id.detailed_news_date);

        this.mNewsProxy = new NewsProxy(this.getActivity());
        this.mNewsProxy.getNews(this.mNewsUri, this, this);

        this.setHasOptionsMenu(true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        this.mNewsProxy.cancelRequests();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("DEBUG", "There was an error during the request: " + error.getMessage());
    }

    @Override
    public void onResponse(News response) {
        this.mTitleTextView.setText(response.getTitle());
        this.mContentTextView.setText(response.getContent());
        this.mAuthorTextView.setText(response.getAuthor());

        try {
            this.mDateTextView.setText(response.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
