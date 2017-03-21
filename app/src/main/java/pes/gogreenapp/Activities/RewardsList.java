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

import static pes.gogreenapp.R.id.orderDateButton;

public class RewardsList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    JSONArray jsonArray = new JSONArray();
    Integer statusOrderDate = 0;
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
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDateRewards();
                adapter = new RewardsAdapter(rewards);
                recyclerView.setAdapter(adapter);
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
        /*
        for (int i = 0; i < rewards.size(); i++){
            System.out.println(rewards.get(i).getDate());
        }
        System.out.println("---------------");*/
        Collections.sort(rewards, new Comparator<Reward>() {
            public int compare(Reward s1, Reward s2) {
                if (statusOrderDate == 2 || statusOrderDate == 0) {
                    return s1.getDate().compareTo(s2.getDate());
                }
                else{
                    return s2.getDate().compareTo(s1.getDate());
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
            obj2.put("points", new Integer(100));
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
            obj3.put("points", new Integer(100));
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
