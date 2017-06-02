package pes.gogreenapp.Fragments;


import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;

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
import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Adrian on 02/06/2017.
 */

public class ShopProfileFragment extends Fragment {

    private SessionManager session;
    private static final String ROLE_MANAGER = "manager";
    private static final String ROLE_SHOPPER = "shopper";
    private static final String ROLE_USER = "user";
    private String TAG = MainActivity.class.getSimpleName();
    private Shop shop;
    private ImageView shopImage;
    private TextView shopName;
    private TextView shopEmail;
    private TextView shopAddress;
    private Button editProfile;

    /**
     * Required empty public constructor
     */
    public ShopProfileFragment () {
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
        return inflater.inflate(R.layout.shop_profile_fragment, container, false);
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

        shopImage = (ImageView) getView().findViewById(R.id.shop_image);
        shopName = (TextView) getView().findViewById(R.id.shop_name);
        shopEmail = (TextView) getView().findViewById(R.id.shop_email);
        shopAddress = (TextView) getView().findViewById(R.id.shop_address);
        editProfile = (Button) getView().findViewById(R.id.editProfileShopButton);
        if ((session.getRole().equals(ROLE_USER) )|| (session.getRole().equals(ROLE_SHOPPER))) editProfile.setVisibility(View.GONE);
        else {
            editProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        new GetInfoShop().execute("http://10.4.41.145/api/shops/1");
    }

    private class GetInfoShop extends AsyncTask<String, Void, Void> {
        Bitmap b_image_shop;

        private Bitmap getRemoteImage(final URL aURL) {
            try {
                final URLConnection conn = aURL.openConnection();
                conn.connect();
                final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                final Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            } catch (IOException e) {}
            return null;
        }


        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET" , new HashMap<>(),
                    session.getToken());
            Log.i(TAG, "Response from url: " + response);

            URL imageUrl = null;
            try {

                JSONObject jsonArray = new JSONObject(response);

                shop = new Shop(jsonArray.getString("image"), jsonArray.getString("name"),
                        jsonArray.getString("email"), jsonArray.getString("address"));

                imageUrl = new URL(jsonArray.getString("image"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (imageUrl != null)b_image_shop = this.getRemoteImage(imageUrl);

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if(shop.getShopUrlImage() != null) shopImage.setImageBitmap(b_image_shop);
            else shopImage.setImageBitmap(null);

            shopName.setText("Nombre de la tienda: " + shop.getShopName());
            shopEmail.setText("Email: " + shop.getShopEmail());
            shopAddress.setText("Direccion: " + shop.getShopAddress());
        }
    }
}
