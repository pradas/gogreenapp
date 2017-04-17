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

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.SessionManager;
import pes.gogreenapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    private SessionManager session;
    private EditText textName;
    private EditText textPassword;
    private Button buttonCancel;

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

        // Session Manager
        session = new SessionManager(getActivity().getApplicationContext());

        Button buttonLogin = (Button) getView().findViewById(R.id.buttonLogin);
        textName = (EditText) getView().findViewById(R.id.textNombre);
        textPassword = (EditText) getView().findViewById(R.id.textContraseña);

        buttonCancel = (Button) getView().findViewById(R.id.buttonCancel);

        buttonLogin.setOnClickListener(v -> {
            //Falta llamar a API y coger token si es correcto,
            session.createLoginSession(textName.getText().toString(), "asdfALdkj1q20e3ijdF");
            // Staring MainActivity
            Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        });

        buttonCancel.setOnClickListener(v -> {
            getActivity().finish();
        });
    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class PostLogin extends AsyncTask<String, Void, Void> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params The parameters of the task.
         */
        @Override
        protected Void doInBackground(String... params) {
            HttpHandler httpHandler= new HttpHandler();
            return null;
        }

        /**
         * Creates the new Adapter and set the actual rewards by the result obtained.
         *
         * @param result of doInBackground()
         */
        @Override
        protected void onPostExecute(Void result) {

        }
    }
}
