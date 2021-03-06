package pes.gogreenapp.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.List;

import pes.gogreenapp.R;

public class FilterDialogFragment extends DialogFragment {

    private int filterCheckedId, sorterCheckedId, directionsCheckedId;
    private List<String> categories;
    private String categoryChecked;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface FilterDialogListener {

        void onDialogPositiveClick(DialogFragment dialog, int filterId, int sorterId, int directionId, String category);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    FilterDialogListener mListener;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // inflate the custom layout for the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.rewards_filter_dialog, null);

        // Setup the Alert Dialog builder
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        Fragment targetFragment = getTargetFragment();
        mListener = (FilterDialogListener) targetFragment;
        mBuilder.setView(inflate).setPositiveButton("Filtrar", (dialogLambda, which) -> mListener
                .onDialogPositiveClick(FilterDialogFragment.this, filterCheckedId, sorterCheckedId, directionsCheckedId,
                        categoryChecked)).setNegativeButton(R.string.cancel,
                (dialogLambda, which) -> mListener.onDialogNegativeClick(FilterDialogFragment.this));

        // set visibility of the last radio
        if ("pes.gogreenapp.Fragments.RewardsListFragment".equals(targetFragment.getClass().getName())) {
            inflate.findViewById(R.id.radio_filter_canjeables).setVisibility(View.VISIBLE);
        }

        if ("pes.gogreenapp.Fragments.OfertasListFragment".equals(targetFragment.getClass().getName())) {
            inflate.findViewById(R.id.radio_filters).setVisibility(View.GONE);
            inflate.findViewById(R.id.filter_by_text).setVisibility(View.GONE);
        } else {
            // set the items to the spinner
            Spinner categoriesSpinner = (Spinner) inflate.findViewById(R.id.categories_spinner);
            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.simple_spinner_dropdown_item,
                            categories);
            categoriesSpinner.setAdapter(arrayAdapter);
            categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    categoryChecked = (String) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    categoryChecked = "";
                }
            });


            // listeners for the radio group of filters
            RadioGroup radioFilters = (RadioGroup) inflate.findViewById(R.id.radio_filters);
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
        }
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
