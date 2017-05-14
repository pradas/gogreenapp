package pes.gogreenapp.Fragments;


import android.app.DatePickerDialog;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import pes.gogreenapp.Objects.Event;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventFragment extends Fragment {
    private SessionManager session;
    private ImageButton DateButton;
    private EditText DateText;
    private Button SendButton;
    private EditText TitleText;
    private ImageView ImageSelected;
    private EditText DescriptionText;
    private EditText PointsText;
    private EditText DirectionText;
    private EditText HourText;
    private EditText MinText;
    private EditText CompanyText;
    private Calendar calendar;
    private String FinalTime = null;
    static private String TAG = "EditEvent";
    private Event event;
    private String url = "http://10.4.41.145/api/events";

    /**
     * Required empty public constructor
     */
    public EditEventFragment() {
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
        /*
        View view = inflater.inflate(R.layout.create_event_fragment, container, false);
        id = getArguments().getInt("id");
        url += id;
        return view;
         */
        return inflater.inflate(R.layout.edit_event_fragment, container, false);
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
        try {
            new GetEvent().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //elements
        DateButton = (ImageButton) getView().findViewById(R.id.DateEditEvent);
        DateText = (EditText) getView().findViewById(R.id.editTextDateEditEvent);
        SendButton = (Button) getView().findViewById(R.id.buttonSendEditEvent);
        TitleText = (EditText) getView().findViewById(R.id.titleEditEvent_edit_text);
        DescriptionText = (EditText) getView().findViewById(R.id.DescriptionEditEvent_edit_text);
        PointsText = (EditText) getView().findViewById(R.id.PointsEditEvent_edit_text);
        DirectionText = (EditText) getView().findViewById(R.id.DirectionEditEvent_edit_text);
        HourText = (EditText) getView().findViewById(R.id.HourEditEvent_edit_text);
        MinText = (EditText) getView().findViewById(R.id.MinEditEvent_edit_text);
        CompanyText = (EditText) getView().findViewById(R.id.CompanyEditEvent_edit_text);
        ImageSelected = (ImageView) getView().findViewById(R.id.ImageSelectedEditEvent);

        //initialize
        TitleText.setText(event.getTitle());
        DescriptionText.setText(event.getDescription());
        PointsText.setText(event.getPoints().toString());
        DirectionText.setText(event.getDirection());
        CompanyText.setText(event.getCompany());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        DateText.setText(sdf.format(event.getDate()));
        if (Integer.parseInt(event.getHour()) < 10){
            String text = "0" + event.getHour();
            HourText.setText(text);
        }
        if (Integer.parseInt(event.getMin()) < 10){
            String text = "0" + event.getMin();
            MinText.setText(text);
        }
        if (event.getImage() != null) {
            byte[] decodedBytes = event.getImage();
            ImageSelected.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
        }

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

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class GetEvent extends AsyncTask<String, Void, Void> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param urls The parameters of the task.
         */
        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(),
                    session.getToken());
            Log.i(TAG, "Response from url: " + response);
            if (response != null) {
                try {
                    JSONObject aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("events");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    JSONObject jsonObject = jsonArray.getJSONObject(8);
                    String address = null;
                    if (!jsonObject.isNull("adress")) address = jsonObject.getString("adress");
                    String company = null;
                    if (!jsonObject.isNull("company")) address = jsonObject.getString("company");
                    String image = null;
                    if (!jsonObject.isNull("image")) address = jsonObject.getString("image");
                    event = new Event(jsonObject.getInt("id"),
                            jsonObject.getString("title"),
                            jsonObject.getString("description"),
                            jsonObject.getInt("points"),
                            address,
                            company,
                            df.parse(jsonObject.getString("date")),
                            image);
                    Log.d(TAG, "event created");
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }


}
