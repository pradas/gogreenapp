package pes.gogreenapp.Fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

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


import pes.gogreenapp.Adapters.EventsListShopAdapter;
import pes.gogreenapp.Adapters.GivePointsByEventsAdapter;
import pes.gogreenapp.Adapters.GivePointsByPointsAdapter;

import pes.gogreenapp.Objects.Event;


import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Adrian on 11/05/2017.
 */

public class GivePointsFragment extends Fragment {

    private SessionManager session;
    private String modeItems;
    private ListView listToGivePoints;
    private List<String> users;
    private List<Event> events;
    private Switch mode;
    private Button grantPoints;
    private GivePointsByEventsAdapter adapterEvents;
    private GivePointsByPointsAdapter adapterPoints;
    private String TAG = "EventsList";

    public GivePointsFragment() {
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
        // Inflate the layout for this fragment
        modeItems = "Eventos";
        users = new ArrayList<String>();
        events = new ArrayList<Event>();
        users.add("Usuario nº1");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.give_points_fragment, container, false);
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
        getActivity().setTitle("Dar Puntos");
        session = SessionManager.getInstance();
        new GetEvents().execute("http://10.4.41.145/api/shops/" + session.getShopId() + "/events");
        mode = (Switch) getView().findViewById(R.id.switchModeItem) ;
        listToGivePoints = (ListView) getView().findViewById(R.id.listViewGivePoints);
        grantPoints = (Button) getView().findViewById(R.id.grantPointsToUsers);

        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    modeItems = "Eventos";
                    users.clear();
                    users.add("Usuario nº1");
                    adapterEvents = new GivePointsByEventsAdapter(getContext(), users, events);
                    listToGivePoints.setAdapter(adapterEvents);
                }
                else {
                    modeItems = "Puntos";
                    users.clear();
                    users.add("Usuario nº1");
                    adapterPoints = new GivePointsByPointsAdapter(getContext(), users);
                    listToGivePoints.setAdapter(adapterPoints);
                }
            }
        });

        grantPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.givePoints)
                        .setPositiveButton(R.string.givePointsAlertButton, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (modeItems.equals("Eventos")) {
                                    List<String> userNames = adapterEvents.getUserNames();
                                    List<Event> eventsSpinneds = adapterEvents.getEvents();
                                    for (int i = 0; i < userNames.size(); ++ i) {
                                        new PutUser().execute("http://10.4.41.145/api/users/", "PUT",
                                                (String) String.valueOf(eventsSpinneds.get(i).getPoints()),
                                                userNames.get(i));
                                    }
                                }
                                else {
                                    List<String> userNames = adapterPoints.getUserNames();
                                    List<String> points = adapterPoints.getPoints();
                                    for (int i = 0; i < userNames.size(); ++ i) {
                                        new PutUser().execute("http://10.4.41.145/api/users/", "PUT",
                                                points.get(i), userNames.get(i));
                                    }
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_give_points, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_user:
                users.add("Usuario nº" + (users.size() + 1));
                if (modeItems.equals("Eventos"))
                    adapterEvents.notifyDataSetChanged();
                else
                    adapterPoints.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            Log.d(TAG, urls[0]);
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
                        events.add(new Event(jsonObject.getString("title"), jsonObject.getInt("points"))
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /**
         * Creates the new Adapter and set the actual events by the result obtained.
         *
         * @param result of doInBackground()
         */
        @Override
        protected void onPostExecute(Void result) {
            adapterEvents = new GivePointsByEventsAdapter(getContext(), users, events);
            listToGivePoints.setAdapter(adapterEvents);
        }
    }


        /**
     * Asynchronous Task for the petition PUT of a User.
     */
    private class PutUser extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the method petition,
         *               params[2] is the number of points to add to the user
         *               params[3] is the username of the user
         * @return "Error" if the method fails, "Correct" if the method works, other if the user doesn't exixts
         */
        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("points", params[2]);
            String url = params [0] + params[3];
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null && !response.equals("500") && !response.equals("404")) {
                return "Correct";
            }
            else if (response.equals("404")) return "El usuario " + params[3] + " no existe";
            return "Error";
        }

        /**
         * Called when doInBackground is finished.
         *
         * @param result Makes a toast with the result
         */
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(getActivity(), "Error al conceder los puntos. Intentelo más tarde",
                        Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("Correct")){
                Toast.makeText(getActivity(), "Puntos concedidos", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
}