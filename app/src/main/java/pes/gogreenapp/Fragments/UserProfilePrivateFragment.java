package pes.gogreenapp.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.user_birthdate;
import static pes.gogreenapp.R.id.user_currentpoints;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfilePrivateFragment extends Fragment {
    User testUser;

    public UserProfilePrivateFragment() {
        // Required empty public constructor
    }

    private void initializeUser(){
        testUser = new User("realPepeViyuela", "Pepe Viyuela", "viyuela@gmail.com", "12-10-1983", "http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.user_profile_private_fragment, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        TextView userBirthDate = (TextView) getView().findViewById(user_birthdate);
        TextView userCurrentPoints = (TextView) getView().findViewById(user_currentpoints);

        DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");

        //a la espera de tener la petición de la API hecha
        //new GetUserImage().execute("http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

        initializeUser();
        userBirthDate.setText((String) sourceFormat.format(testUser.getBirthDate()));
        userCurrentPoints.setText(String.valueOf(testUser.getCurrentPoints()));




    }

}
