package pes.gogreenapp.Activities;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.orderDateButton;
import static pes.gogreenapp.R.id.orderPointsButton;
import static pes.gogreenapp.R.id.showAllButton;
import static pes.gogreenapp.R.id.showCategoriesButton;

public class RewardsList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String categorySelected = "";
    private String TAG = RewardsList.class.getSimpleName();
    private List<Reward> rewards = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        new GetCategories().execute("http://10.4.41.145/api/categories");
        new GetRewards().execute("http://10.4.41.145/api/rewards");
        final Button endDate = (Button) findViewById(orderDateButton);
        final Button points = (Button) findViewById(orderPointsButton);
        final Button categoriesButton = (Button) findViewById(showCategoriesButton);
        final Button allRewards = (Button) findViewById(showAllButton);

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

        categoriesButton.setOnClickListener(v -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(RewardsList.this);
            mBuilder.setTitle("SELECCIONA UNA CATEGORIA");
            int checkeds = 0;
            mBuilder.setSingleChoiceItems(categories.toArray(new String[categories.size()]), checkeds, (dialog, which) -> {
                //Al seleccionar una categoria
                categorySelected = categories.toArray(new String[categories.size()])[which];
            });
            mBuilder.setPositiveButton("SELECCIONAR CATEGORIA", (dialog, id) -> {
                // User clicked OK button
                List<Reward> filteredRewards = filterRewardsByCategories();
                adapter = new RewardsAdapter(filteredRewards);
                recyclerView.setAdapter(adapter);
            });
            mBuilder.setNegativeButton("CANCELAR", (dialog, id) -> {
                categorySelected = "";
                // User cancelled the dialog
            });
            AlertDialog dialog = mBuilder.create();
            dialog.show();
        });
        allRewards.setOnClickListener(v -> {
            adapter = new RewardsAdapter(rewards);
            recyclerView.setAdapter(adapter);
        });
    }

    private List<Reward> filterRewardsByCategories() {
        List<Reward> rewardsFiltered = new ArrayList<>();
        for (int i = 0; i < rewards.size(); i++) {
            if (rewards.get(i).getCategory().equals(categorySelected))
                rewardsFiltered.add(rewards.get(i));
        }
        return rewardsFiltered;
    }

    private class GetRewards extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter = new RewardsAdapter(rewards);
            recyclerView.setAdapter(adapter);
        }
    }

    private class GetCategories extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
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
            }
            return null;
        }
    }
}

