package pes.gogreenapp.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pes.gogreenapp.Adapters.RewardsExchangedAdapter;
import pes.gogreenapp.Adapters.RewardsExchangedAdapter;
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.R;

/**
 * Created by Adry on 07/04/2017.
 */

public class RewardsExchanged extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RewardsExchangedAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private String TAG = RewardsExchanged.class.getSimpleName();
    private List<Reward> rewards = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_exchanged);
        recyclerView = (RecyclerView) findViewById(R.id.rvExchanged);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainerExchanged);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        new GetCategories().execute("http://10.4.41.145/api/categories");
        new GetRewards().execute("http://10.4.41.145/api/rewards");

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
    }

    void refreshItems() {
        // Load items
            rewards.clear();
            new RewardsExchanged.GetRewards().execute("http://10.4.41.145/api/rewards");
            // Load complete
            onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...
        adapter.setRewards(rewards);
        adapter.notifyDataSetChanged();
        // Stop refresh animation
        swipeContainer.setRefreshing(false);
    }

    private class GetRewards extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            /*HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0]);
            Log.i(TAG, "Response from url: " + response);
            if (response != null) {
                try {
                    JSONObject aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("rewards");
                    System.out.println(jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Date d = df.parse((String) jsonObject.get("end_date"));
                        rewards.add(new Reward((Integer) jsonObject.get("id"),
                                (String) jsonObject.get("title"), (Integer) jsonObject.get("points"),
                                d, (String) jsonObject.get("category")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter = new RewardsExchangedAdapter(rewards);
            recyclerView.setAdapter(adapter);
        }
    }

    private class GetCategories extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            /*HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0]);
            Log.i(TAG, "Response from url: " + response);
            if (response != null) {
                JSONObject aux = null;
                try {
                    aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        categories.add((String) jsonObject.get("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }*/
            return null;
        }
    }

}
