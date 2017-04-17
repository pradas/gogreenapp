package pes.gogreenapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pes.gogreenapp.R;
import pes.gogreenapp.Session.SessionManager;

/**
 * Created by Victor on 10/04/2017.
 */

public class Login extends AppCompatActivity {
    Button bLogin,bCancel;
    EditText tNombre,tContraseña;
    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        bLogin = (Button)findViewById(R.id.buttonLogin);
        tNombre = (EditText)findViewById(R.id.textNombre);
        tContraseña = (EditText)findViewById(R.id.textContraseña);

        bCancel = (Button)findViewById(R.id.buttonCancel);

        bLogin.setOnClickListener(v -> {
            //Falta llamar a API y coger token si es correcto,
            session.createLoginSession(tNombre.getText().toString(), "asdfALdkj1q20e3ijdF");
            // Staring MainActivity
            Intent i = new Intent(getApplicationContext(), RewardsList.class);
            startActivity(i);
            finish();
        });

        bCancel.setOnClickListener(v -> {
            finish();
        });
    }
}
