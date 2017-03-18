package pes.gogreenapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RewardsList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private List<Reward> rewards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_list);

        recyclerView = (RecyclerView) findViewById(R.id.rv);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        initializeRewards();

        adapter = new RewardsAdapter(rewards);
        recyclerView.setAdapter(adapter);
    }

    private void initializeRewards() {
        rewards = new ArrayList<Reward>();
        rewards.add(new Reward("Title 1", "100", "2/2/2001", "Category 1"));
        rewards.add(new Reward("Title 1", "100", "2/2/2001", "Category 1"));
        rewards.add(new Reward("Title 1", "100", "2/2/2001", "Category 1"));
        rewards.add(new Reward("Title 1", "100", "2/2/2001", "Category 1"));
    }
}
