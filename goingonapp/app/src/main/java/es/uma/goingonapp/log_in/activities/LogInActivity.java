package es.uma.goingonapp.log_in.activities;

import android.support.v4.app.Fragment;

import es.uma.goingonapp.common.activities.SingleFragmentActivity;
import es.uma.goingonapp.log_in.fragments.LogInFragment;

public class LogInActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new LogInFragment();
    }

    public static final int LOG_IN_REQUEST = 0;
    public static final String USER_EXTRA = "USER_EXTRA";
}