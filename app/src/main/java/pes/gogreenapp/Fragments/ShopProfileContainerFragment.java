package pes.gogreenapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pes.gogreenapp.R;

/**
 * Created by Adrian on 05/06/2017.
 */

public class ShopProfileContainerFragment extends Fragment {

    private int idTienda;

    /**
     * Required empty public constructor
     */
    public ShopProfileContainerFragment() {

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

        getActivity().setTitle("Tienda");
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("id")) {
            idTienda = getArguments().getInt("id");
        } else {
            idTienda = -1;
        }


        return inflater.inflate(R.layout.shop_profile_container_fragment, container, false);
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            ShopProfileInfoFragment uInfoFrag = new ShopProfileInfoFragment();
            OfertasListShopFragment rExFrag = new OfertasListShopFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("id", idTienda);
            uInfoFrag.setArguments(bundle);
            rExFrag.setArguments(bundle);

            transaction.add(R.id.shopProfile, uInfoFrag).add(R.id.dealsListShopFragment, rExFrag).commit();
        }
    }

}
