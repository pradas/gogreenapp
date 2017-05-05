package pes.gogreenapp.Fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pes.gogreenapp.R;

import static android.R.attr.value;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {

    private ImageButton DateButton;
    private EditText DateText;
    private Button SendButton;
    private EditText TitleText;
    private EditText DescriptionText;
    private EditText PointsText;
    private EditText DirectionText;
    private EditText TimeText;
    private EditText CompanyText;
    private Calendar calendar;

    /**
     * Required empty public constructor
     */
    public CreateEventFragment() {
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //elements
        DateButton = (ImageButton) getView().findViewById(R.id.DateCreateEvent);
        DateText = (EditText) getView().findViewById(R.id.editTextDateEvent);
        SendButton = (Button) getView().findViewById(R.id.buttonSendEvent);
        TitleText = (EditText) getView().findViewById(R.id.titleCreateEvent_edit_text);
        DescriptionText = (EditText) getView().findViewById(R.id.DescriptionCreateEvent_edit_text);
        PointsText = (EditText) getView().findViewById(R.id.PointsCreateEvent_edit_text);
        DirectionText = (EditText) getView().findViewById(R.id.DirectionCreateEvent_edit_text);
        TimeText = (EditText) getView().findViewById(R.id.TimeCreateEvent_edit_text);
        CompanyText = (EditText) getView().findViewById(R.id.CompanyCreateEvent_edit_text);
        //events
        DateButton.setOnClickListener((View v) -> {
            calendar = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    (DatePicker view, int year, int monthOfYear, int dayOfMonth) -> {
                        String sDayOfMonth = String.format("%02d", dayOfMonth);
                        String sMonthOfYear = String.format("%02d", monthOfYear + 1);
                        DateText.setText(sDayOfMonth + "-" + sMonthOfYear + "-" + year);
                        DateText.clearFocus();
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
            dpd.show();
        });
        SendButton.setOnClickListener(v -> {
            Boolean send = true;
            if (TitleText.getText().toString().length() <= 0) {
                TitleText.setError("Título necesario");
                send = false;
            }
            if (DescriptionText.getText().toString().length() <= 0) {
                DescriptionText.setError("Descripción necesaria");
                send = false;
            }
            if (PointsText.getText().toString().length() <= 0) {
                PointsText.setError("Puntos necesarios");
                send = false;
            }
            if (DateText.getText().toString().length() <= 0) {
                DateText.setError("Fecha necesaria");
                send = false;
            }
            if (TimeText.getText().toString().length() > 0){
                String[] d = DateText.getText().toString().split(":");
                Log.d("CreateEvent",d[0]);
                Log.d("CreateEvent",d[1]);
                if (d.length == 2){
                    int hour = Integer.parseInt(d[0]);
                    int min = Integer.parseInt(d[1]);
                    if (hour > 23 || min > 59){
                        TimeText.setError("Mal formato (HH:mm)");
                    }
                }
                else {
                    TimeText.setError("Mal formato (HH:mm)");
                }
            }
            if (send) {
                //new LoginFragment.PostLogin().execute("http://10.4.41.145/api/session", "POST",
                //        textName.getText().toString(), textPassword.getText().toString());
            }
            // Staring MainActivity
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_event_fragment, container, false);
    }

}
