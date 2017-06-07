package pes.gogreenapp.Fragments;



import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import pes.gogreenapp.Objects.Oferta;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

public class OfertaDetailedFragment extends Fragment {
    //initialitions
    private SessionManager session;
    private Integer id;
    private String TAG = "OfertaDetailedFragment";
    private String url = "http://10.4.41.145/api/deals/";
    private Oferta oferta;
    private Bitmap bmp;

    /**
     * Required empty public constructor
     */
    public OfertaDetailedFragment() {
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
        View view = inflater.inflate(R.layout.oferta_detailed_fragment, container, false);
        id = getArguments().getInt("id");
        url += id;
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        byte[] b = getArguments().getByteArray("image");
        //Crear imagen
        bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        getActivity().setTitle("Oferta");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
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
        TextView instructions;
        ImageButton fav;
        Button shopBut;

        super.onActivityCreated(savedInstanceState);
        session = SessionManager.getInstance();
        try {
            new GetOferta().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        title = (TextView) getView().findViewById(R.id.titleDetailOferta);
        description = (TextView) getView().findViewById(R.id.descriptionDetailOferta);
        endDate = (TextView) getView().findViewById(R.id.dateValidDetailOferta);
        instructions = (TextView) getView().findViewById(R.id.instructionsDetailOferta);
        fav = (ImageButton) getView().findViewById(R.id.favoriteDetailButtonOferta);
        shopBut = (Button) getView().findViewById(R.id.buttonGoToShop);
        ImageView img = (ImageView) getView().findViewById(R.id.ofertaBackgroundImageProfile);
        img.setImageBitmap(bmp);
        ImageView imgback = (ImageView) getView().findViewById(R.id.imageButtonBackOferta);
        title.setText(oferta.getTitle() + " (" + oferta.getPoints() + " %)");
        description.setText(oferta.getDescription());
        Date finalDate = oferta.getDate();

        endDate.setText("Fecha limite: " + new SimpleDateFormat("dd/MM/yyyy").format(finalDate));
        instructions.setText("Oferta hecha por el comercio " + oferta.getShop_name() + ". Para usarla, ir al comercio y preguntar.");

        fav.setTag("favorite");
        fav.setImageResource(R.drawable.ic_fav_void);

        if (oferta.isFavorite()) {
            fav.setTag("favoritefilled");
            fav.setImageResource(R.drawable.ic_fav_filled);
        }
        else {
            fav.setImageResource(R.drawable.ic_fav_void);
            fav.setTag("favorite");
        }

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav.getTag().equals("favorite")) {
                    new PostFavorite().execute("http://10.4.41.145/api/users/", "POST",
                            session.getUsername(), oferta.getId().toString());
                    fav.setImageResource(R.drawable.ic_fav_filled);
                    fav.setTag("favoritefilled");
                } else {
                    new DeleteFavorite().execute("http://10.4.41.145/api/users/", "DELETE",
                            session.getUsername(), oferta.getId().toString());
                    fav.setImageResource(R.drawable.ic_fav_void);
                    fav.setTag("favorite");
                }
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment;
                fragment = (Fragment) new OfertasListFragment();
                transaction.replace(R.id.flContent, fragment).addToBackStack( "tag" ).commit();

            }
        });
        shopBut.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putInt("id", oferta.getShop());
            FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment fragment = (Fragment) new ShopProfileContainerFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.flContent, fragment).addToBackStack( "tag" ).commit();
        });
    }

    /**
     * Asynchronous Task for the petition POST of a deal.
     */
    private class PostFavorite extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("deal_id", params[3]);
            String url = params[0] + params[2] + "/favourite-deals";
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null) return "Correct";
            return "Error";
        }

        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(getActivity(), "Error al añadir la Oferta a favoritos. Intentalo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getActivity(), "Oferta añadida a favoritos con exito.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asynchronous Task for the petition DELETE the favorite.
     */
    private class DeleteFavorite extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            String url = params[0] + params[2] + "/favourite-deals/" + params[3];
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null) return "Correct";
            return "Error";
        }

        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(getActivity(), "Error al eliminar el Reward de favoritos. Intentalo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getActivity(), "Reward eliminado de favoritos con exito.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asynchronous Task for the petition GET of the deal.
     */
    private class GetOferta extends AsyncTask<String, Void, Void> {

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
                    Date date = null;
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    if (!jsonObject.isNull("date")) date = df.parse(jsonObject.getString("date"));
                    oferta = new Oferta(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.getInt("value"),
                            date, jsonObject.getBoolean("favourite"),
                            jsonObject.getString("image"));
                    oferta.setShop(jsonObject.getInt("shop_id"));
                    oferta.setShop_name(jsonObject.getString("shop"));
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
