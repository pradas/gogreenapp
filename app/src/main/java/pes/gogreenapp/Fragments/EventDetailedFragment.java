package pes.gogreenapp.Fragments;


import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import pes.gogreenapp.Objects.Event;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailedFragment extends Fragment {

    //initialitions
    private SessionManager session;
    private ImageView image;
    private TextView title;
    private TextView description;
    private TextView direction;
    private TextView date;
    private TextView time;
    private TextView instrucciones;
    private ImageButton fav;

    private Integer id;
    static private String TAG = "EventDetailed";
    private Event event;
    private String url = "http://10.4.41.145/api/events/";
    static private final String URLcategories = "http://10.4.41.145/api/categories";

    /**
     * Required empty public constructor
     */
    public EventDetailedFragment() {

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
        View view = inflater.inflate(R.layout.event_detailed_fragment, container, false);
        id = getArguments().getInt("id");
        url += id;
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        return view;
    }


    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        session = SessionManager.getInstance();
        image = (ImageView) getView().findViewById(R.id.imageEventDetailed);
        title = (TextView) getView().findViewById(R.id.titleEventDetailed);
        description = (TextView) getView().findViewById(R.id.descriptionEventDetailed);
        date = (TextView) getView().findViewById(R.id.dateEventDetailed);
        time = (TextView) getView().findViewById(R.id.hourEventDetailed);
        direction = (TextView) getView().findViewById(R.id.directionEventDetailed);
        instrucciones = (TextView) getView().findViewById(R.id.instructionsDetailReward);
        ImageView imgback = (ImageView) getView().findViewById(R.id.imageButtonBackReward);
        fav = (ImageButton) getView().findViewById(R.id.eventFavoriteDetailButton);
        if (!("user".equals(session.getRole()))) fav.setVisibility(View.GONE);
        imgback.setOnClickListener(v -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment fragment;
            if (session.getRole().equals("manager")) {
                fragment = (Fragment) new EventsListShopFragment();
            } else {
                fragment = (Fragment) new EventsListFragment();
            }
            transaction.replace(R.id.flContent, fragment);
            transaction.commit();

        });

        try {
            new GetEvent().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //elements
    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class GetEvent extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param urls The parameters of the task.
         */
        @Override
        protected String doInBackground(String... urls) {

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(), session.getToken());
            Log.i(TAG, "Response from url: " + response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("AAAA", jsonObject.toString());
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String address = null;
                    if (!jsonObject.isNull("adress")) address = jsonObject.getString("adress");
                    String company = null;
                    if (!jsonObject.isNull("shop")) company = jsonObject.getString("shop");
                    String image = null;
                    if (!jsonObject.isNull("image")) image = jsonObject.getString("image");

                    event = new Event(jsonObject.getInt("id"), jsonObject.getString("title"),
                            jsonObject.getString("description"), jsonObject.getInt("points"), address, company,
                            df.parse(jsonObject.getString("date")), image, jsonObject.getString("category"),
                            jsonObject.getBoolean("favourite"), jsonObject.getInt("shop_id"));

                    Log.d(TAG, "event created");
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            return "correct";
        }

        protected void onPostExecute(String result) {
            //initialize
            title.setText(event.getTitle() + " (" + event.getPoints().toString() + " pts)");
            description.setText(event.getDescription());
            direction.setText(event.getDirection());
            instrucciones.setText("Evento orgnanizado por " + event.getCompany() + "\nEl genero del evento es: " +
                    event.getCategory());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            date.setText("Fecha del evento: " + sdf.format(event.getDate()));

            if (event.isFavorite()) {
                fav.setTag("favoritefilled");
                fav.setImageResource(R.drawable.ic_fav_filled);
            } else {
                fav.setImageResource(R.drawable.ic_fav_void);
                fav.setTag("favorite");
            }

            fav.setOnClickListener(v -> {
                if (fav.getTag().equals("favorite")) {
                    new PostFavorite().execute("http://10.4.41.145/api/users/", "POST", session.getUsername(),
                            event.getId().toString());
                    fav.setImageResource(R.drawable.ic_fav_filled);
                    fav.setTag("favoritefilled");
                } else {
                    new DeleteFavorite().execute("http://10.4.41.145/api/users/", "DELETE", session.getUsername(),
                            event.getId().toString());
                    fav.setImageResource(R.drawable.ic_fav_void);
                    fav.setTag("favorite");
                }
            });


            String hour = event.getHour();
            if (Integer.parseInt(event.getHour()) < 10) {
                hour = "0" + event.getHour();
            }
            String min = event.getMin();
            if (Integer.parseInt(event.getMin()) < 10) {
                min = "0" + event.getMin();
            }
            fav.setOnClickListener(v -> {
                if (fav.getTag().equals("favorite")) {
                    new PostFavorite().execute("http://10.4.41.145/api/users/", "POST", session.getUsername(),
                            event.getId().toString());
                    fav.setImageResource(R.drawable.ic_fav_filled);
                    fav.setTag("favoritefilled");
                } else {
                    new DeleteFavorite().execute("http://10.4.41.145/api/users/", "DELETE", session.getUsername(),
                            event.getId().toString());
                    fav.setImageResource(R.drawable.ic_fav_void);
                    fav.setTag("favorite");
                }
            });

            time.setText(hour + ":" + min);
            if (event.getImage() != null) {
                byte[] decodedBytes = event.getImage();
                image.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
            }
        }
    }

    private class PostFavorite extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("event_id", params[3]);
            String url = params[0] + params[2] + "/favourite-events";
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null) return "Correct";
            return "Error";
        }

        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(getActivity(), "Error al añadir el evento a favoritos. Intentalo de nuevo mas tarde",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Evento añadido a favoritos con exito.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DeleteFavorite extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            String url = params[0] + params[2] + "/favourite-events/" + params[3];
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null) return "Correct";
            return "Error";
        }

        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(getActivity(), "Error al eliminar el evento de favoritos. Intentalo de nuevo mas tarde",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Evento eliminado de favoritos con exito.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}