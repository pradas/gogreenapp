package pes.gogreenapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

import static android.content.ContentValues.TAG;

/**
 * Created by Adrian on 07/06/2017.
 */

public class ForgottenPasswordFragment extends Fragment {

    private SessionManager session;
    private EditText identifier;
    private Button sendPassword;
    String mailAddressSender;
    String mailAddressToSend;
    String password;
    Session sessionMail;

    /**
     * Required empty public constructor
     */
    public ForgottenPasswordFragment () {

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
        return inflater.inflate(R.layout.forgotten_password_fragment, container, false);
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

        identifier = (EditText) getView().findViewById(R.id.username_edit_text_forgot_password);
        sendPassword = (Button) getView().findViewById(R.id.reSendPassword);
        mailAddressSender = "gogreenfib@gmail.com";
        password = "Password12FIB";

        sendPassword.setOnClickListener(new View.OnClickListener() {
            Boolean send = true;
            @Override
            public void onClick(View v) {
                if (identifier.getText().toString().length() <= 0) {
                    identifier.setError("Nombre necesario");
                    send = false;
                }

                if (send) {
                    new GetEmail().execute("http://10.4.41.145/api/users/" + identifier.getText().toString());
                    if (!mailAddressToSend.equals(null)) {

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        Properties properties = new Properties();
                        properties.put("mail.smtp.host", "smtp.googlemail.com");
                        properties.put("mail.smtp.socketFactory.port", "465");
                        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                        properties.put("mail.smtp.auth", "true");
                        properties.put("mail.smtp.port", "465");

                        try {
                            sessionMail = Session.getDefaultInstance(properties, new Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(mailAddressSender, password);
                                }
                            });

                            if (sessionMail != null) {
                                Message message = new MimeMessage(sessionMail);
                                message.setFrom(new InternetAddress(mailAddressSender));
                                message.setSubject("[GOGREEN] Recupera tu contraseña");
                                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailAddressToSend));
                                message.setContent("ESTE ES EL MENSAJE", "text/html; charset=utf-8");
                                Transport.send(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_login, LoginFragment.class.newInstance()).commit();
                        } catch (java.lang.InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    private class GetEmail extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET" , new HashMap<>(),
                    session.getToken());
            Log.i(TAG, "Response from url: " + response);
            try {
                JSONObject jsonArray = new JSONObject(response);
                mailAddressToSend = jsonArray.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if (mailAddressToSend.equals(null)) {
                Toast.makeText(getActivity(), "Este usuario no existe",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
