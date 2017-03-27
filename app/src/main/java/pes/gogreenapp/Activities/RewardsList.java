package pes.gogreenapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pes.gogreenapp.Adapters.RewardsAdapter;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.favoriteButton;
import static pes.gogreenapp.R.id.orderDateButton;
import static pes.gogreenapp.R.id.orderPointsButton;

public class RewardsList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    JSONArray jsonArray = new JSONArray();
    Integer statusOrderDate = 0;
    Integer statusOrderPoints = 0;
    private List<Reward> rewards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        initializeJSON();
        initializeRewards();

        adapter = new RewardsAdapter(rewards);
        recyclerView.setAdapter(adapter);

        final Button fecha = (Button) findViewById(orderDateButton);
        final Button points = (Button) findViewById(orderPointsButton);
        final ImageButton fav = (ImageButton) findViewById(favoriteButton);

        fecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter = new RewardsAdapter(rewards);
                    recyclerView.setAdapter(adapter);
                    statusOrderPoints = 0;
                    points.setText("PUNTOS");
                    orderDateRewards();
                    if (statusOrderDate == 0){
                        statusOrderDate = 1;
                        fecha.setText("FECHA ↑");
                    }else if (statusOrderDate == 1){
                        statusOrderDate = 2;
                        fecha.setText("FECHA ↓");
                    }else{
                        statusOrderDate = 1;
                        fecha.setText("FECHA ↑");
                    }
                }
        });
        points.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderPointsRewards();
                    adapter = new RewardsAdapter(rewards);
                    recyclerView.setAdapter(adapter);
                    statusOrderDate = 0;
                    fecha.setText("FECHA");
                    if (statusOrderPoints == 0){
                        statusOrderPoints = 1;
                        points.setText("PUNTOS ↑");
                    }else if (statusOrderPoints == 1){
                        statusOrderPoints = 2;
                        points.setText("PUNTOS ↓");
                    }else{
                        statusOrderPoints = 1;
                        points.setText("PUNTOS ↑");
                    }
                }
        });
    }

    private void initializeRewards() {
        JSONObject jsonObject;
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date d = sourceFormat.parse((String) jsonObject.get("date"));
                rewards.add(new Reward((Integer) jsonObject.get("id"),
                        (String) jsonObject.get("title"), (Integer) jsonObject.get("points"),
                        (Date) d, (String) jsonObject.get("category")));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void orderDateRewards() {
        //
        /*
        for (int i = 0; i < rewards.size(); i++){
            System.out.println(rewards.get(i).getDate());
        }
        System.out.println("---------------");*/
        Collections.sort(rewards, new Comparator<Reward>() {
            public int compare(Reward s1, Reward s2) {
                if (statusOrderDate == 2 || statusOrderDate == 0) {
                    return s1.getEndDate().compareTo(s2.getEndDate());
                }
                else{
                    return s2.getEndDate().compareTo(s1.getEndDate());
                }
            }
        });/*
        for (int i = 0; i < rewards.size(); i++){
            System.out.println(rewards.get(i).getDate());
        }*/
    }
    private void orderPointsRewards() {
        /*
        for (int i = 0; i < rewards.size(); i++){
            System.out.println(rewards.get(i).getDate());
        }
        System.out.println("---------------");*/
        Collections.sort(rewards, new Comparator<Reward>() {
            public int compare(Reward s1, Reward s2) {
                if (statusOrderPoints == 2 || statusOrderPoints == 0) {
                    return s1.getPoints().compareTo(s2.getPoints());
                }
                else{
                    return s2.getPoints().compareTo(s1.getPoints());
                }
            }
        });/*
        for (int i = 0; i < rewards.size(); i++){
            System.out.println(rewards.get(i).getDate());
        }*/
    }

    private void initializeJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", new Integer(1));
            obj.put("title", "reward 1");
            obj.put("points", new Integer(100));
            obj.put("date", "01/01/2018");
            obj.put("category", "category1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(obj);
        JSONObject obj2 = new JSONObject();
        try {
            obj2.put("id", new Integer(1));
            obj2.put("title", "reward 2");
            obj2.put("points", new Integer(200));
            obj2.put("date", "11/7/2008");
            obj2.put("category", "category1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(obj2);
        JSONObject obj3 = new JSONObject();
        try {
            obj3.put("id", new Integer(1));
            obj3.put("title", "reward 3");
            obj3.put("points", new Integer(300));
            obj3.put("date", "31/11/2018");
            obj3.put("category", "category1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(obj3);
        jsonArray.put(obj2);
        jsonArray.put(obj);
        jsonArray.put(obj3);
        jsonArray.put(obj2);
    }
}
