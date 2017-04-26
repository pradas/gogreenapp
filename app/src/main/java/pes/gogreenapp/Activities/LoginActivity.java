package pes.gogreenapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pes.gogreenapp.Fragments.LoginFragment;
import pes.gogreenapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_login, new LoginFragment())
                    .commit();
        }
    }
}
