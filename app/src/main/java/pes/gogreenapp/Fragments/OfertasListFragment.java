package pes.gogreenapp.Fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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


public class OfertasListFragment extends Fragment implements FilterDialogFragment.FilterDialogListener {

    //initialitions
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
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given
     *                           here.
     *
     * @return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        if (!((AppCompatActivity) getActivity()).getSupportActionBar().isShowing())
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        return inflater.inflate(R.layout.ofertas_list_fragment, container, false);
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link Activity#onCreateOptionsMenu(Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu The options menu in which you place your items.
     *
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_filters, menu);
        final MenuItem filterButton = menu.findItem(R.id.filter_icon);

        //Listener for the filter menuIcon
        filterButton.setOnMenuItemClickListener(v -> {
            FilterDialogFragment dialog = new FilterDialogFragment();
            dialog.setTargetFragment(this, 200);
            dialog.show(getFragmentManager(), "FilterDialogFragment");
            return true;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int filterId, int sorterId, int directionId,
                                      String category) {

        List<Oferta> auxEvents = new ArrayList<>(ofertas);

        switch (sorterId) {
            case R.id.radio_sorter_date:
                if (directionId == R.id.radio_sorter_ascendent) {
                    Collections.sort(auxEvents, (r1, r2) -> r1.getDate().compareTo(r2.getDate()));
                } else if (directionId == R.id.radio_sorter_descendent) {
                    Collections.sort(auxEvents, (r1, r2) -> r2.getDate().compareTo(r1.getDate()));
                }
                break;
            case R.id.radio_sorter_points:
                if (directionId == R.id.radio_sorter_ascendent) {
                    Collections.sort(auxEvents, (r1, r2) -> r1.getPoints().compareTo(r2.getPoints()));
                } else if (directionId == R.id.radio_sorter_descendent) {
                    Collections.sort(auxEvents, (r1, r2) -> r2.getPoints().compareTo(r1.getPoints()));
                }
                break;
            default:
                break;
        }

        // change the rewards list info of the adapter
        adapter.setEvents(auxEvents);
        adapter.notifyDataSetChanged();

        //close the dialog
        dialog.getDialog().cancel();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.getDialog().cancel();
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
     * Asynchronous Task for the petition GET the deal.
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
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(), session.getToken());
            Log.i(TAG, "Response from url: " + response);
            Log.i(TAG, urls[0]);
            ofertas.clear();
            if (response != null) {
                try {
                    JSONObject aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("deals");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Date date = null;
                        if (!jsonObject.isNull("date")) date = df.parse(jsonObject.getString("date"));
                        String image = null;
                        if (!jsonObject.isNull("image")) image = jsonObject.getString("image");
                        ofertas.add(new Oferta(jsonObject.getInt("id"), jsonObject.getString("name"),
                                jsonObject.getString("description"), jsonObject.getInt("value"), date,
                                jsonObject.getBoolean("favourite"), image)

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

            adapter = new OfertasListAdapter(getContext(), ofertas);
            recyclerView.setAdapter(adapter);
        }
    }
}
