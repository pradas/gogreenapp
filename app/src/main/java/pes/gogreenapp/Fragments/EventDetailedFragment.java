package pes.gogreenapp.Fragments;


import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView points;
    private TextView category;
    private TextView direction;
    private TextView company;
    private TextView date;
    private TextView time;

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
        View view = inflater.inflate(R.layout.event_detailed_fragment, container, false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = SessionManager.getInstance();
        image = (ImageView) getView().findViewById(R.id.imageEventDetailed);
        title = (TextView) getView().findViewById(R.id.titleEventDetailed);
        description = (TextView) getView().findViewById(R.id.descriptionEventDetailed);
        points = (TextView) getView().findViewById(R.id.pointsEventDetailed);
        category = (TextView) getView().findViewById(R.id.categoryEventDetailed);
        company = (TextView) getView().findViewById(R.id.companyEventDetailed);
        date = (TextView) getView().findViewById(R.id.dateEventDetailed);
        time = (TextView) getView().findViewById(R.id.hourEventDetailed);
        direction = (TextView) getView().findViewById(R.id.directionEventDetailed);
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
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(),
                    session.getToken());
            Log.i(TAG, "Response from url: " + response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String address = null;
                    if (!jsonObject.isNull("adress")) address = jsonObject.getString("adress");
                    String company = null;
                    if (!jsonObject.isNull("company")) company = jsonObject.getString("company");
                    String image = null;
                    if (!jsonObject.isNull("image")) image = jsonObject.getString("image");
                    Boolean favorite = false;
                    if (jsonObject.get("favourite") == "true") favorite = true;
                    event = new Event(jsonObject.getInt("id"),
                            jsonObject.getString("title"),
                            jsonObject.getString("description"),
                            jsonObject.getInt("points"),
                            address,
                            company,
                            df.parse(jsonObject.getString("date")),
                            image,
                            jsonObject.getString("category"),
                            favorite);
                    Log.d(TAG, "event created");
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            return "correct";
        }
        protected void onPostExecute(String result){
            //initialize
            title.setText(event.getTitle());
            description.setText(event.getDescription());
            points.setText(event.getPoints().toString());
            category.setText(event.getCategory());
            direction.setText(event.getDirection());
            company.setText(event.getCompany());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            date.setText(sdf.format(event.getDate()));
            String hour = event.getHour();
            if (Integer.parseInt(event.getHour()) < 10){
                hour = "0" + event.getHour();
            }
            String min = event.getMin();
            if (Integer.parseInt(event.getMin()) < 10){
                min = "0" + event.getMin();
            }
            time.setText(hour+":"+min);
            if (event.getImage() != null) {
                byte[] decodedBytes = event.getImage();
                image.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
            }
        }
    }
}
