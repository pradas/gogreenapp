package pes.gogreenapp.Fragments;


import android.app.DatePickerDialog;
import android.os.AsyncTask;
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
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateOfertaFragment extends Fragment {
    private SessionManager session;
    private ImageButton DateButton;
    private EditText DateText;
    private Button SendButton;
    private EditText TitleText;
    private EditText DescriptionText;
    private EditText DiscountText;
    private Calendar calendar;
    static private String TAG = "CreateOferta";
    static private final String URLPetition = "http://10.4.41.145/api/deals";


    /**
     * Required empty public constructor
     */
    public CreateOfertaFragment() {
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
        return inflater.inflate(R.layout.create_oferta_fragment, container, false);
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
        session = SessionManager.getInstance();

        //elements
        DateButton = (ImageButton) getView().findViewById(R.id.DateCreateOferta);
        DateText = (EditText) getView().findViewById(R.id.editTextDateCreateOferta);
        SendButton = (Button) getView().findViewById(R.id.buttonSendCreateOferta);
        TitleText = (EditText) getView().findViewById(R.id.titleCreateOferta_edit_text);
        DescriptionText = (EditText) getView().findViewById(R.id.DescriptionCreateOferta_edit_text);
        DiscountText = (EditText) getView().findViewById(R.id.PointsCreateOferta_edit_text);

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
            //Toast.makeText(getActivity(), String.valueOf(categoriesSpinner.getSelectedItem()), Toast.LENGTH_LONG).show();
            Boolean send = true;
            if (TitleText.getText().toString().length() <= 0) {
                TitleText.setError("Título necesario");
                send = false;
            }
            if (DescriptionText.getText().toString().length() <= 0) {
                DescriptionText.setError("Descripción necesaria");
                send = false;
            }
            if (DiscountText.getText().toString().length() <= 0) {
                DiscountText.setError("Descuento necesario");
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
                    send = false;
                }
            }
            if (send) {
                Log.d(TAG, "se envia");

                new PostOferta().execute(URLPetition, "POST",
                        TitleText.getText().toString(),
                        DescriptionText.getText().toString(),
                        DiscountText.getText().toString(),
                        DateText.getText().toString()
                );
            }
        });
    }


    /**
     * Asynchronous Task for the petition POST to send a petition of register an User
     */
    private class PostOferta extends AsyncTask<String, Void, String> {
        @Override
        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the method,
         *               params[2] is the title,
         *               params[3] is the description
         *               params[4] is the points
         *               params[5] is the adress
         *               params[6] is the company
         *               params[7] is the date
         *               params[8] is the time
         *               params[9] is the image
         *               params[10] is the category
         * @return void when finished
         */
        protected String doInBackground(String... params) {
            HashMap<String, String> BodyParams = new HashMap<>();
            BodyParams.put("name", params[2]);
            BodyParams.put("description", params[3]);
            BodyParams.put("value", params[4]);
            BodyParams.put("date", params[5]);
            String result = new HttpHandler().makeServiceCall(params[0], params[1], BodyParams,
                    session.getToken());
            Log.i(TAG, "Response from url: " + result);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(getActivity(), "Error, no se ha podido conectar, intentelo de nuevo más tarde", Toast.LENGTH_LONG).show();
            } else if (s.contains("Event created successfully.")) {
                Toast.makeText(getActivity(), "Creado perfectamente.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "No se ha podido crear.", Toast.LENGTH_LONG).show();
            }
        }
    }



}
