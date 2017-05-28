package pes.gogreenapp.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import pes.gogreenapp.R;

public class RewardsFilterDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface RewardsFilterDialogListener {

        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    RewardsFilterDialogListener mListener;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater lInflater = getActivity().getLayoutInflater();
        mBuilder.setView(lInflater.inflate(R.layout.rewards_filter_dialog, null));
        mListener = (RewardsFilterDialogListener) getTargetFragment();
        mBuilder.setPositiveButton("Filtrar", (dialog, which) -> {
            // Send the positive button event back to the host activity
            mListener.onDialogPositiveClick(RewardsFilterDialogFragment.this);
        });
        mBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            // Send the positive button event back to the host activity
            mListener.onDialogNegativeClick(RewardsFilterDialogFragment.this);
        });
        mBuilder.setTitle("Filtros");
        return mBuilder.create();
    }
}
