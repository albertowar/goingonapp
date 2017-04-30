package es.uma.goingonapp.news_list.activities;

import android.support.v4.app.Fragment;

import es.uma.goingonapp.common.activities.SingleFragmentActivity;
import es.uma.goingonapp.news_list.fragments.NewsListFragment;

/**
 * This Activity will hold the Fragment to display the list of HotNews in mobile apps
 */
public class NewsListActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new NewsListFragment();
    }
}
