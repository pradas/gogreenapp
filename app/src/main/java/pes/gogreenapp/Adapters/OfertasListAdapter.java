package pes.gogreenapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pes.gogreenapp.Fragments.EventDetailedFragment;
import pes.gogreenapp.Fragments.OfertaDetailedFragment;
import pes.gogreenapp.Fragments.RewardDetailedFragment;
import pes.gogreenapp.Objects.Event;
import pes.gogreenapp.Objects.Oferta;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;


public class OfertasListAdapter extends RecyclerView.Adapter<OfertasListAdapter.ViewHolder> {
    private List<Oferta> ofertas;
    private Context context;
    private SessionManager session;
    private String TAG = "OfertasListAdapter";

    /**
     * Constructor that set the List of Rewards.
     *
     * @param ofertas non-null List of the Rewards.
     * @param context non-null context of the application.
     */
    public OfertasListAdapter(Context context, List<Oferta> ofertas) {
        this.context = context;
        this.ofertas = ofertas;
        this.session = SessionManager.getInstance();
    }

    /**
     * Setter of the Rewards List.
     *
     * @param ofertas non-null List of the Rewards.
     */
    public void setEvents(List<Oferta> ofertas) {
        this.ofertas = ofertas;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView description;
        TextView discount;
        TextView date;
        ImageButton fav;
        public Integer id;

        /**
         * Constructor of the View Holder that sets all the items.
         *
         * @param itemView valid View where to construct.
         */
        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ofertaImage);
            title = (TextView) itemView.findViewById(R.id.ofertaTitle);
            discount = (TextView) itemView.findViewById(R.id.ofertaPoints);
            date = (TextView) itemView.findViewById(R.id.ofertaEndDate);
            fav = (ImageButton) itemView.findViewById(R.id.ofertaFavoriteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);

                    //Sacar datos de la imagen seleccionada
                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    byte[] b = baos.toByteArray();
                    bundle.putByteArray("image",b);


                    FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Fragment fragment = (Fragment) new OfertaDetailedFragment();
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.flContent, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
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
    public OfertasListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ofertas_list_cardview, parent, false);
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
    public void onBindViewHolder(final OfertasListAdapter.ViewHolder holder, int position) {
        holder.id = ofertas.get(position).getId();
        holder.title.setText(ofertas.get(position).getTitle());
        holder.discount.setText(String.valueOf(ofertas.get(position).getPoints()));
        Date d = ofertas.get(position).getDate();
        holder.date.setText(new SimpleDateFormat("dd-MM-yyyy").format(d));
        if (ofertas.get(position).getImage() != null) {
            byte[] decodedBytes = ofertas.get(position).getImage();
            holder.image.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
        }
        else {
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.event);
            holder.image.setImageBitmap(icon);
        }

        if (ofertas.get(position).isFavorite()) {
            holder.fav.setTag("favoritefilled");
            holder.fav.setImageResource(R.drawable.ic_fav_filled);
        }
        else {
        holder.fav.setImageResource(R.drawable.ic_fav_void);
        holder.fav.setTag("favorite");
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
            String url = params[0] + params [2] + "/favourite-deals";
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null) return "Correct";
            return "Error";
        }

        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(context, "Error al añadir el Evento a favoritos. Intentalo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(context, "Evento añadido a favoritos con exito.", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Returns the total number of events in the data set held by the adapter.
     *
     * @return The total number of events in this adapter.
     */
    @Override
    public int getItemCount() {
        return ofertas.size();
    }
}