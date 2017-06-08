package pes.gogreenapp.Fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Adrian on 07/06/2017.
 */

public class ForgottenPasswordFragment extends Fragment {

    private SessionManager session;
    private EditText email;
    private Button sendPassword;
    String mailAddressSender;
    String password;
    Session sessionMail;
    Boolean exists;
    String textEmail;
    Integer newPassword;
    private Activity activity;

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
        exists = false;
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
        activity = getActivity();

        email = (EditText) getView().findViewById(R.id.email_edit_text_forgot_password);
        ImageView imgback = (ImageView) getView().findViewById(R.id.imageButtonBackOferta);
        sendPassword = (Button) getView().findViewById(R.id.reSendPassword);
        mailAddressSender = "gogreenfib@gmail.com";
        password = "Password12FIB";
        Random r = new Random();
        newPassword = r.nextInt(100000 - 1) + 65;
        textEmail = "Hola. Ha llegado una petición de cambiar contraseña desde la APP de GoGreen. " +
                "Tu nueva contraseña es " + newPassword + ". Introducela para poder entrar y cambiala lo antes " +
                "posible en el menú de Settings de la app.";

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_login, LoginFragment.class.newInstance()).commit();
                } catch (java.lang.InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        });

        sendPassword.setOnClickListener(new View.OnClickListener() {
            Boolean send = true;
            @Override
            public void onClick(View v) {
                if (email.getText().toString().length() <= 0) {
                    email.setError("Correo necesario");
                    send = false;
                }

                if (send) {

                    new PutUser().execute("http://10.4.41.145/api/reset-password",
                            "PUT", (String) String.valueOf(newPassword), email.getText().toString());

                    if (exists) {

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
                                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getText().toString()));
                                message.setContent(textEmail, "text/html; charset=utf-8");
                                Transport.send(message);
                                Toast.makeText(activity, "Email enviado", Toast.LENGTH_SHORT).show();
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

    /**
     * Asynchronous Task for the petition PUT of a User.
     */
    private class PutUser extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the method petition,
         *               params[2] is the new password for the user
         *               params[3] is the email of the user to modify the password
         * @return "Error" if the method fails, "Correct" if the method works, other if the user doesn't exixts
         */
        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("email", params[3]);
            bodyParams.put("password", params[2]);
            String response = httpHandler.makeServiceCall(params[0], params[1], bodyParams, "");
            if (response != null && !response.equals("500") && !response.equals("404")) {
                exists = true;
                return "Correct";
            }
            else if (response.equals("404")) {
                exists = false;
                return "El usuario " + params[3] + " no existe";
            }
            else {
                exists = false;
                return "Error";
            }
        }

        /**
         * Called when doInBackground is finished.
         *
         * @param result Makes a toast with the result
         */
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(activity, "Error al enviar el email",
                        Toast.LENGTH_SHORT).show();
            }
            else if ((!result.equalsIgnoreCase("Correct")) && (!result.equalsIgnoreCase("Error"))){
                Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
