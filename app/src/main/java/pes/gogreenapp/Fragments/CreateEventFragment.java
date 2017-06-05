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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {
    //initialitions
    private static int RESULT_LOAD_IMG = 1;
    private SessionManager session;
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
    private Calendar calendar;
    private List<String> categories = new ArrayList<String>();
    private String FinalTime = null;
    private Spinner categoriesSpinner;
    static private String TAG = "CreateEvent";
    static private String URLPetition = "http://10.4.41.145/api/shops/";
    static private final String URLcategories = "http://10.4.41.145/api/categories";

    /**
     * Checks if the user accepts that the app to read external storage
     *
     * @return true if has permission or false if not
     */
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

    /**
     * Convert Bitmap to byte[]
     * @param bitmap    Image in bitmap format
     * @return Image converted to bitmap
     */
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
        return inflater.inflate(R.layout.create_event_fragment, container, false);
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
        URLPetition = URLPetition + String.valueOf(session.getShopId()) + "/events";
        new GetCategories().execute(URLcategories);

        //elements
        DateButton = (ImageButton) getView().findViewById(R.id.DateCreateEvent);
        PhotoButton = (ImageButton) getView().findViewById(R.id.ImageCreateEventButton);
        ImageSelected = (ImageView) getView().findViewById(R.id.ImageSelectedCreateEvent);
        DateText = (EditText) getView().findViewById(R.id.editTextDateCreateEvent);
        SendButton = (Button) getView().findViewById(R.id.buttonSendCreateEvent);
        TitleText = (EditText) getView().findViewById(R.id.titleCreateEvent_edit_text);
        DescriptionText = (EditText) getView().findViewById(R.id.DescriptionCreateEvent_edit_text);
        PointsText = (EditText) getView().findViewById(R.id.PointsCreateEvent_edit_text);
        DirectionText = (EditText) getView().findViewById(R.id.DirectionCreateEvent_edit_text);
        HourText = (EditText) getView().findViewById(R.id.HourCreateEvent_edit_text);
        MinText = (EditText) getView().findViewById(R.id.MinCreateEvent_edit_text);
        categoriesSpinner = (Spinner) getView().findViewById(R.id.CategoriesSpinner);

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
            //check if has permission
            if(isStoragePermissionGranted()) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        SendButton.setOnClickListener(v -> {
            //check all the conditions
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

            }
            //if all conditions are true, send
            if (send) {
                Log.d("CreateEvent", "se envia");
                String imgString = null;
                if (imgDecodableString != null && !imgDecodableString.isEmpty()) {
                    imgString = Base64.encodeToString(getBytesFromBitmap(BitmapFactory
                            .decodeFile(imgDecodableString)), Base64.NO_WRAP);
                }
                Log.d(TAG, URLPetition);
                new PostEvent().execute(URLPetition, "POST",
                        TitleText.getText().toString(),
                        DescriptionText.getText().toString(),
                        PointsText.getText().toString(),
                        DirectionText.getText().toString(),
                        DateText.getText().toString(),
                        FinalTime,
                        imgString,
                        String.valueOf(categoriesSpinner.getSelectedItem())
                );
            }
        });
    }


    /**
     * Asynchronous Task for the petition POST to send a petition of create Event
     */
    private class PostEvent extends AsyncTask<String, Void, String> {
        @Override
        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the method,
         *               params[2] is the title,
         *               params[3] is the description
         *               params[4] is the points
         *               params[5] is the address
         *               params[6] is the date
         *               params[7] is the time
         *               params[8] is the image
         *               params[9] is the category
         *
         * @return the result of the petition
         */
        protected String doInBackground(String... params) {
            HashMap<String, String> BodyParams = new HashMap<>();
            BodyParams.put("title", params[2]);
            BodyParams.put("description", params[3]);
            BodyParams.put("points", params[4]);
            if (params[5] != null && !params[5].isEmpty()) BodyParams.put("adress", params[5]);
            BodyParams.put("date", params[6]);
            if (params[7] != null && !params[7].equals(":")) BodyParams.put("time", params[7]);
            else BodyParams.put("time", "00:00");
            if (params[8] != null) BodyParams.put("image", params[8]);
            BodyParams.put("category", params[9]);
            String result = new HttpHandler().makeServiceCall(params[0], params[1], BodyParams,
                    session.getToken());
            Log.i(TAG, "Response from url: " + result);

            return result;
        }

        @Override
        /**
         * Executed after doInBackground()
         *
         * @params s is the result of the petition
         *
         * @return void
         */
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(getActivity(), "Error, no se ha podido conectar, intentelo de nuevo más tarde", Toast.LENGTH_LONG).show();
            } else if (s.contains("Event created successfully.")) {
                Toast.makeText(getActivity(), "Creado perfectamente.", Toast.LENGTH_LONG).show();

                FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = (Fragment) new EventsListShopFragment();
                transaction.replace(R.id.flContent, fragment);
                transaction.commit();
            } else {
                Toast.makeText(getActivity(), "No se ha podido crear.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    /**
     * Get the result of the image selected
     *
     * @params  requestCode is 1
     *          resultCode is -1
     *          data is the image path
     */
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

}
