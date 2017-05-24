package pes.gogreenapp.Fragments;


import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;

import pes.gogreenapp.Objects.Event;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventFragment extends Fragment {
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    private SessionManager session;
    private ImageButton DateButton;
    private ImageButton PhotoButton;
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
    private List<String> categories = new ArrayList<String>();
    private Spinner categoriesSpinner;
    static private String TAG = "EditEvent";
    private Event event;
    private String url = "http://10.4.41.145/api/events/4";
    static private final String URLcategories = "http://10.4.41.145/api/categories";
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
        new GetCategories().execute(URLcategories);
        try {
            new GetEvent().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //elements
        DateButton = (ImageButton) getView().findViewById(R.id.DateEditEvent);
        PhotoButton = (ImageButton) getView().findViewById(R.id.ImageEditEventButton);
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
        categoriesSpinner = (Spinner) getView().findViewById(R.id.CategoriesSpinnerEdit);

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
        HourText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Integer.parseInt(HourText.getText().toString()) < 10) {
                        String text = "0" + HourText.getText().toString();
                        HourText.setText(text);
                    }
                }
            }
        });
        MinText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Integer.parseInt(MinText.getText().toString()) < 10) {
                        String text = "0" + MinText.getText().toString();
                        MinText.setText(text);
                    }
                }
            }
        });
        PhotoButton.setOnClickListener((View v) -> {
            isStoragePermissionGranted();
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
                String imgString = null;
                if (imgDecodableString != null && !imgDecodableString.isEmpty()) {
                    imgString = Base64.encodeToString(getBytesFromBitmap(BitmapFactory
                            .decodeFile(imgDecodableString)), Base64.NO_WRAP);
                }

                new PutEvent().execute(url, "PUT",
                        TitleText.getText().toString(),
                        DescriptionText.getText().toString(),
                        PointsText.getText().toString(),
                        DirectionText.getText().toString(),
                        CompanyText.getText().toString(),
                        DateText.getText().toString(),
                        FinalTime,
                        imgString,
                        String.valueOf(categoriesSpinner.getSelectedItem())
                );
            }
        });
    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class GetEvent extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param urls The parameters of the task.
         */
        @Override
        protected String doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(),
                    session.getToken());
            Log.i(TAG, "Response from url: " + response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String address = null;
                    if (!jsonObject.isNull("adress")) address = jsonObject.getString("adress");
                    String company = null;
                    if (!jsonObject.isNull("company")) company = jsonObject.getString("company");
                    String image = null;
                    if (!jsonObject.isNull("image")) image = jsonObject.getString("image");
                    Boolean favorite = false;
                    if (jsonObject.get("favourite") == "true") favorite = true;
                    event = new Event(jsonObject.getInt("id"),
                            jsonObject.getString("title"),
                            jsonObject.getString("description"),
                            jsonObject.getInt("points"),
                            address,
                            company,
                            df.parse(jsonObject.getString("date")),
                            image,
                            jsonObject.getString("category"),
                            favorite);
                    Log.d(TAG, "event created");
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            return "correct";
        }
        protected void onPostExecute(String result){
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
            else {
                HourText.setText(event.getHour());
            }
            if (Integer.parseInt(event.getMin()) < 10){
                String text = "0" + event.getMin();
                MinText.setText(text);
            }
            else {
                MinText.setText(event.getMin());
            }
            if (event.getImage() != null) {
                byte[] decodedBytes = event.getImage();
                ImageSelected.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                ImageSelected.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                cursor.close();
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
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(),
                    session.getToken());
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
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categoriesSpinner.setAdapter(dataAdapter);
        }

    }

    /**
     * Asynchronous Task for the petition POST to send a petition of register an User
     */
    private class PutEvent extends AsyncTask<String, Void, String> {
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
            BodyParams.put("title", params[2]);
            BodyParams.put("description", params[3]);
            BodyParams.put("points", params[4]);
            if (params[5] != null && !params[5].isEmpty()) BodyParams.put("adress", params[5]);
            if (params[6] != null && !params[6].isEmpty()) BodyParams.put("company", params[6]);
            BodyParams.put("date", params[7]);
            if (params[8] != null && !params[8].equals(":")) BodyParams.put("time", params[8]);
            else BodyParams.put("time", "00:00");
            if (params[9] != null) BodyParams.put("image", params[9]);
            BodyParams.put("category", params[10]);
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
                Toast.makeText(getActivity(), "Modificado perfectamente.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "No se ha podido modificar.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
