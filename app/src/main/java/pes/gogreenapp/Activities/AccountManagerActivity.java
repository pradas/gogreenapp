package pes.gogreenapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pes.gogreenapp.Fragments.AboutUsFragment;
import pes.gogreenapp.Fragments.AccountManagerFragment;
import pes.gogreenapp.R;

public class AccountManagerActivity extends AppCompatActivity {

    /**
     * onCreate method to initialize the Activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_manager_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AccountManagerFragment())
                    .commit();
        }
    }
}
