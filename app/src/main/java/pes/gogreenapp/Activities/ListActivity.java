package pes.gogreenapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pes.gogreenapp.Fragments.RewardsListFragment;
import pes.gogreenapp.R;


public class ListActivity extends AppCompatActivity {

    /**
     * onCreate method that set the RewardListFragment into this Activity.
     *
     * @param savedInstanceState last functional state of this activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RewardsListFragment())
                    .commit();
        }
    }
}

