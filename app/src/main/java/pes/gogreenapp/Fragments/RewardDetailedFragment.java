package pes.gogreenapp.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import pes.gogreenapp.Objects.RewardDetailed;
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
    private RewardDetailed reward;


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
        View view =  inflater.inflate(R.layout.reward_detailed_fragment, container, false);
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


        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getActivity().getApplicationContext());
        try {
            new GetReward().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        title = (TextView) getView().findViewById(R.id.titleDetailReward);
        title.setText(reward.getTitle());
        description = (TextView) getView().findViewById(R.id.descriptionDetailReward);
        description.setText(reward.getDescription());
        endDate = (TextView) getView().findViewById(R.id.dateValidDetailReward);
        Date finalDate = reward.getEndDate();
        endDate.setText("Promoción valida hasta el " + new SimpleDateFormat("dd/MM/yyyy").format(finalDate));
        web = (TextView) getView().findViewById(R.id.consultWebDetailReward);
        web.setText("Consulta mas información en " + reward.getContactWeb());
        advert = (TextView) getView().findViewById(R.id.advertDetailReward);
        advert.setText("Promoción válida hasta el " + reward.getEndDate() + " y no acumulable a otras ofertas," +
                "cupones o promociones. Se prohíbe la venda de este vale. Solo se aceptará un vale por día y " +
                "titular.");
        instructions = (TextView) getView().findViewById(R.id.instructionsDetailReward);
        instructions.setText("Para poder utilizar este vale es necesario hacer click en canjear y " +
                "que el propietario o empleado de la tienda escanee el código.");
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
                    reward =  new RewardDetailed((Integer) jsonObject.get("id"),
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
