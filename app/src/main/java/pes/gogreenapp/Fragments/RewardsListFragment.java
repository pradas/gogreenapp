package pes.gogreenapp.Fragments;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import pes.gogreenapp.Activities.ListActivity;
import pes.gogreenapp.Adapters.RewardsListAdapter;
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.orderDateButton;
import static pes.gogreenapp.R.id.orderPointsButton;
import static pes.gogreenapp.R.id.showAllButton;
import static pes.gogreenapp.R.id.showCategoriesButton;

/**
 * Created by Albert on 13/04/2017.
 */

public class RewardsListFragment extends Fragment {
    public static String ARG_REWARDS_LIST_NUMBER = "rewards_list_number";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RewardsListAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    String categorySelected = "";
    private String TAG = ListActivity.class.getSimpleName();
    private List<Reward> rewards = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    public RewardsListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rewards_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.rv);
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        new GetCategories().execute("http://10.4.41.145/api/categories");
        new GetRewards().execute("http://10.4.41.145/api/rewards");
        final Button endDate = (Button) getView().findViewById(orderDateButton);
        final Button points = (Button) getView().findViewById(orderPointsButton);
        final Button categoriesButton = (Button) getView().findViewById(showCategoriesButton);
        final Button allRewards = (Button) getView().findViewById(showAllButton);

        endDate.setOnClickListener(v -> {
            points.setText("PUNTOS");
            if ("FECHA ↓".equals(endDate.getText())) {
                if (categorySelected.equals("")) {
                    Collections.sort(rewards, (s1, s2) -> s1.getEndDate().compareTo(s2.getEndDate()));
                    adapter = new RewardsListAdapter(rewards);
                } else {
                    List<Reward> filteredRewards = filterRewardsByCategories();
                    Collections.sort(filteredRewards, (s1, s2) -> s1.getEndDate().compareTo(s2.getEndDate()));
                    adapter = new RewardsListAdapter(filteredRewards);
                }
                endDate.setText("FECHA ↑");
            } else if ("FECHA".equals(endDate.getText()) || "FECHA ↑".equals(endDate.getText())) {
                if (categorySelected.equals("")) {
                    Collections.sort(rewards, (s1, s2) -> s2.getEndDate().compareTo(s1.getEndDate()));
                    adapter = new RewardsListAdapter(rewards);
                } else {
                    List<Reward> filteredRewards = filterRewardsByCategories();
                    Collections.sort(filteredRewards, (s1, s2) -> s2.getEndDate().compareTo(s1.getEndDate()));
                    adapter = new RewardsListAdapter(filteredRewards);
                }
                endDate.setText("FECHA ↓");
            }
            recyclerView.setAdapter(adapter);
        });

        points.setOnClickListener(v -> {
            endDate.setText("FECHA");
            if ("PUNTOS ↓".equals(points.getText())) {
                if (categorySelected.equals("")) {
                    Collections.sort(rewards, (s1, s2) -> s1.getPoints().compareTo(s2.getPoints()));
                    adapter = new RewardsListAdapter(rewards);
                } else {
                    List<Reward> filteredRewards = filterRewardsByCategories();
                    Collections.sort(filteredRewards, (s1, s2) -> s1.getPoints().compareTo(s2.getPoints()));
                    adapter = new RewardsListAdapter(filteredRewards);
                }
                points.setText("PUNTOS ↑");
            } else if (("PUNTOS".equals(points.getText())) || ("PUNTOS ↑".equals(points.getText()))) {
                if (categorySelected.equals("")) {
                    Collections.sort(rewards, (s1, s2) -> s2.getPoints().compareTo(s1.getPoints()));
                    adapter = new RewardsListAdapter(rewards);
                } else {
                    List<Reward> filteredRewards = filterRewardsByCategories();
                    Collections.sort(filteredRewards, (s1, s2) -> s2.getPoints().compareTo(s1.getPoints()));
                    adapter = new RewardsListAdapter(filteredRewards);
                }
                points.setText("PUNTOS ↓");
            }
            recyclerView.setAdapter(adapter);
        });

        categoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pastCategory = categorySelected;
                categorySelected = "Conciertos";
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("SELECCIONA UNA CATEGORIA");
                int checkeds = 0;
                mBuilder.setSingleChoiceItems(categories.toArray(new String[categories.size()]), checkeds, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Al seleccionar una categoria
                        categorySelected = categories.toArray(new String[categories.size()])[which];
                    }
                });
                mBuilder.setPositiveButton("SELECCIONAR CATEGORIA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        List<Reward> filteredRewards = filterRewardsByCategories();
                        adapter = new RewardsListAdapter(filteredRewards);
                        recyclerView.setAdapter(adapter);
                    }
                });
                mBuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        categorySelected = pastCategory;
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        allRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySelected = "";
                adapter = new RewardsListAdapter(rewards);
                recyclerView.setAdapter(adapter);
            }
        });

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
        new GetRewards().execute("http://10.4.41.145/api/rewards");
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
            adapter = new RewardsListAdapter(rewards);
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
