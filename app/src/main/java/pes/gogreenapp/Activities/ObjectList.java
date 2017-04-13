package pes.gogreenapp.Activities;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;


import pes.gogreenapp.Fragments.RewardsList;
import pes.gogreenapp.R;


public class ObjectList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RewardsList())
                    .commit();
        }
    }
}

