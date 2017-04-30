package es.uma.goingonapp.common.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import es.uma.goingonapp.R;

/**
 * Created by Alb_Erc on 18/04/2015.
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_single_fragment);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.activity_single_fragment);

        if (fragment == null) {
            fragment = createFragment();
            fragment.setRetainInstance(true);
            manager.beginTransaction()
                    .add(R.id.activity_single_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }

        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected abstract Fragment createFragment();
}
