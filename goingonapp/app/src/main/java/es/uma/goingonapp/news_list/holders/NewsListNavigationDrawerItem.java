package es.uma.goingonapp.news_list.holders;

/**
 * Created by Alb_Erc on 22/07/2015.
 */
public class NewsListNavigationDrawerItem {
    private String title;
    private int icon;

    public NewsListNavigationDrawerItem(String title, int icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
