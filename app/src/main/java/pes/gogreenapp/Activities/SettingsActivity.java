package pes.gogreenapp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pes.gogreenapp.Fragments.SettingsFragment;
import pes.gogreenapp.R;

/**
 * Created by Albert on 13/04/2017.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_actitivity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SettingsFragment())
                    .commit();
        }
    }
}
