package pes.gogreenapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Adry on 24/05/2017.
 */

public class UserProfileEditFragment extends Fragment {

    private static int RESULT_LOAD_IMG = 1;
    private User user;
    String imgDecodableString;
    private Activity activity;
    private String TAG = MainActivity.class.getSimpleName();
    private SessionManager session;
    private ImageView userImage;
    private EditText userName;
    private TextView userNickName;
    private TextView userTotalPoints;
    private TextView userCreationDate;
    private Button editBirthdate;
    private Button saveButton;
    private ImageButton editPicture;
    private TextView userBirthDate;
    private TextView userCurrentPoints;
    private EditText userEmail;
    DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static Integer mYear, mMonth, mDay;
    private Bitmap profileImageBitmap;

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
    public UserProfileEditFragment() {
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
        getActivity().setTitle("Editar Perfil");
        return inflater.inflate(R.layout.user_profile_edit_fragment, container, false);
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        session = SessionManager.getInstance();

        userImage = (ImageView) getView().findViewById(R.id.user_image_edit_user);
        userName = (EditText) getView().findViewById(R.id.user_name_edit_user);
        userNickName = (TextView) getView().findViewById(R.id.user_nickname_edit_user);
        userTotalPoints = (TextView) getView().findViewById(R.id.user_total_points_edit_user);
        userCreationDate = (TextView) getView().findViewById(R.id.gobro_since_edit_user);
        editBirthdate = (Button) getView().findViewById(R.id.edit_birthdate_user_button);
        saveButton = (Button) getView().findViewById(R.id.save_edit_profile_button);
        userBirthDate = (TextView) getView().findViewById(R.id.birthdate_edit_user);
        userCurrentPoints = (TextView) getView().findViewById(R.id.user_current_points_edit_user);
        userEmail = (EditText) getView().findViewById(R.id.user_email_edit_user);
        editPicture = (ImageButton) getView().findViewById(R.id.imageEditUser);

        new GetInfoUser().execute("http://10.4.41.145/api/users/" + session.getUsername());

        editPicture.setOnClickListener((View v) -> {
            //check if has permission
            if(isStoragePermissionGranted()) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("¿Está seguro de que desea modificar su perfil?").
                        setPositiveButton("MODIFICAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String imgString = null;
                                if (imgDecodableString != null && !imgDecodableString.isEmpty()) {
                                    imgString = Base64.encodeToString(getBytesFromBitmap(BitmapFactory
                                            .decodeFile(imgDecodableString)), Base64.NO_WRAP);
                                }

                                new PutUser().execute("http://10.4.41.145/api/users/", "PUT", userName.getText().toString(),
                                        userBirthDate.getText().toString(), userEmail.getText().toString(), imgString);
                                FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                Fragment fragment = (Fragment) new UserProfileFragment();
                                transaction.replace(R.id.flContent, fragment).addToBackStack( "tag" ).commit();
                            }
                        })
                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        editBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                                userBirthDate.setText(sDayOfMonth + "-" + sMonthOfYear + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                Calendar calendar = Calendar.getInstance();
                calendar.set(mYear,mMonth,mDay);
                dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                dpd.show();
            }
        });
    }

    /**
     * Asynchronous Task for the petition GET of a User.
     */
    private class GetInfoUser extends AsyncTask<String, Void, Void> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param urls urls[0] is the petition url,
         */
        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET" , new HashMap<>(),
                    session.getToken());
            Log.i(TAG, "Response from url: " + response);

            URL imageUrl = null;
            try {

                JSONObject jsonArray = new JSONObject(response);

                user = new User(jsonArray.getString("username"),
                        jsonArray.getString("name"),
                        jsonArray.getString("email"),
                        jsonArray.getString("birth_date"),
                        jsonArray.getString("image"),
                        jsonArray.getInt("total_points"),
                        jsonArray.getInt("points"),
                        jsonArray.getString("created_at"));

                String image = jsonArray.getString("image");

                byte[] imageData = Base64.decode(image, Base64.DEFAULT);
                profileImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * Called when doInBackground is finished.
         *
         * @param result set the values in all the edittext and textviews
         */
        @Override
        protected void onPostExecute(Void result) {
            userImage.setImageBitmap(profileImageBitmap);

            userName.setText(user.getName());
            userNickName.setText("Username : "  + user.getUsername());
            userTotalPoints.setText(String.valueOf(user.getTotalPoints()));
            userCurrentPoints.setText(String.valueOf(user.getCurrentPoints()));
            userCreationDate.setText("GoBro desde: " + (String) sourceFormat.format(user.getCreationDate()));
            userBirthDate.setText((String) sourceFormat.format(user.getBirthDate()));
            userEmail.setText(user.getEmail());

        }

    }

    /**
     * Asynchronous Task for the petition PUT of a User.
     */
    private class PutUser extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the method petition,
         *               params[2] is the new name of the user
         *               params[3] is the new birth-date of the user
         *               params[4] is the new email of the user
         *               params[5] is the new image of the user
         * @return "Falla" si no es un login correcte o "Correcte" si ha funcionat
         */
        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("name", params[2]);
            bodyParams.put("birth_date", params[3]);
            bodyParams.put("email", params[4]);
            bodyParams.put("image", params[5]);
            String url = params [0] + session.getUsername();
            session = SessionManager.getInstance();
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null && !response.equals("500") ) {
                return "Correct";
            }
            return "Error";
        }

        /**
         * Called when doInBackground is finished.
         *
         * @param result makes a toast with the result
         */
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(activity, "Error al editar el perfil. Intentelo de nuevo mas tarde",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(activity, "Perfil actualizado", Toast.LENGTH_LONG).show();
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
                userImage.setImageBitmap(BitmapFactory
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

}

