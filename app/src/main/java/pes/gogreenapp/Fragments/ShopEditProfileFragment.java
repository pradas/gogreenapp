package pes.gogreenapp.Fragments;

import android.Manifest;
import android.app.Activity;
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
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Objects.Shop;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Adry on 02/06/2017.
 */

public class ShopEditProfileFragment extends Fragment {

    private static int RESULT_LOAD_IMG = 1;
    private SessionManager session;
    private Activity activity;
    private Shop shop;
    private String TAG = MainActivity.class.getSimpleName();
    private ImageView shopImage;
    private EditText shopName;
    private EditText shopEmail;
    private EditText shopAddress;
    private ImageButton editPicture;
    private Button saveEdit;
    private Bitmap profileImageBitmap;
    String imgDecodableString;

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
    public ShopEditProfileFragment() {
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
        getActivity().setTitle("Tienda");
        return inflater.inflate(R.layout.shop_edit_profile_fragment, container, false);
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        session = SessionManager.getInstance();
        this.activity = getActivity();

        shopImage = (ImageView) getView().findViewById(R.id.shop_image);
        shopName = (EditText) getView().findViewById(R.id.shop_name_edit_profile);
        shopEmail = (EditText) getView().findViewById(R.id.shop_email_edit_profile);
        shopAddress = (EditText) getView().findViewById(R.id.shop_address_edit_profile);
        saveEdit = (Button) getView().findViewById(R.id.saveEditProfileButton);
        editPicture = (ImageButton) getView().findViewById(R.id.shop_image_edit_profile);

        editPicture.setOnClickListener((View v) -> {
            //check if has permission
            if(isStoragePermissionGranted()) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        saveEdit.setOnClickListener(new View.OnClickListener() {
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

                                new PutShop().execute("http://10.4.41.145/api/shops/" + session.getShopId(), "PUT", shopName.getText().toString(),
                                        shopEmail.getText().toString(), shopAddress.getText().toString(), imgString);
                                FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                Fragment fragment = (Fragment) new ShopProfileContainerFragment();
                                transaction.replace(R.id.shopProfile, fragment).addToBackStack( "tag" ).commit();
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

        new GetInfoShop().execute("http://10.4.41.145/api/shops/" + session.getShopId());
    }

    private class GetInfoShop extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET" , new HashMap<>(),
                    session.getToken());
            Log.i(TAG, "Response from url: " + response);

            try {

                JSONObject jsonArray = new JSONObject(response);

                shop = new Shop(jsonArray.getString("image"), jsonArray.getString("name"),
                        jsonArray.getString("email"), jsonArray.getString("address"));

                String image = jsonArray.getString("image");

                byte[] imageData = Base64.decode(image, Base64.DEFAULT);
                profileImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            shopImage.setImageBitmap(profileImageBitmap);
            shopName.setText(shop.getShopName());
            shopEmail.setText(shop.getShopEmail());
            shopAddress.setText(shop.getShopAddress());
        }
    }

    /**
     * Asynchronous Task for the petition PUT of a User.
     */
    private class PutShop extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the method petition,
         *               params[2] is the username or email for identification in the login and
         *               params[3] is the password to identification in the login
         * @return "Falla" si no es un login correcte o "Correcte" si ha funcionat
         */
        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("name", params[2]);
            bodyParams.put("email", params[3]);
            bodyParams.put("address", params[4]);
            bodyParams.put("image", params[5]);
            String url = params [0];
            session = SessionManager.getInstance();
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null && !response.equals("500") ) {
                return "Correct";
            }
            return "Error";
        }

        /**
         * Called when doInBackground is finished, Toast an error if there is an error.
         *
         * @param result If is "Falla" makes the toast.
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
                shopImage.setImageBitmap(BitmapFactory
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
