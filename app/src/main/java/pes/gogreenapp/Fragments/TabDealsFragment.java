package pes.gogreenapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pes.gogreenapp.R;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Adrian on 23/05/2017.
 */

public class TabDealsFragment extends Fragment {

    private SessionManager session;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;
    private TextView warning;

    public TabDealsFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_deals_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = SessionManager.getInstance();
        recyclerView = (RecyclerView) getView().findViewById(R.id.rvDealsFavList);
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainerDealsFavList);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //new GetCategories().execute(url + "categories");
        //new GetDeals().execute(url + "users/" + session.getUsername() + "/favourite-rewards");
        warning = (TextView) getView().findViewById(R.id.warningNoResultDealsFavList);

        swipeContainer.setOnRefreshListener(this::refreshItems);
    }

    /**
     * On swipe, refresh all the items of the screen.
     */
    void refreshItems() {
        // Load items
        //rewards.clear();
        warning.setText("");
        // Get items
        //new GetDeals().execute(url + "/users/" + session.getUsername() + "/favourite-rewards");

        // Load complete
        //onItemsLoadComplete();
    }
}
