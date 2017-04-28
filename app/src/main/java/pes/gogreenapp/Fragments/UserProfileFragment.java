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
 * Created by Daniel on 17/04/2017.
 */
public class UserProfileFragment extends Fragment
        implements UserProfilePrivateEditFragment.OnEditSelectionEventListener,
                    UserProfilePrivateFragment.OnEditSelectionEventListener{

    /**
     *  Required empty public constructor
     */
    public UserProfileFragment(){

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
        return inflater.inflate(R.layout.user_profile_fragment, container, false);
    }


    /**
     * when pressing the "edit" button, replace the fragments shown on the profile
     */
    @Override
    public void onEditSelectionEvent(boolean editUser){

        if(editUser){
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            UserProfilePublicEditFragment uPublicEditFrag = new UserProfilePublicEditFragment();
            UserProfilePrivateEditFragment uPrivateEditFrag = new UserProfilePrivateEditFragment();
            transaction
                    .replace(R.id.user_profile_c1, uPublicEditFrag)
                    .replace(R.id.user_profile_c2, uPrivateEditFrag)
                    .addToBackStack(null)
                    .commit();

        }
        else {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            UserProfilePublicFragment uPublicFrag = new UserProfilePublicFragment();
            UserProfilePrivateFragment uPrivateFrag = new UserProfilePrivateFragment();
            transaction
                    .replace(R.id.user_profile_c1, uPublicFrag)
                    .replace(R.id.user_profile_c2, uPrivateFrag)
                    .addToBackStack(null)
                    .commit();

        }

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

        RewardsExchangedFragment rExFrag = new RewardsExchangedFragment();

        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            UserProfilePublicFragment uPublicFrag = new UserProfilePublicFragment();
            UserProfilePrivateFragment uPrivateFrag = new UserProfilePrivateFragment();

            transaction
                    .add(R.id.user_profile_c1, uPublicFrag)
                    .add(R.id.user_profile_c2, uPrivateFrag)
                    .add(R.id.rewards_exchanged_fragment, rExFrag)
                    .commit();
        }
    }



}
