package pes.gogreenapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import pes.gogreenapp.Fragments.FormUserFragment;
import pes.gogreenapp.R;

/**
 * Created by Jorge Alvarez on 22/04/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

       getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FormUserFragment())
                    .commit();
    }


}
