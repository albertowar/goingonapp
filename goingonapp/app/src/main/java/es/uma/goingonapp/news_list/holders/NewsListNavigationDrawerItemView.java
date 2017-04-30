package es.uma.goingonapp.news_list.holders;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alb_Erc on 22/07/2015.
 */
public class NewsListNavigationDrawerItemView {
    private TextView title;
    private ImageView icon;

    public NewsListNavigationDrawerItemView(TextView title, ImageView icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }
}
