package pes.gogreenapp.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Button;

import pes.gogreenapp.R;

/**
 * Created by Usuario on 16/05/2017.
 */



public class RewardsFilterDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface FilterDialogListener {

        public void onDialogAllClick(DialogFragment dialog);
        public void onDialogCategoriesClick(DialogFragment dialog);
        public void onDialogRedeemablesClick(DialogFragment dialog);
        public void onDialogDateOrderClick (DialogFragment dialog);
        public void onDialogPointsOrderClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    FilterDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the FilterDialogListener

    public void onAttachToParentFragment(Fragment fragment) {
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the FilterDialogListener so we can send events to the host
            mListener = (FilterDialogListener) fragment;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(fragment.toString()
                    + " must implement FilterDialogListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        LayoutInflater lInflater = getActivity().getLayoutInflater();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setView(lInflater.inflate(R.layout.rewards_filter_dialog, null));
        mBuilder.setTitle("SELECCIONA UNA CATEGORIA");
        mBuilder.setNegativeButton("CANCELAR", (dialog, id) -> {

        });

        AlertDialog dialog = mBuilder.create();

        final Button endDate = (Button) dialog.findViewById(R.id.orderDateButton);
        final Button points = (Button) dialog.findViewById(R.id.orderPointsButton);
        final Button categoriesButton = (Button) dialog.findViewById(R.id.showCategoriesButton);
        final Button allRewards = (Button) dialog.findViewById(R.id.showAllButton);
        final Button redeemables = (Button) dialog.findViewById(R.id.showRedeemablesButton);

        //listeners, comentados porque petan
        /*
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onDialogDateOrderClick(RewardsFilterDialogFragment.this);
            }
        });*/
        //endDate.setOnClickListener(v -> { mListener.onDialogDateOrderClick(RewardsFilterDialogFragment.this);});
        /*
        points.setOnClickListener(v -> {mListener.onDialogPointsOrderClick(RewardsFilterDialogFragment.this);});
        categoriesButton.setOnClickListener(v -> {mListener.onDialogCategoriesClick(RewardsFilterDialogFragment.this);});
        redeemables.setOnClickListener(v -> {mListener.onDialogRedeemablesClick(RewardsFilterDialogFragment.this);});
        allRewards.setOnClickListener(v -> {mListener.onDialogAllClick(RewardsFilterDialogFragment.this);});
*/


        return mBuilder.create();
    }
}
