package pes.gogreenapp.Fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pes.gogreenapp.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    private ImageButton DateButton;
    private ImageView ImageSelected;
    private ImageButton PhotoButton;
    private EditText DateText;
    private Button SendButton;
    private EditText TitleText;
    private EditText DescriptionText;
    private EditText PointsText;
    private EditText DirectionText;
    private EditText HourText;
    private EditText MinText;
    private EditText CompanyText;
    private Calendar calendar;
    private String FinalTime = null;

    /**
     * Required empty public constructor
     */
    public CreateEventFragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_edit_event_fragment, container, false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //elements
        DateButton = (ImageButton) getView().findViewById(R.id.DateCreateEvent);
        PhotoButton = (ImageButton) getView().findViewById(R.id.ImageCreateEventButton);
        ImageSelected = (ImageView) getView().findViewById(R.id.ImageSelected);
        DateText = (EditText) getView().findViewById(R.id.editTextDateEvent);
        SendButton = (Button) getView().findViewById(R.id.buttonSendCreateEvent);
        TitleText = (EditText) getView().findViewById(R.id.titleCreateEvent_edit_text);
        DescriptionText = (EditText) getView().findViewById(R.id.DescriptionCreateEvent_edit_text);
        PointsText = (EditText) getView().findViewById(R.id.PointsCreateEvent_edit_text);
        DirectionText = (EditText) getView().findViewById(R.id.DirectionCreateEvent_edit_text);
        HourText = (EditText) getView().findViewById(R.id.HourCreateEvent_edit_text);
        MinText = (EditText) getView().findViewById(R.id.MinCreateEvent_edit_text);
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

        PhotoButton.setOnClickListener((View v) -> {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
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
            } else {
                Date date = null;
                String inputDate = DateText.getText().toString();
                try {
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    formatter.setLenient(false);
                    date = formatter.parse(inputDate);
                } catch (ParseException e) {
                    DateText.setError("Fecha invalida (dd-mm-yyyy)");
                }
            }
            if (HourText.getText().toString().length() > 0 && MinText.getText().toString().length() <= 0) {
                MinText.setError("Minutos necesarios");
                send = false;
            }
            if (HourText.getText().toString().length() <= 0 && MinText.getText().toString().length() > 0) {
                HourText.setError("Hora necesaria");

            }
            if (HourText.getText().toString().length() > 0 && MinText.getText().toString().length() > 0) {
                if (Integer.parseInt(HourText.getText().toString()) > 23) {
                    HourText.setError("Hora incorrecta");
                    send = false;
                }
                if (Integer.parseInt(MinText.getText().toString()) > 59) {
                    MinText.setError("Minutos incorrectos");
                    send = false;
                }
                FinalTime = HourText.getText().toString() + ":" + MinText.getText().toString();
                Log.d("CreateEvent", FinalTime);
            }
            if (send) {
                Log.d("CreateEvent", "se envia");
                //new LoginFragment.PostLogin().execute("http://10.4.41.145/api/session", "POST",
                //        textName.getText().toString(), textPassword.getText().toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageSelected.setMaxHeight(PhotoButton.getHeight());
                ImageSelected.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
            } else {
                Toast.makeText(getContext(), "No has escogido ninguna imagen",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("CreateEvent", e.toString());
            Toast.makeText(getContext(), "Error al escoger la imagen", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
