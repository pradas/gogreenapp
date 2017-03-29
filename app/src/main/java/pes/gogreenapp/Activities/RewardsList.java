package pes.gogreenapp.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import pes.gogreenapp.Handlers.HttpHandler;
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
    private String url = "http://10.4.41.145/api/rewards";
    private String TAG = RewardsList.class.getSimpleName();

    @Deprecated
    JSONArray jsonArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        new GetRewards().execute(url);

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

    private class GetRewards extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(url);
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
}
