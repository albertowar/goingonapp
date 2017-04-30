package es.uma.goingonapp.news_list.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.uma.goingonapp.R;
import es.uma.goingonapp.news_list.holders.NewsListNavigationDrawerItem;
import es.uma.goingonapp.news_list.holders.NewsListNavigationDrawerItemView;

/**
 * Created by Alb_Erc on 22/07/2015.
 */
public class NewsListNavigationDrawerAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<NewsListNavigationDrawerItem> items;

    public NewsListNavigationDrawerAdapter(Activity activity, ArrayList<NewsListNavigationDrawerItem> items) {
        super();

        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsListNavigationDrawerItemView itemView;

        LayoutInflater inflater = this.activity.getLayoutInflater();

        if (convertView == null) {
            NewsListNavigationDrawerItem item = this.items.get(position);
            convertView = inflater.inflate(R.layout.navigation_drawer_item, null);

            TextView titleView = (TextView) convertView.findViewById(R.id.navigation_drawer_item_title);
            titleView.setText(item.getTitle());
            ImageView iconView = (ImageView) convertView.findViewById(R.id.navigation_drawer_item_icon);
            iconView.setImageResource(item.getIcon());
            itemView = new NewsListNavigationDrawerItemView(titleView, iconView);
            convertView.setTag(itemView);
        }

        return convertView;
    }
}
