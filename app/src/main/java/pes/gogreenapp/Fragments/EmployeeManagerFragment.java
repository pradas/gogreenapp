package pes.gogreenapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pes.gogreenapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeManagerFragment extends Fragment {


    public EmployeeManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.employee_manager_fragment, container, false);
    }

}
