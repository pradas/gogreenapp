package pes.gogreenapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.user_creation_date;
import static pes.gogreenapp.R.id.user_email;
import static pes.gogreenapp.R.id.user_image;
import static pes.gogreenapp.R.id.user_name;
import static pes.gogreenapp.R.id.user_nickname;
import static pes.gogreenapp.R.id.user_points;



/**
 * Created by Daniel on 17/04/2017.
 */
public class UserProfileFragment extends Fragment
        implements UserProfilePrivateEditFragment.OnEditSelectionEventListener,
                    UserProfilePrivateFragment.OnEditSelectionEventListener{
    User testUser;
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
        return inflater.inflate(R.layout.user_profile_container_fragment, container, false);
    }

    @Override
    public void onEditSelectionEvent(boolean editUser){


        if(editUser){


            UserProfilePublicEditFragment uPublicEditFrag = new UserProfilePublicEditFragment();
            UserProfilePrivateEditFragment uPrivateEditFrag = new UserProfilePrivateEditFragment();


            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

            transaction
                    .replace(R.id.user_profile_c1, uPublicEditFrag)
                    .replace(R.id.user_profile_c2, uPrivateEditFrag)
                    .addToBackStack(null)
                    .commit();
            /*
            transaction.replace(R.id.user_profile_c1, uPublicEditFrag);
            transaction.commit();


            transaction = getChildFragmentManager().beginTransaction();

            transaction.replace(R.id.user_profile_c2, uPrivateEditFrag);
            transaction.commit();
            */

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

            UserProfilePublicFragment uPublicFrag = new UserProfilePublicFragment();
            UserProfilePrivateFragment uPrivateFrag = new UserProfilePrivateFragment();


            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();


            transaction
                    .add(R.id.user_profile_c1, uPublicFrag)
                    .add(R.id.user_profile_c2, uPrivateFrag)
                    .add(R.id.rewards_exchanged_fragment, rExFrag)
                    .commit();
        }

        TextView userName = (TextView) getView().findViewById(user_name);
        TextView userNickName = (TextView) getView().findViewById(user_nickname);
        TextView userPoints = (TextView) getView().findViewById(user_points);
        TextView userEmail = (TextView) getView().findViewById(user_email);
        TextView userCreationDate = (TextView) getView().findViewById(user_creation_date);
        ImageView userImage = (ImageView) getView().findViewById(user_image);

        DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");

        //a la espera de tener la petición de la API hecha
        //new GetUserImage().execute("http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

        initializeUser();
        new GetInfoUser().execute();

    }


    private void initializeUser(){
        testUser = new User("realPepeViyuela", "Pepe Viyuela", "viyuela@gmail.com", "12-10-1983", "http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

    }



    private class GetInfoUser extends AsyncTask<String, Void, Void> {



        @Override
        protected Void doInBackground(String... urls) {
            //HttpHandler httpHandler = new HttpHandler();
            //String response = httpHandler.makeServiceCall(urls[0]);
            //Log.i(TAG, "Response from url: " + response);



            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

        }

    }

}
