package pes.gogreenapp.Fragments;

import android.content.pm.PackageInstaller;
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

/**
 * Created by Adrian on 23/05/2017.
 */

public class TabRewardsFragment extends Fragment {

    public static String ARG_REWARDS_LIST_NUMBER = "rewards_list_number";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RewardsListAdapter adapter;
    String url = "http://10.4.41.145/api/";
    String urlStorage = "http://10.4.41.145/storage/";
    private SwipeRefreshLayout swipeContainer;
    private String TAG = MainActivity.class.getSimpleName();
    private List<Reward> rewards = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private SessionManager session;
    private String userName;
    private TextView warning;

    public TabRewardsFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_rewards_fragment, container, false);
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
        session = SessionManager.getInstance();
        recyclerView = (RecyclerView) getView().findViewById(R.id.rvRewardsFavList);
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainerRewardsFavList);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        new GetCategories().execute(url + "categories");
        new GetRewards().execute(url + "users/" + session.getUsername() + "/favourite-rewards");
        warning = (TextView) getView().findViewById(R.id.warningNoResultRewardsFavList);

        swipeContainer.setOnRefreshListener(this::refreshItems);
    }

    /**
     * On swipe, refresh all the items of the screen.
     */
    void refreshItems() {
        // Load items
        rewards.clear();
        warning.setText("");
        // Get items
        new GetRewards().execute(url + "/users/" + session.getUsername() + "/favourite-rewards");

        // Load complete
        onItemsLoadComplete();
    }

    /**
     * When the refresh is complete, set again the Adapter.
     */
    void onItemsLoadComplete() {
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
            } catch (IOException e) {}
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
            String response = httpHandler.makeServiceCall(urls[0], "GET", new HashMap<>(),
                    session.getToken());
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
                        if(imageStringLink != "null"){
                            imageUrl = new URL(urlStorage + imageStringLink);
                            b_image_user = this.getRemoteImage(imageUrl);
                        }
                        rewards.add(new Reward((Integer) jsonObject.get("id"),
                                (String) jsonObject.get("title"), (Integer) jsonObject.get("points"),
                                d, (String) jsonObject.get("category"), (Boolean) jsonObject.get("favourite"), b_image_user));
                    }
                } catch (JSONException | ParseException | MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /**
         * Creates the new Adapter and set the actual rewards by the result obtained.
         *
         * @param result of doInBackground()
         */
        @Override
        protected void onPostExecute(Void result) {
            adapter = new RewardsListAdapter(getContext(), rewards);
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Asynchronous Task for the petition GET of all the Categories.
     */
    private class GetCategories extends AsyncTask<String, Void, Void> {

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
                JSONObject aux;
                try {
                    aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        categories.add((String) jsonObject.get("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
