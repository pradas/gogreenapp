package pes.gogreenapp.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Objects.Shop;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Adry on 02/06/2017.
 */

public class ShopEditProfileFragment extends Fragment {

    private SessionManager session;
    private Activity activity;
    private Shop shop;
    private String TAG = MainActivity.class.getSimpleName();
    private ImageView shopImage;
    private EditText shopName;
    private EditText shopEmail;
    private EditText shopAddress;
    private Button saveEdit;
    private Bitmap profileImageBitmap;


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

        shopImage = (ImageView) getView().findViewById(R.id.shop_image_edit_profile);
        shopName = (EditText) getView().findViewById(R.id.shop_name_edit_profile);
        shopEmail = (EditText) getView().findViewById(R.id.shop_email_edit_profile);
        shopAddress = (EditText) getView().findViewById(R.id.shop_address_edit_profile);
        saveEdit = (Button) getView().findViewById(R.id.saveEditProfileButton);

        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("¿Está seguro de que desea modificar su perfil?").
                        setPositiveButton("MODIFICAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new PutShop().execute("http://10.4.41.145/api/shops/1", "PUT", shopName.getText().toString(),
                                        shopEmail.getText().toString(), shopAddress.getText().toString());
                                FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                Fragment fragment = (Fragment) new ShopProfileFragment();
                                transaction.replace(R.id.flContent, fragment);
                                transaction.commit();
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

        new GetInfoShop().execute("http://10.4.41.145/api/shops/1");
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
}
