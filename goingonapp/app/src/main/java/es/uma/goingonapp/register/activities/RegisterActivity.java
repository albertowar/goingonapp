package es.uma.goingonapp.register.activities;

import android.support.v4.app.Fragment;

import es.uma.goingonapp.common.activities.SingleFragmentActivity;
import es.uma.goingonapp.register.fragments.RegisterFragment;

public class RegisterActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new RegisterFragment();
    }

    public static final int REGISTER_REQUEST = 1;
    public static final String USER_EXTRA = "USER_EXTRA";
}