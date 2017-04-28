package pes.gogreenapp.Fragments;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Handlers.HttpHandler;

import pes.gogreenapp.Objects.GlobalPreferences;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.Objects.SessionManager;
import pes.gogreenapp.R;

/**
 * Created by Adrian on 17/04/2017.
 */

public class RewardDetailedFragment extends Fragment {

    private SessionManager session;
    private Integer id;
    private String TAG = MainActivity.class.getSimpleName();
    private String url = "http://10.4.41.145/api/rewards/";
    private Reward reward;


    /**
     * Required empty public constructor
     */
    public RewardDetailedFragment() {
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
        View view = inflater.inflate(R.layout.reward_detailed_fragment, container, false);
        id = getArguments().getInt("id");
        url += id;
        return view;
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
        TextView title;
        TextView description;
        TextView endDate;
        TextView web;
        TextView advert;
        TextView instructions;
        ImageButton fav;
        Button action;


        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getActivity().getApplicationContext(),
                new GlobalPreferences(getActivity().getApplicationContext()).getUser());
        try {
            new GetReward().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        title = (TextView) getView().findViewById(R.id.titleDetailReward);
        description = (TextView) getView().findViewById(R.id.descriptionDetailReward);
        endDate = (TextView) getView().findViewById(R.id.dateValidDetailReward);
        web = (TextView) getView().findViewById(R.id.consultWebDetailReward);
        advert = (TextView) getView().findViewById(R.id.advertDetailReward);
        instructions = (TextView) getView().findViewById(R.id.instructionsDetailReward);
        fav = (ImageButton) getView().findViewById(R.id.favoriteDetailButton);
        action = (Button) getView().findViewById(R.id.actionDetailReward);

        if (getArguments().getString("parent").equals("list")) action.setText("CANJEAR");
        else action.setText("UTILIZAR");
        title.setText(reward.getTitle());
        description.setText(reward.getDescription());
        Date finalDate = reward.getEndDate();
        endDate.setText("Promoción valida hasta el " + new SimpleDateFormat("dd/MM/yyyy").format(finalDate));
        web.setText("Consulta mas información en " + reward.getContactWeb());
        advert.setText("Promoción válida hasta el " + reward.getEndDate() + " y no acumulable a otras ofertas," +
                "cupones o promociones. Se prohíbe la venda de este vale. Solo se aceptará un vale por día y " +
                "titular.");
        instructions.setText("Para poder utilizar este vale es necesario hacer click en canjear y " +
                "que el propietario o empleado de la tienda escanee el código.");

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav.getTag().equals("favorite")) {
                    fav.setImageResource(R.mipmap.favoritefilled);
                    fav.setTag("favoritefilled");
                } else {
                    fav.setImageResource(R.mipmap.favorite);
                    fav.setTag("favorite");
                }
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments().getString("parent").equals("list")) { //ACCIÓN CANJEAR
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getRootView().getContext());
                    mBuilder.setMessage("¿Está seguro de que desea canjear esta promoción?");
                    mBuilder.setPositiveButton(R.string.exchange, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (session.getPoints() < (Integer) reward.getPoints()) {
                                Toast.makeText(getActivity(), "No tienes suficientes puntos para canjear este reward",
                                        Toast.LENGTH_LONG).show();
                            }
                            else {
                                new PostReward().execute("http://10.4.41.145/api/users/", "POST",
                                        session.getUserName(), (String) String.valueOf(reward.getId()));
                                Integer points = session.getPoints();
                                points -= (Integer) reward.getPoints();
                                //no se como se hace el set
                                /*FragmentManager manager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                Fragment fragment = (Fragment) new RewardsListFragment();
                                transaction.replace(R.id.flContent, fragment);
                                transaction.commit();*/
                            }
                        }
                    });
                    mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                } else { //ACCIÓN UTILIZAR
                    String url = "http://10.4.41.145/api/users/" + session.getUserName() + "/rewards/" + reward.getId();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);

                    FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Fragment fragment = (Fragment) new QRCodeFragment();
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.flContent, fragment);
                    transaction.commit();

                }
            }
        });
    }

    private class PostReward extends AsyncTask<String, Void, String> {

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
            bodyParams.put("reward_id", params[3]);
            String url = params[0] + params [2] + "/rewards";
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null) return "Correct";
            return "Error";
        }

        /**
         * Called when doInBackground is finished, Toast an error if there is an error.
         *
         * @param result If is "Falla" makes the toast.
         */
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(getActivity(), "Error al canjear el Reward. Intentalo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(getActivity(), "Reward canjeado con exito.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class GetReward extends AsyncTask<String, Void, Void> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param urls The parameters of the task.
         */
        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(),
                    session.getToken());
            Log.i(TAG, "Response from url: " + response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Date endDate = df.parse((String) jsonObject.get("end_date"));
                    reward = new Reward((Integer) jsonObject.get("id"),
                            (String) jsonObject.get("title"), (Integer) jsonObject.get("points"), endDate,
                            (String) jsonObject.get("description"), (String) jsonObject.get("exchange_info"),
                            (String) jsonObject.get("contact_web"), (String) jsonObject.get("contact_info"),
                            (Double) jsonObject.get("exchange_latitude"), (Double) jsonObject.get("exchange_longitude"));
                    String s = reward.getTitle();
                    Integer i = reward.getId();
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
