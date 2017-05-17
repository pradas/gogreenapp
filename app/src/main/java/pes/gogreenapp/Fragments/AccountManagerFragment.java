package pes.gogreenapp.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.Adapters.AccountManagerAdapter;
import pes.gogreenapp.Exceptions.NullParametersException;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.SessionManager;
import pes.gogreenapp.Utils.UserData;

public class AccountManagerFragment extends Fragment {

    /**
     * Required empty public constructor
     */
    public AccountManagerFragment() {

    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given
     *                           here.
     *
     * @return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.account_manager_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        SessionManager instance = SessionManager.getInstance();

        List<String> usernames = new ArrayList<>();
        try {
            usernames = UserData.getUsernames(getActivity().getApplicationContext(), instance.getUsername());
        } catch (NullParametersException e) {
            e.printStackTrace();
        }

        AccountManagerAdapter adapter = new AccountManagerAdapter(usernames, getActivity().getApplicationContext());
        ListView listView = (ListView) getView().findViewById(R.id.account_manager_list);
        listView.setAdapter(adapter);
    }

}
