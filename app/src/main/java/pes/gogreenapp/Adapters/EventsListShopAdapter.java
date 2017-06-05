package pes.gogreenapp.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pes.gogreenapp.Fragments.EditEventFragment;
import pes.gogreenapp.Fragments.EventDetailedFragment;
import pes.gogreenapp.Fragments.EventsListShopFragment;
import pes.gogreenapp.Fragments.RewardDetailedFragment;
import pes.gogreenapp.Objects.Event;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;


public class EventsListShopAdapter extends RecyclerView.Adapter<EventsListShopAdapter.ViewHolder> {
    private List<Event> events;
    private Context context;
    private SessionManager session;
    private String TAG = "EventsListAdapter";

    /**
     * Constructor that set the List of Rewards.
     *
     * @param events  non-null List of the Rewards.
     * @param context non-null context of the application.
     */
    public EventsListShopAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
        this.session = SessionManager.getInstance();
    }

    /**
     * Setter of the Rewards List.
     *
     * @param events non-null List of the Rewards.
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView points;
        TextView date;
        TextView hour;
        TextView category;
        ImageView DeleteButton;
        ImageButton fav;
        ImageButton edit;
        public Integer id;

        /**
         * Constructor of the View Holder that sets all the items.
         *
         * @param itemView valid View where to construct.
         */
        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.eventImageShop);
            title = (TextView) itemView.findViewById(R.id.eventTitleShop);
            points = (TextView) itemView.findViewById(R.id.eventPointsShop);
            category = (TextView) itemView.findViewById(R.id.eventCategoryShop);
            DeleteButton = (ImageButton) itemView.findViewById(R.id.deleteEditButtonShop);
            date = (TextView) itemView.findViewById(R.id.eventEndDateShop);
            hour = (TextView) itemView.findViewById(R.id.eventHourShop);
            fav = (ImageButton) itemView.findViewById(R.id.eventFavoriteButtonShop);
            edit = (ImageButton) itemView.findViewById(R.id.eventEditButtonShop);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    bundle.putString("parent", "list");

                    FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Fragment fragment = (Fragment) new EventDetailedFragment();
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.flContent, fragment);
                    transaction.commit();
                }
            });
            DeleteButton.setOnClickListener(v -> {
                Log.d(TAG, "Hola");
                String url = "http://10.4.41.145/api/events/" + String.valueOf(id);
                new DeleteEvent().execute(url, "DELETE",
                        session.getUsername());
            });
        }
    }


    /**
     * Obtains the LayoutInflater from the given context and usse it to create a new ViewHolder.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public EventsListShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_shop_cardview, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final EventsListShopAdapter.ViewHolder holder, int position) {
        holder.id = events.get(position).getId();
        holder.title.setText(events.get(position).getTitle());
        holder.category.setText(events.get(position).getCategory());
        holder.points.setText(String.valueOf(events.get(position).getPoints()));
        Date d = events.get(position).getDate();
        holder.date.setText(new SimpleDateFormat("dd-MM-yyyy").format(d));
        holder.hour.setText(new SimpleDateFormat("HH:mm").format(d));
        if (events.get(position).getImage() != null) {
            byte[] decodedBytes = events.get(position).getImage();
            holder.image.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
        }
        holder.fav.setOnClickListener(v -> {
            if (holder.fav.getTag().equals("favorite")) {
                new PostFavorite().execute("http://10.4.41.145/api/users/", "POST",
                        session.getUsername(), holder.id.toString());
                holder.fav.setImageResource(R.drawable.ic_fav_filled);
                holder.fav.setTag("favoritefilled");
            } else {
                holder.fav.setImageResource(R.drawable.ic_fav_void);
                holder.fav.setTag("favorite");
            }
        });
        holder.edit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", holder.id);
            bundle.putString("parent", "list");

            FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment fragment = (Fragment) new EditEventFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.flContent, fragment);
            transaction.commit();
        });
        holder.DeleteButton.setOnClickListener(v -> {
            String url = "http://10.4.41.145/api/shops/" + String.valueOf(events.get(position).getIdShop()) + "/events/" + String.valueOf(holder.id);
            Log.d(TAG, url);
            new DeleteEvent().execute(url, "DELETE",
                    session.getUsername());
        });

    }


    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class PostFavorite extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("event_id", params[3]);
            String url = params[0] + params[2] + "/favourite-events";
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if ("200".equals(response)) return "Correct";
            return "Error";
        }

        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(context, "Error al añadir el Evento a favoritos. Intentalo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(context, "Evento añadido a favoritos con exito.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class DeleteEvent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(params[0], params[1], new HashMap<>(), session.getToken());
            if (response.contains("Event deleted successfully.")) return "Correct";
            return "Error";
        }

        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(context, "Error al borrar el Evento.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context, "Evento borrado con exito.", Toast.LENGTH_LONG).show();
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = (Fragment) new EventsListShopFragment();
                transaction.replace(R.id.flContent, fragment);
                transaction.commit();
            }
        }
    }


    /**
     * Returns the total number of events in the data set held by the adapter.
     *
     * @return The total number of events in this adapter.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }
}
