package pes.gogreenapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Exceptions.NullParametersException;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;
import pes.gogreenapp.Utils.UserData;

/**
 * Created by Jorge Alvarez on 07/04/17.
 */

public class RegisterFragment extends Fragment {
    private SessionManager session;
    public static boolean testMode = false;
    private static Integer mYear, mMonth, mDay;
    private static final String TAG = "submitUserTag";
    private static final String URLPetition = "http://raichu.fib.upc.edu/api/users";
    RequestQueue mRequestQueue;

    @Override
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

        getView().findViewById(R.id.editFechaNacimiento).setOnKeyListener(null);
        final ImageButton pickDate = (ImageButton) getView().findViewById(R.id.datePickerButton);
        final EditText textView = (EditText) getView().findViewById(R.id.editFechaNacimiento);
        final Button createUserButton = (Button) getView().findViewById(R.id.create_user_button);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // myCalendar.add(Calendar.DATE, 0);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                textView.setText(sdf.format(myCalendar.getTime()));
            }


        };
        createUserButton.setOnClickListener(v -> {
            EditText password = (EditText) getView().findViewById(R.id.editContraseña);
            EditText password2 = (EditText) getView().findViewById(R.id.editContraseñaConfirmar);
            EditText username = (EditText) getView().findViewById(R.id.editUsername);
            EditText name = (EditText) getView().findViewById(R.id.editName);
            EditText email = (EditText) getView().findViewById(R.id.editEmail);
            EditText birthdayDate = (EditText) getView().findViewById(R.id.editFechaNacimiento);
            boolean ok = true;
            int passwordok = 0;
            if (username.getText().toString().isEmpty()) {
                username.setError("Campo necesario");
                ok = false;
            }

            if (name.getText().toString().isEmpty()) {
                name.setError("Campo necesario");
                ok = false;
            }

            if (password.getText().toString().isEmpty()) {
                password.setError("Campo necesario");
                ok = false;
                passwordok++;
            }

            if (password2.getText().toString().isEmpty()) {
                password2.setError("Campo necesario");
                ok = false;
                passwordok++;
            }
            if (passwordok == 0 && !password.getText().toString().equals(password2.getText().toString())) {
                password.setError("La contraseña no es la misma");
            }

            if (email.getText().toString().isEmpty()) {
                email.setError("Campo necesario");
                ok = false;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                email.setError("Email no valido");
                ok = false;
            }

            if (birthdayDate.getText().toString().isEmpty()) {
                birthdayDate.setError("Campo necesario");
                ok = false;
            }

            if (ok) {
                if (!testMode)
                    new PostMethod().execute(URLPetition, username.getText().toString(), name.getText().toString(), email.getText().toString(), password.getText().toString(), birthdayDate.getText().toString());
            }

        });
        ((ImageButton) getView().findViewById(R.id.imageBackRegisterToLogin)).setOnClickListener((View v)->{
            try {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_login, LoginFragment.class.newInstance())
                        .commit();
            } catch (java.lang.InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        pickDate.setOnClickListener((View v) -> {
            // TODO Auto-generated method stub
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR) - 18;
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
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
                            String sDayOfMonth = String.format("%02d", dayOfMonth);
                            String sMonthOfYear = String.format("%02d", monthOfYear + 1);
                            textView.setText(sDayOfMonth + "-"
                                    + sMonthOfYear + "-" + year);
                            textView.setError(null);
                            textView.clearFocus();

                        }
                    }, mYear, mMonth, mDay);
            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            dpd.show();
        });
    }

    @Nullable
    @Override
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param menu               If non-null, the menu will be re-constructed applied.
     *                           If null the menu will be destroyed
     * @param inflater           The MenuInflater object that can be used to inflate any views in
     *                           the fragment.
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register, menu);
    }

    @Override
    /**
     * Override default onStop to apply a interruption of Petitions
     */
    public void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
    /**
     * Asynchronous Task for the petition POST to send a petition of register an User
     */
    private class PostMethod extends AsyncTask<String, Void, String> {
        @Override
        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the username,
         *               params[2] is the name of the user
         *               params[3] is the email
         *               params[4] is the password
         *               params[5] is the birthday date
         * @return void when finished
         */
        protected String doInBackground(String... params) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
            HashMap<String, String> impl = new HashMap<>();
            impl.put("username", params[1]);
            impl.put("name", params[2]);
            impl.put("email", params[3]);
            impl.put("password", params[4]);
            impl.put("birth_date", params[5]);

            String result = new HttpHandler().makeServiceCall(params[0], "POST", impl, "");
            Log.i(TAG, "Response from url: " + result);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(getActivity(), "Error, no se ha podido conectar, intentelo de nuevo más tarde", Toast.LENGTH_LONG).show();
            } else if (s.equals("409")) {
                Toast.makeText(getActivity(), "Email o nombre de usuario repetido", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getActivity(),"Usuario creado",Toast.LENGTH_LONG).show();
                EditText name = (EditText) getView().findViewById(R.id.editUsername);
                EditText password = (EditText) getView().findViewById(R.id.editContraseña);
                new PostLogin().execute("http://10.4.41.145/api/session", "POST", name.getText().toString(), password.getText().toString());
            }
        }
    }

    private class PostLogin extends AsyncTask<String, Void, String> {

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
            bodyParams.put("user", params[2]);
            bodyParams.put("password", params[3]);
            String response = httpHandler.makeServiceCall(params[0], params[1], bodyParams, "");
            if (response != null) {
                try {
                    JSONObject aux = new JSONObject(response);
                    session = SessionManager.getInstance();
                    session.putInfoLoginSession(params[2], aux.get("role").toString(),
                            aux.get("token").toString(), aux.getInt("points"));
                    UserData.createUser(params[2], aux.getString("token"), aux.getInt("points"),
                            aux.getString("role"), 0, getActivity().getApplicationContext());
                    Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                } catch (JSONException | NullParametersException e) {
                    e.printStackTrace();
                }
            } else {
                return "Falla";

            }
            return "Correcte";
        }

        /**
         * Called when doInBackground is finished, Toast an error if there is an error.
         *
         * @param result If is "Falla" makes the toast.
         */
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Falla")) {
                Toast.makeText(getActivity(), "Nombre o password incorrecto", Toast.LENGTH_LONG).show();
            }
        }
    }


}