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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateOfertaFragment extends Fragment {
    //initialitions
    private static int RESULT_LOAD_IMG = 1;
    private SessionManager session;
    String imgDecodableString;
    private ImageView ImageSelected;
    private ImageButton PhotoButton;
    private ImageButton DateButton;
    private EditText DateText;
    private Button SendButton;
    private EditText TitleText;
    private EditText DescriptionText;
    private EditText DiscountText;
    private Calendar calendar;
    static private String TAG = "CreateOferta";
    private String URLPetition;

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
        getActivity().setTitle("Nueva Oferta");
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
        URLPetition =  "http://10.4.41.145/api/shops/" + String.valueOf(session.getShopId()) + "/deals";
        //elements
        DateButton = (ImageButton) getView().findViewById(R.id.DateCreateOferta);
        PhotoButton = (ImageButton) getView().findViewById(R.id.ImageCreateOfertaButton);
        ImageSelected = (ImageView) getView().findViewById(R.id.ImageSelectedCreateOferta);
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
        PhotoButton.setOnClickListener((View v) -> {
            if (isStoragePermissionGranted()) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
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
                String imgString = null;
                if (imgDecodableString != null && !imgDecodableString.isEmpty()) {
                    imgString = Base64.encodeToString(getBytesFromBitmap(BitmapFactory
                            .decodeFile(imgDecodableString)), Base64.NO_WRAP);
                }
                else {
                    Bitmap icon = BitmapFactory.decodeResource(getResources(),
                            R.drawable.oferta);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imgString= Base64.encodeToString(getBytesFromBitmap(icon), Base64.NO_WRAP);

                    Log.i(TAG, "default event image bytecoded: " + imgString);
                }
                Log.d(TAG, URLPetition);
                new PostOferta().execute(URLPetition, "POST",
                        TitleText.getText().toString(),
                        DescriptionText.getText().toString(),
                        DiscountText.getText().toString(),
                        DateText.getText().toString(),
                        imgString
                );
            }
        });
    }


    /**
     * Asynchronous Task for the petition POST to send a petition to create a deal
     */
    private class PostOferta extends AsyncTask<String, Void, String> {
        @Override
        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the method,
         *               params[2] is the name,
         *               params[3] is the description
         *               params[4] is the value
         *               params[5] is the date
         *               params[6] is the image
         *
         * @return the result of the petition
         */
        protected String doInBackground(String... params) {
            HashMap<String, String> BodyParams = new HashMap<>();
            BodyParams.put("name", params[2]);
            BodyParams.put("description", params[3]);
            BodyParams.put("value", params[4]);
            BodyParams.put("date", params[5]);
            if (params[6] != null) BodyParams.put("image", params[6]);
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
            } else if (s.contains("Deal created successfully.")) {
                Toast.makeText(getActivity(), "Creado perfectamente.", Toast.LENGTH_SHORT).show();

                FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = (Fragment) new OfertasListShopFragment();
                transaction.replace(R.id.flContent, fragment).addToBackStack( "tag" ).commit();
            } else {
                Toast.makeText(getActivity(), "No se ha podido crear.", Toast.LENGTH_SHORT).show();
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
            Log.d(TAG, e.toString());
            Toast.makeText(getContext(), "Error al escoger la imagen", Toast.LENGTH_LONG)
                    .show();
        }
    }




}
