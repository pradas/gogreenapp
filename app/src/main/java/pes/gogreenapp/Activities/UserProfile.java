package pes.gogreenapp.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pes.gogreenapp.Adapters.UserAdapter;
import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.user_creation_date;
import static pes.gogreenapp.R.id.user_email;
import static pes.gogreenapp.R.id.user_image;
import static pes.gogreenapp.R.id.user_name;
import static pes.gogreenapp.R.id.user_nickname;
import static pes.gogreenapp.R.id.user_points;

public class UserProfile extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    //no tiene que ser user adapter, sino dealadapter
    //UserAdapter adapter;
    User testUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        TextView userName = (TextView) findViewById(user_name);
        TextView userNickName = (TextView) findViewById(user_nickname);
        TextView userPoints = (TextView) findViewById(user_points);
        TextView userEmail = (TextView) findViewById(user_email);
        TextView userCreationDate = (TextView) findViewById(user_creation_date);
        ImageView userImage = (ImageView) findViewById(user_image);
        DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
        initializeUser();

        userName.setText(testUser.getName());
        userNickName.setText(testUser.getUsername());
        userPoints.setText(String.valueOf(testUser.getTotalPoints()));
        userCreationDate.setText((String) sourceFormat.format(testUser.getCreationDate()));
        //userCreationDate.setText(date);
        userEmail.setText(testUser.getEmail());
        //userImage.setImageResource();

        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void initializeUser(){
        testUser = new User ("realPepeViyuela", "Pepe Viyuela", "viyuela@gmail.com", "12-10-1983");

    }

    /*
    private void onItemsLoadComplete(){
        adapter.setUserInfo(testUser);
        adapter.notifyDataSetChanged();
    }*/
}
