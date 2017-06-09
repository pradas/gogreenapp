package pes.gogreenapp.Fragments;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.AsyncHttpHandler;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {

    private static int RESULT_LOAD_IMG = 1;
    private SessionManager session;
    String imgDecodableString;
    private ImageButton DateButton;
    private ImageView ImageSelected;
    private ImageButton PhotoButton;
    private ImageButton HourButton;
    private EditText DateText;
    private Button SendButton;
    private EditText TitleText;
    private EditText DescriptionText;
    private EditText PointsText;
    private EditText DirectionText;
    private EditText HourText;
    private EditText CompanyText;
    private Calendar calendar;
    private List<String> categories = new ArrayList<String>();
    private Spinner categoriesSpinner;
    static private String TAG = "CreateEvent";
    static private final String URLcategories = "http://10.4.41.145/api/categories";
    private int hour, min;

    public boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat
                        .requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    /**
     * Required empty public constructor
     */
    public CreateEventFragment() {

    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given
     *                           here.
     *
     * @return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Crear evento");
        return inflater.inflate(R.layout.create_event_fragment, container, false);
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        session = SessionManager.getInstance();
        new GetCategories().execute(URLcategories);

        //elements
        DateButton = (ImageButton) getView().findViewById(R.id.DateCreateEvent);
        PhotoButton = (ImageButton) getView().findViewById(R.id.ImageCreateEventButton);
        ImageSelected = (ImageView) getView().findViewById(R.id.ImageSelectedCreateEvent);
        DateText = (EditText) getView().findViewById(R.id.editTextDateCreateEvent);
        TitleText = (EditText) getView().findViewById(R.id.titleCreateEvent_edit_text);
        DescriptionText = (EditText) getView().findViewById(R.id.DescriptionCreateEvent_edit_text);
        PointsText = (EditText) getView().findViewById(R.id.PointsCreateEvent_edit_text);
        DirectionText = (EditText) getView().findViewById(R.id.DirectionCreateEvent_edit_text);
        HourText = (EditText) getView().findViewById(R.id.HourCreateEventEditText);
        CompanyText = (EditText) getView().findViewById(R.id.CompanyCreateEvent_edit_text);
        categoriesSpinner = (Spinner) getView().findViewById(R.id.CategoriesSpinner);
        SendButton = (Button) getView().findViewById(R.id.buttonSendCreateEvent);
        HourButton = (ImageButton) getView().findViewById(R.id.HourCreateEventButton);


        SendButton.setOnClickListener((View v) -> {
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
                    send = false;
                }
            }

            if (HourText.getText().toString().length() <= 0) {
                HourText.setError("Hora necesaria");
                send = false;
            }
            else if (HourText.getText().toString().length() > 0) {
                String hour = HourText.getText().toString();
                String[] hourDivided = hour.split(":");

                if ((Integer.parseInt(hourDivided[0]) < 0) || (Integer.parseInt(hourDivided[0]) >= 24)) {
                    HourText.setError("Hora incorrecta");
                    send = false;
                }
                if ((Integer.parseInt(hourDivided[1]) < 0) || (Integer.parseInt(hourDivided[1]) >= 60)) {
                    HourText.setError("Hora incorrecta");
                    send = false;
                }
            }
            if (send) {
                Log.d("CreateEvent", "se envia");
                String imgString = null;
                if (imgDecodableString != null && !imgDecodableString.isEmpty()) {
                    imgString = Base64.encodeToString(getBytesFromBitmap(BitmapFactory.decodeFile(imgDecodableString)),
                            Base64.NO_WRAP);
                } else {
                    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.event);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imgString = Base64.encodeToString(getBytesFromBitmap(icon), Base64.NO_WRAP);

                    Log.i(TAG, "default event image bytecoded: " + imgString);
                }

                RequestParams requestParams = new RequestParams();
                requestParams.put("title", TitleText.getText());
                requestParams.put("description", DescriptionText.getText());
                requestParams.put("points", PointsText.getText());
                requestParams.put("adress", DirectionText.getText());
                requestParams.put("company", CompanyText.getText());
                requestParams.put("date", DateText.getText());
                requestParams.put("time", HourText.getText().toString());
                requestParams.put("image", imgString);
                requestParams.put("category", String.valueOf(categoriesSpinner.getSelectedItem()));
                AsyncHttpHandler.post("shops/" + String.valueOf(session.getShopId()) + "/events", requestParams,
                        new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                // Handle resulting parsed JSON response here
                                Toast.makeText(getActivity(), "evente creado", Toast.LENGTH_SHORT).show();
                                try {
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.flContent, EventsListShopFragment.class.newInstance())
                                            .addToBackStack(EventsListShopFragment.class.getName())
                                            .commit();
                                } catch (java.lang.InstantiationException | IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String xml, Throwable throwable) {
                                // called when response HTTP status is "4XX"
                                Log.e("API_ERROR", String.valueOf(statusCode) + " " + throwable.getMessage());
                                Toast.makeText(getActivity(), "No se ha podido crear.", Toast.LENGTH_SHORT).show();
                            }
                });
            }
        });
        //events
        DateText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                calendar = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        (DatePicker view, int year, int monthOfYear, int dayOfMonth) -> {
                            String sDayOfMonth = String.format("%02d", dayOfMonth);
                            String sMonthOfYear = String.format("%02d", monthOfYear + 1);
                            DateText.setText(sDayOfMonth + "-" + sMonthOfYear + "-" + year);
                            DateText.clearFocus();
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dpd.show();
            }
        });
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

        HourText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                min = c.get(Calendar.MINUTE);

                TimePickerDialog picker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute < 10) HourText.setText(hourOfDay + ":0" + minute);
                        else HourText.setText(hourOfDay + ":" + minute);
                    }
                }, hour, min, true);
                picker.show();
            }
        });

        HourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                min = c.get(Calendar.MINUTE);

                TimePickerDialog picker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute < 10) HourText.setText(hourOfDay + ":0" + minute);
                        else HourText.setText(hourOfDay + ":" + minute);
                    }
                }, hour, min, true);
                picker.show();
            }
        });

        PhotoButton.setOnClickListener((View v) -> {
            isStoragePermissionGranted();
            Intent galleryIntent =
                    new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor =
                        getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                ImageSelected.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                cursor.close();
            } else {
                Toast.makeText(getContext(), "No has escogido ninguna imagen", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("CreateEvent", e.toString());
            Toast.makeText(getContext(), "Error al escoger la imagen", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Asynchronous Task for the petition GET of all the Categories.
     */
    private class GetCategories extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param urls The parameters of the task.
         */
        @Override
        protected String doInBackground(String... urls) {

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(), session.getToken());
            Log.i(TAG, "Response from url: " + response);
            if (response != null) {
                JSONObject aux;
                try {
                    aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        categories.add((String) jsonObject.get("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "correcte";
            }
            return "falla";
        }

        /**
         * Called when doInBackground is finished, Toast an error if there is an error.
         *
         * @param result If is "Falla" makes the toast.
         */
        protected void onPostExecute(String result) {

            ArrayAdapter<String> dataAdapter =
                    new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categoriesSpinner.setAdapter(dataAdapter);
        }

    }

}
