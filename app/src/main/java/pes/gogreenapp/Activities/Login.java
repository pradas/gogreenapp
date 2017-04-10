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

/**
 * Created by Victor on 10/04/2017.
 */

public class Login extends AppCompatActivity {
    Button bLogin,bCancel;
    EditText tNombre,tContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        bLogin = (Button)findViewById(R.id.buttonLogin);
        tNombre = (EditText)findViewById(R.id.textNombre);
        tContraseña = (EditText)findViewById(R.id.textContraseña);

        bCancel = (Button)findViewById(R.id.buttonCancel);

        bLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, RewardsList.class);
            startActivity(intent);
        });

        bCancel.setOnClickListener(v -> {
            finish();
        });
    }
}
