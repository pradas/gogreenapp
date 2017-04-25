package pes.gogreenapp.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.user_creation_date;
import static pes.gogreenapp.R.id.user_image;
import static pes.gogreenapp.R.id.user_name;
import static pes.gogreenapp.R.id.user_nickname;
import static pes.gogreenapp.R.id.user_points;


public class UserProfilePublicFragment extends Fragment {
    User testUser;
    String url = "http://10.4.41.145/api/";
    public UserProfilePublicFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.user_profile_public_fragment, container, false);
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
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        TextView userName = (TextView) getView().findViewById(user_name);
        TextView userNickName = (TextView) getView().findViewById(user_nickname);
        TextView userPoints = (TextView) getView().findViewById(user_points);
        TextView userCreationDate = (TextView) getView().findViewById(user_creation_date);
        ImageView userImage = (ImageView) getView().findViewById(user_image);
        DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");

        //a la espera de tener la petición de la API hecha
        //new GetUserImage().execute("http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

        initializeUser();
        new UserProfilePublicFragment.GetInfoUser().execute();

        userName.setText(testUser.getName());
        userNickName.setText(testUser.getUsername());
        userPoints.setText(String.valueOf(testUser.getTotalPoints()));
        userCreationDate.setText((String) sourceFormat.format(testUser.getCreationDate()));
        //userCreationDate.setText(date);

        //userImage.setImageResource();



    }


    private void initializeUser(){
        testUser = new User("realPepeViyuela", "Pepe Viyuela", "viyuela@gmail.com", "12-10-1983", "http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

    }

    private class GetInfoUser extends AsyncTask<String, Void, Void> {
        Bitmap b_image_user;

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
            //HttpHandler httpHandler = new HttpHandler();
            //String response = httpHandler.makeServiceCall(urls[0]);
            //Log.i(TAG, "Response from url: " + response);

            URL imageUrl = null;
            try {
                imageUrl = new URL(testUser.getUserUrlImage());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            b_image_user = this.getRemoteImage(imageUrl);
            //testUser.setUserImage();

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            ImageView userImage = (ImageView) getView().findViewById(user_image);
            userImage.setImageBitmap(b_image_user);
        }

    }
}
