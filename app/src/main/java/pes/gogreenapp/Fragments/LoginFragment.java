package pes.gogreenapp.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.GlobalPreferences;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.Objects.SessionManager;
import pes.gogreenapp.R;

import static pes.gogreenapp.R.id.buttonLogin;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    private SessionManager session;
    private EditText textName;
    private EditText textPassword;

    /**
     * Required empty public constructor
     */
    public LoginFragment() {
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
        return inflater.inflate(R.layout.login_fragment, container, false);
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

        Button buttonLogin = (Button) getView().findViewById(R.id.buttonLogin);
        textName = (EditText) getView().findViewById(R.id.username_edit_text);
        textPassword = (EditText) getView().findViewById(R.id.password_user_text);

        buttonLogin.setOnClickListener(v -> {
            Boolean send = true;
            if (textName.getText().toString().length() <= 0) {
                textName.setError("Nombre necesario");
                send = false;
            }
            if (textPassword.getText().toString().length() <= 0) {
                textPassword.setError("Contraseña necesaria");
                send = false;
            }
            if (send) {
                new PostLogin().execute("http://10.4.41.145/api/session", "POST",
                        textName.getText().toString(), textPassword.getText().toString());
            }
            // Staring MainActivity
        });

    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
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
                    new GlobalPreferences(getActivity().getApplicationContext()).setUser(params[2]);
                    session = new SessionManager(getActivity().getApplicationContext(), params[2]);
                    session.createLoginSession(params[2], aux.get("token").toString());
                    Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                } catch (JSONException e) {
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
