package pes.gogreenapp.Fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.edit_profile_button;
import static pes.gogreenapp.R.id.user_birthdate;
import static pes.gogreenapp.R.id.user_birthdate_button;
import static pes.gogreenapp.R.id.user_currentpoints;
import static pes.gogreenapp.R.id.user_email_edit;


public class UserProfilePrivateEditFragment extends Fragment {
    User testUser;
    private static Integer mYear, mMonth, mDay;
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
        Button editBirthdate = (Button) getView().findViewById(user_birthdate_button);
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
        editBirthdate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            //modificar para tener el cumpleaños del usuario actual;
            mYear = c.get(Calendar.YEAR) -18;
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox

                            if (year > mYear)
                                view.updateDate(mYear, mMonth, mDay);

                            if (monthOfYear > mMonth && year == mYear)
                                view.updateDate(mYear, mMonth, mDay);

                            if (dayOfMonth > mDay && year == mYear && monthOfYear == mMonth)
                                view.updateDate(mYear, mMonth, mDay);
                            String sDayOfMonth = String.format("%02d",dayOfMonth);
                            String sMonthOfYear = String.format("%02d",monthOfYear + 1);

                            userBirthDate.setText(sDayOfMonth + "/" + sMonthOfYear + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear,mMonth,mDay);
            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            dpd.show();

        });




    }

}
