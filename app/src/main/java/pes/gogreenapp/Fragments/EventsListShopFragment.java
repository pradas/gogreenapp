package pes.gogreenapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pes.gogreenapp.Adapters.EventsListShopAdapter;
import pes.gogreenapp.Objects.Event;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

import static pes.gogreenapp.R.id.filtrarCategoriaEventos;
import static pes.gogreenapp.R.id.filtrarTodosEventos;
import static pes.gogreenapp.R.id.ordenarFechaEventos;
import static pes.gogreenapp.R.id.ordenarPuntosEventos;


public class EventsListShopFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EventsListShopAdapter adapter;
    String url = "http://10.4.41.145/api/";
    private SwipeRefreshLayout swipeContainer;
    private String TAG = "EventsList";
    private List<Event> events = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    String categorySelected = "";
    private TextView warning;
    private SessionManager session;
    private String pointsFilter = "nada";
    private String dateFilter = "nada";

    /**
     * Required empty public constructor
     */
    public EventsListShopFragment() {
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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.events_list_shop_fragment, container, false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.events_list_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case ordenarFechaEventos:
                ordenarFecha();
                return true;
            case ordenarPuntosEventos:
                ordenarPuntos();
                return true;
            case filtrarTodosEventos:
                filtrarTodos();
                return true;
            case filtrarCategoriaEventos:
                filtrarCategoria();
                return true;
        }
        return false;
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
        recyclerView = (RecyclerView) getView().findViewById(R.id.rv_events_shop);
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainerEventsShop);
        warning = (TextView) getView().findViewById(R.id.warningNoResultEventsShop);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        new GetEvents().execute(url + "events");
        new GetCategories().execute(url + "categories");
        // Refresh items
        swipeContainer.setOnRefreshListener(this::refreshItems);
    }

    private void filtrarCategoria() {
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
            List<Event> filteredEvents = filterEventsByCategories();
            if (filteredEvents.size() == 0)
                warning.setText("NO HAY PROMOCIONES EN ESTA CATEGORIA");
            else warning.setText("");
            adapter = new EventsListShopAdapter(getContext(), filteredEvents);
            recyclerView.setAdapter(adapter);
        });
        mBuilder.setNegativeButton("CANCELAR", (dialog, id) -> {
            categorySelected = pastCategory; // User cancelled the dialog
        });
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    private List<Event> filterEventsByCategories() {
        List<Event> rewardsFiltered = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getCategory().equals(categorySelected))
                rewardsFiltered.add(events.get(i));
        }
        return rewardsFiltered;
    }

    private void filtrarTodos() {
        categorySelected = "";
        warning.setText("");
        adapter = new EventsListShopAdapter(getContext(), events);
        recyclerView.setAdapter(adapter);

    }

    private void ordenarPuntos() {
        if (pointsFilter.equals("nada") || pointsFilter.equals("descendente")) {
            if (categorySelected.equals("")) {
                Collections.sort(events, (s1, s2) -> s1.getPoints().compareTo(s2.getPoints()));
                adapter = new EventsListShopAdapter(getContext(), events);
            } else {
                List<Event> filteredEvents = filterEventsByCategories();
                Collections.sort(filteredEvents, (s1, s2) -> s1.getPoints().compareTo(s2.getPoints()));
                adapter = new EventsListShopAdapter(getContext(), filteredEvents);
            }
            pointsFilter = "ascendente";
        } else if (pointsFilter.equals("ascendente")) {
            if (categorySelected.equals("")) {
                Collections.sort(events, (s1, s2) -> s2.getPoints().compareTo(s1.getPoints()));
                adapter = new EventsListShopAdapter(getContext(), events);
            } else {
                List<Event> filteredEvents = filterEventsByCategories();
                Collections.sort(filteredEvents, (s1, s2) -> s2.getPoints().compareTo(s1.getPoints()));
                adapter = new EventsListShopAdapter(getContext(), filteredEvents);
            }
            pointsFilter = "descendente";
        }
        recyclerView.setAdapter(adapter);

    }

    private void ordenarFecha() {
        if (dateFilter.equals("nada") || dateFilter.equals("descendente")) {
            if (categorySelected.equals("")) {
                Collections.sort(events, (s1, s2) -> s1.getDate().compareTo(s2.getDate()));
                adapter = new EventsListShopAdapter(getContext(), events);
            } else {
                List<Event> filteredEvents = filterEventsByCategories();
                Collections.sort(filteredEvents, (s1, s2) -> s1.getDate().compareTo(s2.getDate()));
                adapter = new EventsListShopAdapter(getContext(), filteredEvents);
            }
            dateFilter = "ascendente";
        } else if (dateFilter.equals("ascendente")) {
            if (categorySelected.equals("")) {
                Collections.sort(events, (s1, s2) -> s2.getDate().compareTo(s1.getDate()));
                adapter = new EventsListShopAdapter(getContext(), events);
            } else {
                List<Event> filteredEvents = filterEventsByCategories();
                Collections.sort(filteredEvents, (s1, s2) -> s2.getDate().compareTo(s1.getDate()));
                adapter = new EventsListShopAdapter(getContext(), filteredEvents);
            }
            dateFilter = "descendente";
        }
        recyclerView.setAdapter(adapter);

    }
    /**
     * On swipe, refresh all the items of the screen.
     */
    void refreshItems() {
        // Load items
        events.clear();
        warning.setText("");
        categorySelected = "";
        // Get items
        /* TODO fer crida a API només de events id tenda*/
        new GetEvents().execute("http://10.4.41.145/api/events");
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
                        Boolean favorite = false;
                        if (jsonObject.get("favourite") == "true") favorite = true;
                        events.add(
                                new Event(jsonObject.getInt("id"),
                                jsonObject.getString("title"),
                                jsonObject.getString("description"),
                                jsonObject.getInt("points"),
                                address,
                                company,
                                date,
                                image,
                                jsonObject.getString("category"),
                                        favorite)
                        );
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
            adapter = new EventsListShopAdapter(getContext(), events);
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
