package pes.gogreenapp.Fragments;

import android.support.v4.app.Fragment;
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
import java.util.HashMap;
import java.util.List;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Adapters.RewardsListAdapter;
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.orderDateButton;
import static pes.gogreenapp.R.id.orderPointsButton;
import static pes.gogreenapp.R.id.showAllButton;
import static pes.gogreenapp.R.id.showCategoriesButton;

public class RewardsListFragment extends Fragment {
    public static String ARG_REWARDS_LIST_NUMBER = "rewards_list_number";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RewardsListAdapter adapter;
    String categorySelected = "";
    private SwipeRefreshLayout swipeContainer;
    private String TAG = MainActivity.class.getSimpleName();
    private List<Reward> rewards = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    /**
     * Required empty public constructor
     */
    public RewardsListFragment() {
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in
     *                           the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI
     *                           should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rewards_list_fragment, container, false);
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
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

        // Listener for the Date order button
        endDate.setOnClickListener(v -> {
            points.setText(R.string.points_order_button);
            if ("FECHA ↓".equals(endDate.getText())) {
                if (categorySelected.equals("")) {
                    Collections.sort(rewards, (s1, s2) -> s1.getEndDate().compareTo(s2.getEndDate()));
                    adapter = new RewardsListAdapter(rewards);
                } else {
                    List<Reward> filteredRewards = filterRewardsByCategories();
                    Collections.sort(filteredRewards, (s1, s2) -> s1.getEndDate().compareTo(s2.getEndDate()));
                    adapter = new RewardsListAdapter(filteredRewards);
                }
                endDate.setText(R.string.date_ascendant_order_button);
            } else if ("FECHA".equals(endDate.getText()) || "FECHA ↑".equals(endDate.getText())) {
                if (categorySelected.equals("")) {
                    Collections.sort(rewards, (s1, s2) -> s2.getEndDate().compareTo(s1.getEndDate()));
                    adapter = new RewardsListAdapter(rewards);
                } else {
                    List<Reward> filteredRewards = filterRewardsByCategories();
                    Collections.sort(filteredRewards, (s1, s2) -> s2.getEndDate().compareTo(s1.getEndDate()));
                    adapter = new RewardsListAdapter(filteredRewards);
                }
                endDate.setText(R.string.date_descendent_order_button);
            }
            recyclerView.setAdapter(adapter);
        });

        // Listener for the Points order button.
        points.setOnClickListener(v -> {
            endDate.setText(R.string.date_order_button);
            if ("PUNTOS ↓".equals(points.getText())) {
                if (categorySelected.equals("")) {
                    Collections.sort(rewards, (s1, s2) -> s1.getPoints().compareTo(s2.getPoints()));
                    adapter = new RewardsListAdapter(rewards);
                } else {
                    List<Reward> filteredRewards = filterRewardsByCategories();
                    Collections.sort(filteredRewards, (s1, s2) -> s1.getPoints().compareTo(s2.getPoints()));
                    adapter = new RewardsListAdapter(filteredRewards);
                }
                points.setText(R.string.points_ascendent_order_button);
            } else if (("PUNTOS".equals(points.getText())) || ("PUNTOS ↑".equals(points.getText()))) {
                if (categorySelected.equals("")) {
                    Collections.sort(rewards, (s1, s2) -> s2.getPoints().compareTo(s1.getPoints()));
                    adapter = new RewardsListAdapter(rewards);
                } else {
                    List<Reward> filteredRewards = filterRewardsByCategories();
                    Collections.sort(filteredRewards, (s1, s2) -> s2.getPoints().compareTo(s1.getPoints()));
                    adapter = new RewardsListAdapter(filteredRewards);
                }
                points.setText(R.string.points_descendent_order_button);
            }
            recyclerView.setAdapter(adapter);
        });

        //Listener for the Categories filer button.
        categoriesButton.setOnClickListener(v -> {
            String pastCategory = categorySelected;
            categorySelected = "Conciertos";
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("SELECCIONA UNA CATEGORIA");
            int checkeds = 0;
            mBuilder.setSingleChoiceItems(categories.toArray(new String[categories.size()]),
                    checkeds, (dialog, which) -> {
                        categorySelected = categories.toArray(new String[categories.size()])[which];
                    }).setPositiveButton("SELECCIONAR CATEGORIA", (dialog, id) -> {
                // User clicked OK button
                List<Reward> filteredRewards = filterRewardsByCategories();
                adapter = new RewardsListAdapter(filteredRewards);
                recyclerView.setAdapter(adapter);
            });
            mBuilder.setNegativeButton("CANCELAR", (dialog, id) -> {
                categorySelected = pastCategory; // User cancelled the dialog
            });
            AlertDialog dialog = mBuilder.create();
            dialog.show();
        });

        //Listener for the All Rewards filter button.
        allRewards.setOnClickListener(v -> {
            categorySelected = "";
            adapter = new RewardsListAdapter(rewards);
            recyclerView.setAdapter(adapter);
        });

        // Refresh items
        swipeContainer.setOnRefreshListener(this::refreshItems);
    }

    /**
     * On swipe, refresh all the items of the screen.
     */
    void refreshItems() {
        // Load items
        rewards.clear();

        // Get items
        new GetRewards().execute("http://10.4.41.145/api/rewards");

        // Load complete
        onItemsLoadComplete();
    }

    /**
     * When the refresh is complete, set again the Adapter.
     */
    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        adapter.setRewards(rewards);
        adapter.notifyDataSetChanged();

        // Stop refresh animation
        swipeContainer.setRefreshing(false);
    }

    /**
     * Filter the rewards by the category clicked on the Category filter button.
     *
     * @return the List of rewards with the filter applied.
     */
    private List<Reward> filterRewardsByCategories() {
        List<Reward> rewardsFiltered = new ArrayList<>();
        for (int i = 0; i < rewards.size(); i++) {
            if (rewards.get(i).getCategory().equals(categorySelected))
                rewardsFiltered.add(rewards.get(i));
        }
        return rewardsFiltered;
    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class GetRewards extends AsyncTask<String, Void, Void> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param urls The parameters of the task.
         */
        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>());
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
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /**
         * Creates the new Adapter and set the actual rewards by the result obtained.
         *
         * @param result of doInBackground()
         */
        @Override
        protected void onPostExecute(Void result) {
            adapter = new RewardsListAdapter(rewards);
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Asynchronous Task for the petition GET of all the Categories.
     */
    private class GetCategories extends AsyncTask<String, Void, Void> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param urls The parameters of the task.
         */
        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>());
            Log.i(TAG, "Response from url: " + response);
            if (response != null) {
                JSONObject aux;
                try {
                    aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        categories.add((String) jsonObject.get("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
