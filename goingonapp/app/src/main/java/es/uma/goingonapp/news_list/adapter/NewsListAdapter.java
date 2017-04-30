package es.uma.goingonapp.news_list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import es.uma.goingonapp.R;
import es.uma.goingonapp.common.entities.NewsThumbnail;
import es.uma.goingonapp.news_list.holders.NewsListItemHolder;

/**
 * This class manages the cards list display and data management.
 * Moreover, it manages the interaction between the card and the user.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListItemHolder> {

    private ArrayList<NewsThumbnail> mHotNewsList;

    public NewsListAdapter() {
        this.mHotNewsList = new ArrayList<NewsThumbnail>();
    }

    @Override
    public NewsListItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.view_hot_news_item, container, false);

        return new NewsListItemHolder(root, this, container.getContext());
    }

    /**
     * The callback to perform when the user is scrolling and reaches a certain position.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(NewsListItemHolder holder, int position) {
        NewsThumbnail hotNews = this.mHotNewsList.get(position);

        holder.setHotNewsThumbnail(hotNews);
    }

    @Override
    public int getItemCount() {
        return mHotNewsList.size();
    }

    /*
     * Inserting a new item at the head of the list. This uses a specialized
     * RecyclerView method, notifyItemInserted(), to trigger any enabled item
     * animations in addition to updating the view.
     */
    public void addItem(int position, NewsThumbnail hotNews) {
        if (position <= this.mHotNewsList.size()) {
            this.mHotNewsList.add(position, hotNews);
            this.notifyItemInserted(position);
        }
    }

    public boolean containsItem(NewsThumbnail hotNews)
    {
        return this.mHotNewsList.contains(hotNews);
    }
}
