package pes.gogreenapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.edit_profile_button;
import static pes.gogreenapp.R.id.user_birthdate;
import static pes.gogreenapp.R.id.user_currentpoints;
import static pes.gogreenapp.R.id.user_email_edit;


public class UserProfilePrivateEditFragment extends Fragment {
    User testUser;
    OnEditSelectionEventListener mOnEditSelectionEventListener;

    public UserProfilePrivateEditFragment() {
        // Required empty public constructor
    }

    public interface OnEditSelectionEventListener{
        void onEditSelectionEvent(boolean userEdit);
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
        return inflater.inflate(R.layout.user_profile_private_edit_fragment, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        onAttachToParentFragment(getParentFragment());

        Button editButton = (Button) getView().findViewById(edit_profile_button);
        TextView userBirthDate = (TextView) getView().findViewById(user_birthdate);
        TextView userCurrentPoints = (TextView) getView().findViewById(user_currentpoints);
        EditText userEmail = (EditText) getView().findViewById(user_email_edit);

        DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");

        //a la espera de tener la petición de la API hecha
        //new GetUserImage().execute("http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

        initializeUser();
        userBirthDate.setText((String) sourceFormat.format(testUser.getBirthDate()));
        userCurrentPoints.setText(String.valueOf(testUser.getCurrentPoints()));
        userEmail.setText(testUser.getEmail());
        editButton.setOnClickListener(v -> {
            //ok test
            //userEmail.setText("test");
            if(mOnEditSelectionEventListener != null){
                mOnEditSelectionEventListener.onEditSelectionEvent(false);
            }
        });



    }

}
