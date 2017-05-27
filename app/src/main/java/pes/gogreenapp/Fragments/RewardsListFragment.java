package pes.gogreenapp.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Adapters.RewardsListAdapter;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

public class RewardsListFragment extends Fragment {

    public static String ARG_REWARDS_LIST_NUMBER = "rewards_list_number";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RewardsListAdapter adapter;
    String categorySelected = "";
    String url = "http://10.4.41.145/api/";
    String urlStorage = "http://10.4.41.145/storage/";
    private SwipeRefreshLayout swipeContainer;
    private String TAG = MainActivity.class.getSimpleName();
    private List<Reward> rewards = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private SessionManager session;
    private String userName;
    private TextView warning;

    /**
     * Required empty public constructor
     */
    public RewardsListFragment() {

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

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.rewards_list_fragment, container, false);
    }

    public void showFilterDialog() {
        //Creamos una instancia del FilterDialog y la mostramos
        RewardsFilterDialogFragment dialog = new RewardsFilterDialogFragment();
        dialog.show(getFragmentManager(), "RewardsFilterDialogFragment");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_filters, menu);
        final MenuItem filterButton = menu.findItem(R.id.filter_icon);

        //Listener for the filter menuIcon
        filterButton.setOnMenuItemClickListener(v -> {
            showFilterDialog();
            return true;
        });


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
        recyclerView = (RecyclerView) getView().findViewById(R.id.rv);
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RewardsListAdapter(getContext(), rewards);
        recyclerView.setAdapter(adapter);
        new GetRewards().execute(url + "rewards");

        // Refresh items
        swipeContainer.setOnRefreshListener(this::refreshItems);
    }

    /**
     * On swipe, refresh all the items of the screen.
     */
    void refreshItems() {
        // Load complete
        // Update the adapter and notify data set changed
        adapter.setRewards(rewards);
        adapter.notifyDataSetChanged();

        // Stop refresh animation
        swipeContainer.setRefreshing(false);
    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class GetRewards extends AsyncTask<String, Void, Void> {

        private Bitmap getRemoteImage(final URL aURL) {

            try {
                final URLConnection conn = aURL.openConnection();
                conn.connect();
                final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                final Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            } catch (IOException e) {
            }
            return null;
        }

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param urls The parameters of the task.
         */
        @Override
        protected Void doInBackground(String... urls) {

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(), session.getToken());
            Log.i(TAG, "Response from url: " + response);
            URL imageUrl = null;
            if (response != null) {
                try {
                    JSONObject aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("rewards");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Date d = df.parse((String) jsonObject.get("end_date"));
                        Bitmap b_image_user = null;
                        String imageStringLink = null;
                        imageStringLink = jsonObject.getString("image");
                        if (imageStringLink != "null") {
                            imageUrl = new URL(urlStorage + imageStringLink);
                            b_image_user = this.getRemoteImage(imageUrl);
                        }
                        rewards.add(new Reward((Integer) jsonObject.get("id"), (String) jsonObject.get("title"),
                                (Integer) jsonObject.get("points"), d, (String) jsonObject.get("category"),
                                (Boolean) jsonObject.get("favourite"), b_image_user));
                        adapter.setRewards(rewards);
                        publishProgress();
                    }
                } catch (JSONException | ParseException | MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            adapter.notifyDataSetChanged();
        }
    }
}
