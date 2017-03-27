package pes.gogreenapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    private List<Reward> rewards = new ArrayList<>();

    @Deprecated
    JSONArray jsonArray = new JSONArray();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        /* Se ha de cambiar cuando tengamos la llamada a la API
        ------------------------------------------------------ */
        initializeJSON();
        initializeRewards();
        /* ---------------------------------------------------- */

        adapter = new RewardsAdapter(rewards);
        recyclerView.setAdapter(adapter);

        final Button endDate = (Button) findViewById(orderDateButton);
        final Button points = (Button) findViewById(orderPointsButton);

        /**
         *
         */
        endDate.setOnClickListener(v -> {
            adapter = new RewardsAdapter(rewards);
            recyclerView.setAdapter(adapter);
            points.setText("PUNTOS");
            if ("FECHA ↓".equals(endDate.getText())) {
                Collections.sort(rewards, (s1, s2) -> s1.getEndDate().compareTo(s2.getEndDate()));
                endDate.setText("FECHA ↑");
            } else if ("FECHA".equals(endDate.getText()) || "FECHA ↑".equals(endDate.getText())) {
                Collections.sort(rewards, (s1, s2) -> s2.getEndDate().compareTo(s1.getEndDate()));
                endDate.setText("FECHA ↓");
            }
        });

        /**
         *
         */
        points.setOnClickListener(v -> {
            adapter = new RewardsAdapter(rewards);
            recyclerView.setAdapter(adapter);
            endDate.setText("FECHA");
            if ("PUNTOS ↓".equals(points.getText())) {
                Collections.sort(rewards, (s1, s2) -> s1.getPoints().compareTo(s2.getPoints()));
                points.setText("PUNTOS ↑");
            } else if (("PUNTOS".equals(points.getText())) || ("PUNTOS ↑".equals(points.getText()))) {
                Collections.sort(rewards, (s1, s2) -> s2.getPoints().compareTo(s1.getPoints()));
                points.setText("PUNTOS ↓");
            }
        });
    }

    @Deprecated
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

    @Deprecated
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
