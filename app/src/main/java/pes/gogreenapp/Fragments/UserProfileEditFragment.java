package pes.gogreenapp.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Adry on 24/05/2017.
 */

public class UserProfileEditFragment extends Fragment {

    User user;
    private String TAG = MainActivity.class.getSimpleName();
    SessionManager session;

    public UserProfileEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.user_profile_edit_fragment, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        session = SessionManager.getInstance();

        //new GetInfoUser().execute();

        ImageView userImage = (ImageView) getView().findViewById(R.id.user_image_edit_user);
        EditText userName = (EditText) getView().findViewById(R.id.user_name_edit_user);
        TextView userNickName = (TextView) getView().findViewById(R.id.user_nickname_edit_user);
        TextView userPoints = (TextView) getView().findViewById(R.id.user_points_edit_user);
        TextView userCreationDate = (TextView) getView().findViewById(R.id.gobro_since_edit_user);
        Button editBirthdate = (Button) getView().findViewById(R.id.edit_birthdate_user);
        Button saveButton = (Button) getView().findViewById(R.id.save_edit_profile_button);
        TextView userBirthDate = (TextView) getView().findViewById(R.id.user_birthdate_edit_user);
        TextView userCurrentPoints = (TextView) getView().findViewById(R.id.actual_points);
        EditText userEmail = (EditText) getView().findViewById(R.id.user_email_edit_user);

        DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");

        userName.setText(user.getName());
        userNickName.setText(user.getUsername());
        userPoints.setText(String.valueOf(user.getTotalPoints()));
        userCreationDate.setText((String) sourceFormat.format(user.getCreationDate()));
        userBirthDate.setText((String) sourceFormat.format(user.getBirthDate()));
        userCurrentPoints.setText(String.valueOf(user.getCurrentPoints()));
        userEmail.setText(user.getEmail());
        //userImage.setImageResource();



    }

    private class GetPublicInfoUser extends AsyncTask<String, Void, Void> {
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
                        jsonArray.getInt("points")),
                        jsonArray.getString("created_at");

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

        }

    }

}

