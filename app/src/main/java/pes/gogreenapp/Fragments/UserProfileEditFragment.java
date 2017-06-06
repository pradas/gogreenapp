package pes.gogreenapp.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    private User user;
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
    private TextView userBirthDate;
    private TextView userCurrentPoints;
    private EditText userEmail;
    DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static Integer mYear, mMonth, mDay;

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

        new GetInfoUser().execute("http://10.4.41.145/api/users/" + session.getUsername());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("¿Está seguro de que desea modificar su perfil?").
                        setPositiveButton("MODIFICAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new PutUser().execute("http://10.4.41.145/api/users/", "PUT", userName.getText().toString(),
                                        userBirthDate.getText().toString(), userEmail.getText().toString());
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

            userName.setText(user.getName());
            userNickName.setText("Nickname: " + user.getUsername());
            userTotalPoints.setText("Puntos totales: " + String.valueOf(user.getTotalPoints()));
            userCurrentPoints.setText("Puntos actuales: " + String.valueOf(user.getCurrentPoints()));
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
         *               params[2] is the username or email for identification in the login and
         *               params[3] is the password to identification in the login
         * @return "Falla" si no es un login correcte o "Correcte" si ha funcionat
         */
        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("name", params[2]);
            bodyParams.put("birth_date", params[3]);
            bodyParams.put("email", params[4]);
            String url = params [0] + session.getUsername();
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

