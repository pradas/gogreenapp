package pes.gogreenapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.GlobalPreferences;
import pes.gogreenapp.Objects.SessionManager;
import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;


/**
 * Created by Daniel on 17/04/2017.
 */
public class UserProfileFragment extends Fragment
        implements UserProfilePrivateEditFragment.OnEditProfileEventListener,
                    UserProfilePrivateFragment.OnEditSelectionEventListener{

    String url = "http://10.4.41.145/api/";
    DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
    SessionManager session;
    User userInfo;
    RequestQueue mRequestQueue;
    private String TAG = MainActivity.class.getSimpleName();
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

    @Override
    public void onSaveSelectionEvent(){
        TextView userBirthDate = (TextView) getView().findViewById(R.id.user_birthdate);
        TextView userNameLayout = (TextView) getView().findViewById(R.id.user_name_edit);
        TextView userEmail = (EditText) getView().findViewById(R.id.user_email_edit);

        //new GetInfoUser().execute(url + "users/" + session.getUserName());
        new PutUserMethod().execute(url + "/users/" +session.getUserName() ,
                userNameLayout.getText().toString(),
                userEmail.getText().toString(),
                userBirthDate.getText().toString());
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
        session = new SessionManager(getActivity().getApplicationContext(),
                new GlobalPreferences(getActivity().getApplicationContext()).getUser());
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

    /**
     * Asynchronous Task for the petition POST to send a petition of register an User
     */
    private class PutUserMethod extends AsyncTask<String, Void, String> {
        @Override
        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the username,
         *               params[2] is the name of the user
         *               params[3] is the email
         *               params[4] is the password
         *               params[5] is the birthday date
         * @return void when finished
         */
        protected String doInBackground(String... params) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
            HashMap<String,String> impl = new HashMap<>();
            impl.put("name",params[1]);
            impl.put("email",params[2]);
            impl.put("birth_date",params[3]);

            String result = new HttpHandler().makeServiceCall(params[0],"PUT" ,impl,"");
            Log.i(TAG, "Response from url: " + result);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null){
                Toast.makeText(getActivity(),"Error, no se ha podido conectar, intentelo de nuevo mÃ¡s tarde",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(),"Usuario editado",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetPrivateInfoUser extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(),
                    session.getToken());
            Log.i(TAG, "Response from url: " + response);


            try {

                JSONObject jsonArray = new JSONObject(response);

                userInfo = new User(jsonArray.getString("username"),
                        jsonArray.getString("name"),
                        jsonArray.getString("email"),
                        jsonArray.getString("birth_date"),
                        jsonArray.getString("image"),
                        jsonArray.getInt("total_points"),
                        jsonArray.getInt("points"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {


        }
    }



}
