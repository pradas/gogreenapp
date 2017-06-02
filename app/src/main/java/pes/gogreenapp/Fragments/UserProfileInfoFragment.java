package pes.gogreenapp.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Adrian on 24/05/2017.
 */

public class UserProfileInfoFragment extends Fragment {

    private SessionManager session;
    private String TAG = MainActivity.class.getSimpleName();
    private ImageView userImage;
    private TextView userName;
    private TextView userNickName;
    private TextView userTotalPoints;
    private TextView userActualPoints;
    private TextView userCreationDate;
    private TextView userBirthDate;
    private TextView userEmail;
    DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");

    private User user;


    /**
     *  Required empty public constructor
     */
    public UserProfileInfoFragment(){
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
        return inflater.inflate(R.layout.user_profile_info_fragment, container, false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = SessionManager.getInstance();

        userImage = (ImageView) getView().findViewById(R.id.user_image);
        userName = (TextView) getView().findViewById(R.id.user_name);
        userNickName = (TextView) getView().findViewById(R.id.user_nickname);
        userTotalPoints = (TextView) getView().findViewById(R.id.user_total_points);
        userActualPoints = (TextView) getView().findViewById(R.id.user_actual_points);
        userCreationDate = (TextView) getView().findViewById(R.id.gobro_since);
        userBirthDate = (TextView) getView().findViewById(R.id.user_birthdate);
        userEmail = (TextView) getView().findViewById(R.id.user_email);
        ImageButton editUser = (ImageButton) getView().findViewById(R.id.edit_profile_button);

        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = (Fragment) new UserProfileEditFragment();
                transaction.replace(R.id.flContent, fragment);
                transaction.commit();
            }
        });

        new GetInfoUser().execute("http://10.4.41.145/api/users/" + session.getUsername());

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

                imageUrl = new URL(jsonArray.getString("image"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (imageUrl != null)b_image_user = this.getRemoteImage(imageUrl);

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if(user.getUserUrlImage() != null) userImage.setImageBitmap(b_image_user);
            else userImage.setImageBitmap(null);

            userName.setText("Nombre: " + user.getName());
            userNickName.setText("Nickname: " + user.getUsername());
            userTotalPoints.setText("Puntos totales: " + String.valueOf(user.getTotalPoints()));
            userActualPoints.setText("Puntos actuales: " + String.valueOf(user.getCurrentPoints()));
            userCreationDate.setText("GoBro desde: " + (String) sourceFormat.format(user.getCreationDate()));
            userBirthDate.setText("Fecha de nacimiento: " + (String) sourceFormat.format(user.getBirthDate()));
            userEmail.setText("Email: " + user.getEmail());

        }

    }
}
