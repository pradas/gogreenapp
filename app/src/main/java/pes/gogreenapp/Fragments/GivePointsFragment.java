package pes.gogreenapp.Fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
    private Button anotherUser, grantPoints;
    private GivePointsByEventsAdapter adapterEvents;
    private GivePointsByPointsAdapter adapterPoints;

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

        JSONArray eventsHard = new JSONArray();

        JSONObject event = new JSONObject();
        JSONObject event2 = new JSONObject();
        JSONObject event3 = new JSONObject();
        JSONObject event4 = new JSONObject();
        try {
            event.put("title","Evento 1");
            event.put("points",100);
            event2.put("title","Evento 2");
            event2.put("points",200);
            event3.put("title","Evento 3");
            event3.put("points",300);
            event4.put("title","Evento 4");
            event4.put("points",400);
            eventsHard.put(event);
            eventsHard.put(event2);
            eventsHard.put(event3);
            eventsHard.put(event4);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < eventsHard.length(); ++ i) {
            try {
                JSONObject jsonObject = eventsHard.getJSONObject(i);
                events.add(new Event((String) jsonObject.get("title"), (Integer) jsonObject.get("points")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        mode = (Switch) getView().findViewById(R.id.switchModeItem) ;
        listToGivePoints = (ListView) getView().findViewById(R.id.listViewGivePoints);
        anotherUser = (Button) getView().findViewById(R.id.anotherUserToGive);
        grantPoints = (Button) getView().findViewById(R.id.grantPointsToUsers);
        adapterEvents = new GivePointsByEventsAdapter(getContext(), users, events);
        listToGivePoints.setAdapter(adapterEvents);


        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.switchModeGive);
                if (!isChecked) {
                    builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            modeItems = "Eventos";
                            users.clear();
                            users.add("Usuario nº1");
                            adapterEvents = new GivePointsByEventsAdapter(getContext(), users, events);
                            listToGivePoints.setAdapter(adapterEvents);
                        }
                    });
                }
                else {
                    builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            modeItems = "Puntos";
                            users.clear();
                            users.add("Usuario nº1");
                            adapterPoints = new GivePointsByPointsAdapter(getContext(), users);
                            listToGivePoints.setAdapter(adapterPoints);
                        }
                    });
                }
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        anotherUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modeItems.equals("Eventos")) {
                    users.add("Usuario nº" + (users.size() + 1));
                    adapterEvents.notifyDataSetChanged();
                }
                else {
                    users.add("Usuario nº" + (users.size() + 1));
                    adapterPoints.notifyDataSetChanged();
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

    /**
     * Asynchronous Task for the petition PUT of a User.
     */
    private class PutUser extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the method petition,
         *               params[2] is the username or email for identification in the login and
         *               params[3] is the password to identification in the login
         * @return "Falla" si no es un login correcte o "Correcte" si ha funcionat
         */
        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("points", params[2]);
            String url = params [0] + params[3];
            session = SessionManager.getInstance();
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null && !response.equals("500") ) {
                return "Correct";
            }
            return "Error";
        }

        /**
         * Called when doInBackground is finished, Toast an error if there is an error.
         *
         * @param result If is "Falla" makes the toast.
         */
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(getActivity(), "Error al conceder los puntos. Intentelo más tarde",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(), "Puntos concedidos", Toast.LENGTH_LONG).show();
            }
        }
    }
}