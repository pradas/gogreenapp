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

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Adapters.EventsListAdapter;
import pes.gogreenapp.Objects.Event;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Adrian on 23/05/2017.
 */

public class TabEventsFragment extends Fragment {

    public static String ARG_EVENTS_LIST_NUMBER = "eventss_list_number";
    private SessionManager session;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;
    private List<Event> events = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    String url = "http://10.4.41.145/api/";
    private TextView warning;
    private String TAG = MainActivity.class.getSimpleName();
    EventsListAdapter adapter;

    /**
     * Required empty public constructor
     */
    public TabEventsFragment() {
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
        return inflater.inflate(R.layout.tab_events_fragment, container, false);
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
        session = SessionManager.getInstance();
        recyclerView = (RecyclerView) getView().findViewById(R.id.rvEventsFavList);
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainerEventsFavList);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        new GetCategories().execute(url + "categories");
        new GetEvents().execute(url + "users/" + session.getUsername() + "/favourite-events");
        warning = (TextView) getView().findViewById(R.id.warningNoResultEventsFavList);

        swipeContainer.setOnRefreshListener(this::refreshItems);
    }

    /**
     * On swipe, refresh all the items of the screen.
     */
    void refreshItems() {
        // Load items
        events.clear();
        warning.setText("");
        // Get items
        new GetEvents().execute(url + "users/" + session.getUsername() + "/favourite-events");
        Log.d(TAG, "setting events");

        // Load complete
        onItemsLoadComplete();
    }

    /**
     * When the refresh is complete, set again the Adapter.
     */
    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        adapter.setEvents(events);
        adapter.notifyDataSetChanged();

        // Stop refresh animation
        swipeContainer.setRefreshing(false);
        //adapter.getItemCount();
    }

    /**
     * Asynchronous Task for the petition GET of all the Events.
     */
    private class GetEvents extends AsyncTask<String, Void, Void> {

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
            if (response != null) {
                try {
                    JSONObject aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("events");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String address = null;
                        if (!jsonObject.isNull("adress")) address = jsonObject.getString("adress");
                        String company = null;
                        if (!jsonObject.isNull("company"))
                            address = jsonObject.getString("company");
                        String image = null;
                        if (!jsonObject.isNull("image"))
                            image = jsonObject.getString("image");
                        Date date = null;
                        if (!jsonObject.isNull("date")) date = df.parse(jsonObject.getString("date"));
                        events.add(
                                new Event(jsonObject.getInt("id"), jsonObject.getString("title"),
                                        jsonObject.getString("description"), jsonObject.getInt("points"),
                                        address, company, date, image, jsonObject.getString("category"),
                                        jsonObject.getBoolean("favourite")));
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
            adapter = new EventsListAdapter(getContext(), events);
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
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(),
                    session.getToken());
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
