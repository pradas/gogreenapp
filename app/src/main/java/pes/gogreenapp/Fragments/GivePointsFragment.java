package pes.gogreenapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.Adapters.GivePointsAdapter;
import pes.gogreenapp.R;

/**
 * Created by Adrian on 11/05/2017.
 */

public class GivePointsFragment extends Fragment {

    ListView listToGivePoints;
    List<String> users;
    Button anotherUser;
    GivePointsAdapter adapter;

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
        users = new ArrayList<String>();
        users.add("Usuario nº1");
        adapter = new GivePointsAdapter(getContext(), users);
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

        anotherUser = (Button) getView().findViewById(R.id.anotherUserToGive);

        listToGivePoints = (ListView) getView().findViewById(R.id.listViewGivePoints);
        listToGivePoints.setAdapter(adapter);

        anotherUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.add("Usuario nº" + (users.size()+1));
                adapter.notifyDataSetChanged();
            }
        });
    }
}
