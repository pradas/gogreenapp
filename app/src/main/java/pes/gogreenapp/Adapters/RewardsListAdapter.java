package pes.gogreenapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.test.espresso.core.deps.guava.io.ByteArrayDataOutput;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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

import pes.gogreenapp.Fragments.RewardDetailedFragment;
import pes.gogreenapp.Fragments.RewardsListFragment;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Albert on 19/03/2017.
 */
public class RewardsListAdapter extends RecyclerView.Adapter<RewardsListAdapter.ViewHolder> {
    private List<Reward> rewards;
    private Context context;
    private SessionManager session;

    /**
     * Constructor that set the List of Rewards.
     *
     * @param rewards non-null List of the Rewards.
     * @param context non-null context of the application.
     */
    public RewardsListAdapter(Context context, List<Reward> rewards) {
        this.context = context;
        this.rewards = rewards;
        this.session = SessionManager.getInstance();
    }

    /**
     * Setter of the Rewards List.
     *
     * @param rewards non-null List of the Rewards.
     */
    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView points;
        TextView date;
        TextView category;
        ImageButton fav;
        ImageButton exchange;
        ImageView background;
        public Integer id;

        /**
         * Constructor of the View Holder that sets all the items.
         *
         * @param itemView valid View where to construct.
         */
        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.rewardTitle);
            points = (TextView) itemView.findViewById(R.id.rewardPoints);
            date = (TextView) itemView.findViewById(R.id.rewardEndDate);
            category = (TextView) itemView.findViewById(R.id.rewardCategory);
            fav = (ImageButton) itemView.findViewById(R.id.favoriteButton);
            exchange = (ImageButton) itemView.findViewById(R.id.exchangeButton);
            background = (ImageView) itemView.findViewById(R.id.rewardBackgroundImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    bundle.putString("parent", "list");

                    //Sacar datos de la imagen seleccionada
                    Bitmap bitmap = ((BitmapDrawable) background.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    byte[] b = baos.toByteArray();
                    bundle.putByteArray("image",b);


                    FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Fragment fragment = (Fragment) new RewardDetailedFragment();
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.flContent, fragment);
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
    public RewardsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_list_cardview, parent, false);
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
    public void onBindViewHolder(final RewardsListAdapter.ViewHolder holder, int position) {
        holder.id = rewards.get(position).getId();
        holder.title.setText(rewards.get(position).getTitle());
        holder.points.setText(String.valueOf(rewards.get(position).getPoints()));
        Date d = rewards.get(position).getEndDate();
        holder.date.setText(new SimpleDateFormat("dd/MM/yyyy").format(d));
        holder.category.setText(rewards.get(position).getCategory());
        if(rewards.get(position).getImage() != null){
            holder.background.setImageBitmap(rewards.get(position).getImage());
        }
        else {
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_card_background);
            holder.background.setImageBitmap(icon);
        }
        if (rewards.get(position).isFavorite()) {
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
                new DeleteFavorite().execute("http://10.4.41.145/api/users/", "DELETE",
                        session.getUsername(), holder.id.toString());
                holder.fav.setImageResource(R.drawable.ic_fav_void);
                holder.fav.setTag("favorite");
            }
        });
        holder.exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getRootView().getContext());
                mBuilder.setMessage("¿Está seguro de que desea canjear esta promoción?");
                mBuilder.setPositiveButton(R.string.exchange, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (session.getPoints() < (Integer) rewards.get(position).getPoints()) {
                            Toast.makeText(context, "No tienes suficientes puntos para canjear este reward",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            new Exchange().execute("http://10.4.41.145/api/users/", "POST",
                                    session.getUsername(), holder.id.toString());
                            Integer points = session.getPoints();
                            points -= (Integer) rewards.get(position).getPoints();
                            session.setPoints(points);
                            FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            Fragment fragment = (Fragment) new RewardsListFragment();
                            transaction.replace(R.id.flContent, fragment);
                            transaction.commit();
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
            }
        });
    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class Exchange extends AsyncTask<String, Void, String> {

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
                Toast.makeText(context, "Error al canjear el Reward. Intentalo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(context, "Reward canjeado con exito.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class PostFavorite extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            bodyParams.put("reward_id", params[3]);
            String url = params[0] + params [2] + "/favourite-rewards";
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null) return "Correct";
            return "Error";
        }

        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(context, "Error al añadir el Reward a favoritos. Intentalo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(context, "Reward añadido a favoritos con exito.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asynchronous Task for the petition GET of all the Rewards.
     */
    private class DeleteFavorite extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            String url = params[0] + params [2] + "/favourite-rewards/" + params[3];
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null) return "Correct";
            return "Error";
        }

        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(context, "Error al eliminar el Reward de favoritos. Intentalo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(context, "Reward eliminado de favoritos con exito.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Returns the total number of rewards in the data set held by the adapter.
     *
     * @return The total number of rewards in this adapter.
     */
    @Override
    public int getItemCount() {
        return rewards.size();
    }
}
