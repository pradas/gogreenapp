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

import pes.gogreenapp.Adapters.OfertasListAdapter;
import pes.gogreenapp.Objects.Oferta;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

import static pes.gogreenapp.R.id.ordenarFechaOfertas;
import static pes.gogreenapp.R.id.ordenarPuntosOfertas;


public class OfertasListFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    OfertasListAdapter adapter;
    String url = "http://10.4.41.145/api/";
    private SwipeRefreshLayout swipeContainer;
    private String TAG = "OfertasList";
    private List<Oferta> ofertas = new ArrayList<>();
    String categorySelected = "";
    private TextView warning;
    private SessionManager session;
    private String pointsFilter = "nada";
    private String dateFilter = "nada";

    /**
     * Required empty public constructor
     */
    public OfertasListFragment() {
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
        return inflater.inflate(R.layout.ofertas_list_fragment, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ofertas_list_menu, menu);

        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case ordenarFechaOfertas:
                ordenarFecha();
                return true;
            case ordenarPuntosOfertas:
                ordenarPuntos();
                return true;
        }
        return false;
    }


    private void ordenarPuntos() {
        if (pointsFilter.equals("nada") || pointsFilter.equals("descendente")) {
                Collections.sort(ofertas, (s1, s2) -> s1.getPoints().compareTo(s2.getPoints()));
                adapter = new OfertasListAdapter(getContext(), ofertas);
            pointsFilter = "ascendente";
        } else if (pointsFilter.equals("ascendente")) {
            Collections.sort(ofertas, (s1, s2) -> s2.getPoints().compareTo(s1.getPoints()));
                adapter = new OfertasListAdapter(getContext(), ofertas);
            pointsFilter = "descendente";
        }
        recyclerView.setAdapter(adapter);

    }

    private void ordenarFecha() {
        if (dateFilter.equals("nada") || dateFilter.equals("descendente")) {
            Collections.sort(ofertas, (s1, s2) -> s1.getDate().compareTo(s2.getDate()));
                adapter = new OfertasListAdapter(getContext(), ofertas);
            dateFilter = "ascendente";
        } else if (dateFilter.equals("ascendente")) {
                Collections.sort(ofertas, (s1, s2) -> s2.getDate().compareTo(s1.getDate()));
                adapter = new OfertasListAdapter(getContext(), ofertas);
            dateFilter = "descendente";
        }
        recyclerView.setAdapter(adapter);

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
        recyclerView = (RecyclerView) getView().findViewById(R.id.rv_ofertas);
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainerOfertas);
        warning = (TextView) getView().findViewById(R.id.warningNoResultOfertas);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        new GetOfertas().execute(url + "deals");
        // Refresh items
        swipeContainer.setOnRefreshListener(this::refreshItems);
    }
    /**
     * On swipe, refresh all the items of the screen.
     */
    void refreshItems() {
        // Load items
        ofertas.clear();
        warning.setText("");
        categorySelected = "";
        // Get items
        new GetOfertas().execute("http://10.4.41.145/api/deals");
        Log.d(TAG, "setting ofertas");

        // Load complete
        onItemsLoadComplete();
    }

    /**
     * When the refresh is complete, set again the Adapter.
     */
    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        adapter.setEvents(ofertas);
        adapter.notifyDataSetChanged();

        // Stop refresh animation
        swipeContainer.setRefreshing(false);
        //adapter.getItemCount();
    }

    /**
     * Asynchronous Task for the petition GET of all the Events.
     */
    private class GetOfertas extends AsyncTask<String, Void, Void> {

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
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Date date = null;
                        if (!jsonObject.isNull("date")) date = df.parse(jsonObject.getString("date"));
                        ofertas.add(
                                new Oferta(
                                        jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("description"),
                                jsonObject.getInt("value"),
                                date)

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
            adapter = new OfertasListAdapter(getContext(), ofertas);
            recyclerView.setAdapter(adapter);
        }
    }
}
