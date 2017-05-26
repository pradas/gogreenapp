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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Exceptions.NullParametersException;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.UserData;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    private final String EXTRA_BOOLEAN = "ADD_ACCOUNT";
    private SessionManager session;
    private EditText textName;
    private EditText textPassword;
    private Button buttonRegister;

    /**
     * Required empty public constructor
     */
    public LoginFragment() {

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
        return inflater.inflate(R.layout.login_fragment, container, false);
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

        // boolean to see if this fragment is called to Add an Account for the Switch
        boolean calledForAddAccount = getActivity().getIntent().getBooleanExtra(EXTRA_BOOLEAN, false);
        Button buttonLogin = (Button) getView().findViewById(R.id.buttonLogin);
        textName = (EditText) getView().findViewById(R.id.username_edit_text);
        textPassword = (EditText) getView().findViewById(R.id.password_user_text);
        buttonRegister = (Button) getView().findViewById(R.id.buttonRegister);

        // Set the text to Añadir Cuenta if calledFromAddAccount is true and hide Register Button
        if (calledForAddAccount) {
            buttonLogin.setText(R.string.add_account);
            buttonRegister.setVisibility(View.INVISIBLE);
        }

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
                new PostLogin().execute("http://10.4.41.145/api/session", "POST", textName.getText().toString(),
                        textPassword.getText().toString());
            }
            // Staring MainActivity
        });

        // If it is called for Add Account register isn't necessary
        if (!calledForAddAccount) {
            buttonRegister.setOnClickListener(v -> {
                try {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_login, RegisterFragment.class.newInstance()).commit();
                } catch (java.lang.InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                getActivity().setTitle("Register");
            });
        }

    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class PostLogin extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url, params[1] is the method petition, params[2] is the username or
         *               email for identification in the login and params[3] is the password to identification in the
         *               login
         *
         * @return "Falla" si no es un login correcte o "Correcte" si ha funcionat
         */
        @Override
        protected String doInBackground(String... params) {

            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("user", params[2]);
            bodyParams.put("password", params[3]);
            String response = httpHandler.makeServiceCall(params[0], params[1], bodyParams, "");
            if (response.equals("401")){
                return "Falla";
            }
            else{
                try {
                    JSONObject aux = new JSONObject(response);

                    // put the info of the User logged into the Session Manager
                    session = SessionManager.getInstance(getActivity().getApplicationContext());
                    session.putInfoLoginSession(params[2], aux.getString("role"), aux.getString("token"),
                            aux.getInt("points"));
                    /*if ("manager".equals(aux.getString("role"))){
                        session.setShopId(aux.getInt("shopId"));
                    }*/

                    // insert the User info into the SQLite
                    try {
                        UserData.createUser(params[2], aux.getString("token"), aux.getInt("points"),
                                aux.getString("role"), getActivity().getApplicationContext());
                    } catch (NullParametersException e) {
                        System.out.println(e.getMessage());
                    }

                    Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "FallaServer";
                }
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
            else if (result.equalsIgnoreCase("FallaServer")) {
                Toast.makeText(getActivity(), "No esta disponible el servidor", Toast.LENGTH_LONG).show();
            }
        }
    }
}
