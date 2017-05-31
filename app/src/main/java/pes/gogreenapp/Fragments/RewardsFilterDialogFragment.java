package pes.gogreenapp.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.List;

import pes.gogreenapp.R;

public class RewardsFilterDialogFragment extends DialogFragment {

    private int filterCheckedId, sorterCheckedId, directionsCheckedId;
    private List<String> categories;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface RewardsFilterDialogListener {

        void onDialogPositiveClick(DialogFragment dialog, int filterId, int sorterId, int directionId);

        void onDialogNegativeClick(DialogFragment dialog, int filterId, int sorterId, int directionId);
    }

    // Use this instance of the interface to deliver action events
    RewardsFilterDialogListener mListener;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // inflate the custom layout for the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.rewards_filter_dialog, null);

        // Setup the Alert Dialog builder
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mListener = (RewardsFilterDialogListener) getTargetFragment();
        mBuilder.setView(inflate).setPositiveButton("Filtrar", (dialogLambda, which) -> mListener
                .onDialogPositiveClick(RewardsFilterDialogFragment.this, filterCheckedId, sorterCheckedId,
                        directionsCheckedId)).setNegativeButton(R.string.cancel, (dialogLambda, which) -> mListener
                .onDialogNegativeClick(RewardsFilterDialogFragment.this, filterCheckedId, sorterCheckedId,
                        directionsCheckedId));

        // set the items to the spinner
        Spinner categoriesSpinner = (Spinner) inflate.findViewById(R.id.categories_spinner);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,
                        categories);
        categoriesSpinner.setAdapter(arrayAdapter);


        // listeners for the radio group of filters
        RadioGroup radioFilters = (RadioGroup) inflate.findViewById(R.id.radio_filters_rewards);
        radioFilters.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_filter_category:
                    categoriesSpinner.setVisibility(View.VISIBLE);
                    filterCheckedId = checkedId;
                    break;
                default:
                    filterCheckedId = checkedId;
                    break;
            }
        });

        RadioGroup radioSorters = (RadioGroup) inflate.findViewById(R.id.radio_sorter_rewards);
        RadioGroup radioSorterDirections = (RadioGroup) inflate.findViewById(R.id.radio_sorter_directions);
        radioSorters.setOnCheckedChangeListener((group, checkedId) -> {

            sorterCheckedId = checkedId;
            radioSorterDirections.clearCheck();
            radioSorterDirections.setVisibility(View.VISIBLE);

        });

        radioSorterDirections.setOnCheckedChangeListener((group, checkedId) -> {
            directionsCheckedId = checkedId;
        });

        return mBuilder.create();
    }

    public void setCategories(List<String> categories) {

        this.categories = categories;
    }
}
