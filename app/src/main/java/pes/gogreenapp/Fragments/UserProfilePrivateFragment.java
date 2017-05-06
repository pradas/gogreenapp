package pes.gogreenapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;
import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.edit_profile_button;
import static pes.gogreenapp.R.id.user_birthdate;
import static pes.gogreenapp.R.id.user_currentpoints;
import static pes.gogreenapp.R.id.user_email;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfilePrivateFragment extends Fragment {
    User testUser;
    User userInfo;
    SessionManager session;
    String url = "http://10.4.41.145/api/";
    private String TAG = MainActivity.class.getSimpleName();
    private String userName;
    TextView userBirthDate;
    TextView userCurrentPoints;
    TextView userEmail;
    DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");



    OnEditSelectionEventListener mOnEditSelectionEventListener;

    public interface OnEditSelectionEventListener{
        void onEditSelectionEvent(boolean userEdit);
    }

    public UserProfilePrivateFragment() {
        // Required empty public constructor
    }

    private void initializeUser(){
        testUser = new User("realPepeViyuela", "Pepe Viyuela", "viyuela@gmail.com", "12-10-1983", "http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

    }


    public void onAttachToParentFragment(Fragment fragment) {
       try{
           mOnEditSelectionEventListener = (OnEditSelectionEventListener) fragment;
       }
       catch (ClassCastException e){
           throw new ClassCastException(
                   fragment.toString() + " must implement OnEditSelectionEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.user_profile_private_fragment, container, false);
    }



    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getActivity().getApplicationContext(),
                new GlobalPreferences(getActivity().getApplicationContext()).getUser());

        onAttachToParentFragment(getParentFragment());

        Button editButton = (Button) getView().findViewById(edit_profile_button);
        userBirthDate = (TextView) getView().findViewById(user_birthdate);
        userCurrentPoints = (TextView) getView().findViewById(user_currentpoints);
        userEmail = (TextView) getView().findViewById(user_email);


        //a la espera de tener la petición de la API hecha
        //new GetUserImage().execute("http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

        initializeUser();
        userName = session.getUserName();
        new GetPrivateInfoUser().execute(url + "users/" + userName);

        editButton.setOnClickListener(v -> {
            //ok test
            //userEmail.setText("test");
            if(mOnEditSelectionEventListener != null){
                mOnEditSelectionEventListener.onEditSelectionEvent(true);
            }
        });



    }

    private class GetPrivateInfoUser extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET" , new HashMap<>(),
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

            }  catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            userBirthDate.setText((String) sourceFormat.format(userInfo.getBirthDate()));
            userCurrentPoints.setText(String.valueOf(userInfo.getCurrentPoints()));
            userEmail.setText(userInfo.getEmail());
        }

    }

}
