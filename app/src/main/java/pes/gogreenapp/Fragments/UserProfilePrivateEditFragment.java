package pes.gogreenapp.Fragments;


import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.GlobalPreferences;
import pes.gogreenapp.Objects.SessionManager;
import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.edit_profile_button;
import static pes.gogreenapp.R.id.save_profile_button;
import static pes.gogreenapp.R.id.user_birthdate;
import static pes.gogreenapp.R.id.user_birthdate_button;
import static pes.gogreenapp.R.id.user_currentpoints;
import static pes.gogreenapp.R.id.user_email_edit;


public class UserProfilePrivateEditFragment extends Fragment {
    User testUser;
    User userInfo;
    SessionManager session;
    String url = "http://10.4.41.145/api/";
    private String TAG = MainActivity.class.getSimpleName();
    private String userName;
    private static Integer mYear, mMonth, mDay;
    TextView userBirthDate;
    TextView userCurrentPoints;
    EditText userEmail;
    DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");


    OnEditProfileEventListener mOnEditSelectionEventListener;

    public UserProfilePrivateEditFragment() {
        // Required empty public constructor
    }

    public interface OnEditProfileEventListener {
        void onEditSelectionEvent(boolean userEdit);
        void onSaveSelectionEvent();
    }




    private void initializeUser(){
        testUser = new User("realPepeViyuela", "Pepe Viyuela", "viyuela@gmail.com", "12-10-1983", "http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

    }

    public void onAttachToParentFragment(Fragment fragment) {
        try{
            mOnEditSelectionEventListener = (OnEditProfileEventListener) fragment;
        }
        catch (ClassCastException e){
            throw new ClassCastException(
                    fragment.toString() + " must implement OnEditProfileEventListener");
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
        session = new SessionManager(getActivity().getApplicationContext(),
                new GlobalPreferences(getActivity().getApplicationContext()).getUser());


        onAttachToParentFragment(getParentFragment());
        Button editBirthdate = (Button) getView().findViewById(user_birthdate_button);
        Button editButton = (Button) getView().findViewById(edit_profile_button);
        Button saveButton = (Button) getView().findViewById(save_profile_button);
        userBirthDate = (TextView) getView().findViewById(user_birthdate);
        userCurrentPoints = (TextView) getView().findViewById(user_currentpoints);
        userEmail = (EditText) getView().findViewById(user_email_edit);


        //a la espera de tener la petición de la API hecha
        //new GetUserImage().execute("http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

        initializeUser();
        userName = session.getUserName();
        new GetPrivateInfoUser().execute(url + "users/" + userName);

        editButton.setOnClickListener(v -> {
            //ok test
            //userEmail.setText("test");
            if(mOnEditSelectionEventListener != null){
                mOnEditSelectionEventListener.onEditSelectionEvent(false);
            }
        });
        saveButton.setOnClickListener(v -> {
            if(mOnEditSelectionEventListener != null){
                mOnEditSelectionEventListener.onSaveSelectionEvent();
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
