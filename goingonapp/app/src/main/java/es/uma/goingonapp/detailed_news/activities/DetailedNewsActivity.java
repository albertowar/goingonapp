package es.uma.goingonapp.detailed_news.activities;

import android.support.v4.app.Fragment;

import es.uma.goingonapp.common.activities.SingleFragmentActivity;
import es.uma.goingonapp.detailed_news.fragments.DetailedNewsFragment;

public class DetailedNewsActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new DetailedNewsFragment();
    }
}