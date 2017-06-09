package pes.gogreenapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pes.gogreenapp.Adapters.TabPagerAdapter;
import pes.gogreenapp.R;

/**
 * Created by Adrian on 23/05/2017.
 */

public class FavsListFragment extends Fragment {

    public FavsListFragment () {
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
        if (!((AppCompatActivity) getActivity()).getSupportActionBar().isShowing())
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        return inflater.inflate(R.layout.favs_list_fragment, container, false);
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
        getActivity().setTitle("Favoritos");

        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tabLayoutFavsList);
        tabLayout.addTab(tabLayout.newTab().setText("Rewards"));
        tabLayout.addTab(tabLayout.newTab().setText("Eventos"));
        tabLayout.addTab(tabLayout.newTab().setText("Ofertas"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewPagerFavsList);
        final TabPagerAdapter adapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.d("favs", String.valueOf(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
}
