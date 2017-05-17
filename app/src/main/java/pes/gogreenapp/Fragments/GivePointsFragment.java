package pes.gogreenapp.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Adapters.GivePointsByEventsAdapter;
import pes.gogreenapp.Adapters.GivePointsByPointsAdapter;
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.GlobalPreferences;
import pes.gogreenapp.Objects.SessionManager;
import pes.gogreenapp.R;

/**
 * Created by Adrian on 11/05/2017.
 */

public class GivePointsFragment extends Fragment {

    private SessionManager session;
    private String modeItems;
    private ListView listToGivePoints;
    private List<String> users;
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
        users.add("Usuario nº1");
        adapterEvents = new GivePointsByEventsAdapter(getContext(), users);
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

        mode = (Switch) getView().findViewById(R.id.switchModeItem) ;
        listToGivePoints = (ListView) getView().findViewById(R.id.listViewGivePoints);
        anotherUser = (Button) getView().findViewById(R.id.anotherUserToGive);
        grantPoints = (Button) getView().findViewById(R.id.grantPointsToUsers);

        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) { //EVENTOS
                    modeItems = "Eventos";
                    listToGivePoints.setAdapter(adapterEvents);
                }
                else { //PUNTOS
                    modeItems = "Puntos";
                    listToGivePoints.setAdapter(adapterPoints);
                }
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
                                String url = "http://10.4.41.145/api/users/";
                                if (modeItems.equals("Eventos")) {
                                    /*List<String> userNames = adapterEvents.getUserNames();
                                    List<String> events = adapterEvents.getEvents();
                                    url += userNames.get(i);
                                    for (int i = 0; i < userNames.size(); ++ i) {
                                        new PutUser().execute(url, "PUT", events.get(i).getPoints());
                                    }*/
                                }
                                else {
                                    /*List<String> userNames = adapterPoints.getUserNames();
                                    List<Integer> points = adapterPoints.getPoints();
                                    url += userNames.get(i);
                                    for (int i = 0; i < userNames.size(); ++ i) {
                                        new PutUser().execute(url, "PUT", points.get(i));
                                    }*/
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
            String response = httpHandler.makeServiceCall(params[0], params[1], bodyParams, "");
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
