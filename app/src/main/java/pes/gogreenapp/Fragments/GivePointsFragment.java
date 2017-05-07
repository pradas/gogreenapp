package pes.gogreenapp.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import pes.gogreenapp.Objects.GlobalPreferences;
import pes.gogreenapp.Objects.SessionManager;
import pes.gogreenapp.R;

import static android.R.attr.id;

/**
 * Created by Adrian on 05/05/2017.
 */

public class GivePointsFragment extends Fragment {

    private SessionManager session;
    private ViewGroup principalView;

    /**
     * Required empty public constructor
     */
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
        View view = inflater.inflate(R.layout.give_points_fragment, container, false);
        principalView = (ViewGroup) view.findViewById(R.id.givePointsFragment);
        return view;
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

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getActivity().getApplicationContext(),
                new GlobalPreferences(getActivity().getApplicationContext()).getUser());
        RadioGroup radioButtons = (RadioGroup) getView().findViewById(R.id.radioGroupGivePoints);
        final Integer[] buttonMode = {0};
        Button grant = (Button) getView().findViewById(R.id.grantPoints);
        Button anotherUser;
        View eventsView = LayoutInflater.from(getContext()).inflate(R.layout.give_points_by_events, principalView, false);
        View pointsView = LayoutInflater.from(getContext()).inflate(R.layout.give_points_by_points, principalView, false);

        radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.layoutRadioGroup);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);

                if (checkedId == R.id.eventsRadioButton) {
                    buttonMode[0] = 0;
                    eventsView.setLayoutParams(params);
                    principalView.removeView(pointsView);
                    principalView.addView(eventsView);
                }
                else {
                    buttonMode[0] = 1;
                    pointsView.setLayoutParams(params);
                    principalView.removeView(eventsView);
                    principalView.addView(pointsView);
                }
            }
        });

        if (buttonMode[0] == 0) anotherUser = (Button) eventsView.findViewById(R.id.anotherUserButton);
        else anotherUser = (Button) pointsView.findViewById(R.id.anotherUserButton);

        grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("¿Está seguro de que desea conceder los puntos?")
                    .setPositiveButton(R.string.exchange, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        anotherUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View eventsView = LayoutInflater.from(getContext()).inflate(R.layout.give_points_by_events, principalView, false);
                View pointsView = LayoutInflater.from(getContext()).inflate(R.layout.give_points_by_points, principalView, false);
                View header = LayoutInflater.from(getContext()).inflate(R.layout.header_give_points_fragment, principalView, false);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                /*if (buttonMode[0] == 0) params.addRule(RelativeLayout.BELOW,
                        eventsView.findViewById(R.id.anotherUserButton).getId());
                else params.addRule(RelativeLayout.BELOW, pointsView.
                        findViewById(R.id.anotherUserButton).getId());*/                        //NO FUNCIONA
                params.topMargin = 320;                                                         //TTEMPORAL
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                header.setLayoutParams(params);
                principalView.addView(header);

                params.topMargin = 500;                                                         //TTEMPORAL
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                eventsView.setLayoutParams(params);
                principalView.addView(eventsView);
            }
        });
    }
}
