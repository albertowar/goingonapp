package es.uma.goingonapp.post_news.activities;

import android.support.v4.app.Fragment;

import es.uma.goingonapp.common.activities.SingleFragmentActivity;
import es.uma.goingonapp.post_news.fragments.PostNewsFragment;

/**
 * Created by Alb_Erc on 19/04/2015.
 */
public class PostNewsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PostNewsFragment();
    }
}
