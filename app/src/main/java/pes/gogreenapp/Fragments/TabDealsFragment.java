package pes.gogreenapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pes.gogreenapp.Adapters.OfertasListAdapter;
import pes.gogreenapp.Objects.Oferta;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Adrian on 23/05/2017.
 */

public class TabDealsFragment extends Fragment {

    private SessionManager session;
    RecyclerView recyclerView;
    OfertasListAdapter adapter;
    String url = "http://10.4.41.145/api/";
    RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;
    private TextView warning;
    private String TAG = "OfertasList";
    private List<Oferta> deals = new ArrayList<>();

    /**
     * Required empty public constructor
     */
    public TabDealsFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_deals_fragment, container, false);
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = SessionManager.getInstance();
        recyclerView = (RecyclerView) getView().findViewById(R.id.rvDealsFavList);
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainerDealsFavList);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        new GetDeals().execute(url + "users/" + session.getUsername() + "/favourite-deals");
        warning = (TextView) getView().findViewById(R.id.warningNoResultDealsFavList);

        swipeContainer.setOnRefreshListener(this::refreshItems);
    }

    /**
     * On swipe, refresh all the items of the screen.
     */
    void refreshItems() {
        // Load items
        //rewards.clear();
        warning.setText("");
        // Get items
        new GetDeals().execute(url + "/users/" + session.getUsername() + "/favourite-deals");

        // Load complete
        onItemsLoadComplete();
    }

    /**
     * When the refresh is complete, set again the Adapter.
     */
    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        adapter.setEvents(deals);
        adapter.notifyDataSetChanged();

        // Stop refresh animation
        swipeContainer.setRefreshing(false);
        //adapter.getItemCount();
    }

    /**
     * Asynchronous Task for the petition GET the deal.
     */
    private class GetDeals extends AsyncTask<String, Void, Void> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param urls The parameters of the task.
         */
        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(),
                    session.getToken());
            Log.i(TAG, "Response from url: " + response);
            Log.i(TAG, urls[0]);
            if (response != null) {
                try {
                    JSONObject aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("deals");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Date date = null;
                        if (!jsonObject.isNull("date")) date = df.parse(jsonObject.getString("date"));
                        deals.add(
                                new Oferta(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("description"),
                                        jsonObject.getInt("value"),
                                        date, jsonObject.getBoolean("favourite"),
                                        jsonObject.getString("image"))

                        );
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /**
         * Creates the new Adapter and set the actual deal by the result obtained.
         *
         * @param result of doInBackground()
         */
        @Override
        protected void onPostExecute(Void result) {
            adapter = new OfertasListAdapter(getContext(), deals);
            recyclerView.setAdapter(adapter);
        }
    }
}
