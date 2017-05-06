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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import pes.gogreenapp.Objects.GlobalPreferences;
import pes.gogreenapp.Objects.SessionManager;
import pes.gogreenapp.R;

import static android.R.attr.id;

/**
 * Created by Adrian on 05/05/2017.
 */

public class GivePointsFragment extends Fragment {

    private SessionManager session;
    private ViewGroup v;
    private RelativeLayout spinnerLayoutEvents;
    private RelativeLayout textLayoutPoints;
    private RelativeLayout buttonLayout;

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
        spinnerLayoutEvents = new RelativeLayout(getContext());
        textLayoutPoints = new RelativeLayout(getContext());
        buttonLayout = new RelativeLayout(getContext());
        v = (ViewGroup) view.findViewById(R.id.givePointsFragment);
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
        Button grant = (Button) getView().findViewById(R.id.grantPoints);

        Button anotherUser = new Button(getContext());
        anotherUser.setId(Button.generateViewId());
        anotherUser.setText(R.string.anotherUser);

        Spinner spinnerEvents = new Spinner(getContext());
        spinnerEvents.setId(Spinner.generateViewId());

        EditText pointsToReceive = new EditText(getContext());
        pointsToReceive.setId(EditText.generateViewId());

        radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams buttonParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.layoutRadioGroup);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);

                if (checkedId == R.id.eventsRadioButton) {
                    buttonParam.addRule(RelativeLayout.BELOW, spinnerEvents.getId());
                    buttonParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    spinnerLayoutEvents.setLayoutParams(params);
                    spinnerLayoutEvents.removeView(spinnerEvents);
                    spinnerLayoutEvents.addView(spinnerEvents);
                    v.removeView(textLayoutPoints);
                    v.removeView(buttonLayout);
                    v.addView(spinnerLayoutEvents);
                }
                else {
                    buttonParam.addRule(RelativeLayout.BELOW, pointsToReceive.getId());
                    buttonParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    textLayoutPoints.setLayoutParams(params);
                    textLayoutPoints.removeView(pointsToReceive);
                    textLayoutPoints.addView(pointsToReceive);
                    v.removeView(spinnerLayoutEvents);
                    v.removeView(buttonLayout);
                    v.addView(textLayoutPoints);
                }

                buttonLayout.setLayoutParams(buttonParam);
                buttonLayout.removeView(anotherUser);
                buttonLayout.addView(anotherUser);
                v.addView(buttonLayout);
            }
        });

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
    }
}
