package pes.gogreenapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pes.gogreenapp.Fragments.RegisterFragment;
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
                    .add(R.id.container_login, new RegisterFragment())
                    .commit();
    }


}
