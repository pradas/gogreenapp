package pes.gogreenapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RewardsList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    JSONArray jsonArray = new JSONArray();
    private List<Reward> rewards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_list);

        recyclerView = (RecyclerView) findViewById(R.id.rv);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //initializeJSON();
        //initializeRewards();

        rewards = new ArrayList<>();
        rewards.add(new Reward(1, "A", 1, "A", "A"));
        adapter = new RewardsAdapter(rewards);
        recyclerView.setAdapter(adapter);
    }

    private void initializeRewards() {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                rewards.add(new Reward((Integer) jsonObject.get("id"),
                        (String) jsonObject.get("title"), (Integer) jsonObject.get("points"),
                        (String) jsonObject.get("date"), (String) jsonObject.get("category")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        jsonArray.put(obj);
        jsonArray.put(obj);
        jsonArray.put(obj);
        jsonArray.put(obj);
        jsonArray.put(obj);
        jsonArray.put(obj);
    }
}
