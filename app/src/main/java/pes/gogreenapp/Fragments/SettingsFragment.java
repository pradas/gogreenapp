package pes.gogreenapp.Fragments;


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

import java.util.HashMap;

import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Albert on 13/04/2017.
 */
public class SettingsFragment extends Fragment {
    private SessionManager session;
    private EditText newPass;
    private EditText confirmNewPass;
    private Button changePass;


    /**
     * Required empty public constructor
     */
    public SettingsFragment() {
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
        return inflater.inflate(R.layout.settings_fragment, container, false);
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

        newPass = (EditText) getView().findViewById(R.id.newPass);
        confirmNewPass = (EditText) getView().findViewById(R.id.confirmNewPass);
        changePass = (Button) getView().findViewById(R.id.changePass);

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean send = true;

                if (newPass.getText().toString().length() <= 0) {
                    newPass.setError("La nueva contraseña es necesaria");
                    send = false;
                }
                if (confirmNewPass.getText().toString().length() <= 0) {
                    confirmNewPass.setError("Es necesario confirmar la nueva contraseña");
                    send = false;
                }
                if ((newPass.getText().toString().length() > 0 || confirmNewPass.getText().toString().length() > 0)
                        && (!newPass.getText().toString().equals(confirmNewPass.getText().toString()))) {
                    newPass.setError("Las contraseñas no coinciden");
                    confirmNewPass.setError("Las contraseñas no coinciden");
                    send = false;
                }

                if (send) {
                    new PutUser().execute("http://10.4.41.145/api/users/", "PUT", newPass.getText().toString(),
                            session.getUsername());
                }
            }
        });

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
         *               params[2] is the new password to set
         *               params[3] is the username of the user to edit the password
         * @return A string with the result
         */
        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("password", params[2]);
            String url = params [0] + params[3];
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null && !response.equals("500") && !response.equals("404")) {
                return "Correct";
            }
            else if (response.equals("404")) return "El usuario " + params[3] + " no existe";
            return "Error";
        }

        /**
         * Called when doInBackground is finished.
         *
         * @param result makes a toast with the result
         */
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(getActivity(), "Error al editar la contraseña",
                        Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("Correct")){
                Toast.makeText(getActivity(), "Contraseña cambiada", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            }
        }
    }

}
