package pes.gogreenapp.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.Adapters.GivePointsByEventsAdapter;
import pes.gogreenapp.Adapters.GivePointsByPointsAdapter;
import pes.gogreenapp.R;

/**
 * Created by Adrian on 11/05/2017.
 */

public class GivePointsFragment extends Fragment {

    String modeItems;
    ListView listToGivePoints;
    List<String> users;
    Switch mode;
    Button anotherUser, grantPoints;
    GivePointsByEventsAdapter adapterEvents;
    GivePointsByPointsAdapter adapterPoints;

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
        anotherUser = (Button) getView().findViewById(R.id.anotherUserToGive);
        grantPoints = (Button) getView().findViewById(R.id.grantPointsToUsers);
        listToGivePoints = (ListView) getView().findViewById(R.id.listViewGivePoints);

        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (! isChecked) { //EVENTOS
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
                                if (modeItems.equals("Eventos")) {
                                    List<String> userNames = adapterEvents.getUserNames();
                                }
                                else {
                                    List<String> userNames = adapterPoints.getUserNames();
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
}
