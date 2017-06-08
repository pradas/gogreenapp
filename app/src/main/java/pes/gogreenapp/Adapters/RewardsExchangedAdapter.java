package pes.gogreenapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pes.gogreenapp.Fragments.FilterDialogFragment;
import pes.gogreenapp.Fragments.QRCodeFragment;
import pes.gogreenapp.Fragments.RewardDetailedFragment;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.HttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * Created by Adry on 07/04/2017.
 */

public class RewardsExchangedAdapter extends RecyclerView.Adapter<RewardsExchangedAdapter.ViewHolder> {

    private List<Reward> rewards;
    private Context context;
    private String userName;
    private SessionManager session;

    /**
     * Constructor that set the List of Rewards.
     *
     * @param rewards non-null List of the Rewards.
     * @param context non-null context of the application.
     * @param userName non-null userName of the user of the applciation.
     */
    public RewardsExchangedAdapter(Context context, List<Reward> rewards, String userName) {
        this.context = context;
        this.rewards = rewards;
        this.session = SessionManager.getInstance();
        userName = session.getUsername();
    }

    /**
     * Setter of the Rewards List.
     *
     * @param rewards non-null List of the Rewards.
     */
    public void setRewards(List<Reward> rewards) { this.rewards = rewards; }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView category;
        public TextView points;
        public TextView endDate;
        public ImageView rewardImage;
        public ImageButton use;
        public ImageButton fav;
        public Integer id;

        /**
         * Constructor of the View Holder that sets all the items.
         *
         * @param itemView valid View where to construct.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.rewardTitle);
            category = (TextView) itemView.findViewById(R.id.rewardCategory);
            points = (TextView) itemView.findViewById(R.id.rewardPoints);
            endDate = (TextView) itemView.findViewById(R.id.rewardEndDate);
            rewardImage = (ImageView) itemView.findViewById(R.id.rewardBackgroundImage);
            use = (ImageButton) itemView.findViewById(R.id.exchangeButton);
            fav = (ImageButton) itemView.findViewById(R.id.favoriteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    bundle.putString("parent", "exchangeds");


                    Bitmap bitmap = ((BitmapDrawable) rewardImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    byte[] b = baos.toByteArray();
                    bundle.putByteArray("image",b);


                    FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Fragment fragment = (Fragment) new RewardDetailedFragment();
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
    public RewardsExchangedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_list_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RewardsListAdapter.ViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RewardsExchangedAdapter.ViewHolder holder, int position) {
        holder.id = rewards.get(position).getId();
        holder.title.setText(rewards.get(position).getTitle());
        holder.category.setText(rewards.get(position).getCategory());
        Date d = rewards.get(position).getEndDate();
        holder.endDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(d));
        holder.points.setText(String.valueOf(rewards.get(position).getPoints()));
        if(rewards.get(position).getImage() != null){
            holder.rewardImage.setImageBitmap(rewards.get(position).getImage());
        }
        else {
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_card_background);
            holder.rewardImage.setImageBitmap(icon);
        }
        if (rewards.get(position).isFavorite()) {
            holder.fav.setTag("favoritefilled");
            holder.fav.setImageResource(R.drawable.ic_fav_filled);
        }
        else {
            holder.fav.setImageResource(R.drawable.ic_fav_void);
            holder.fav.setTag("favorite");
        }
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        holder.use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.4.41.145/use-reward/" + rewards.get(position).getId();

                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = (Fragment) new QRCodeFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.flContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    /**
     * Asynchronous Task for the petition POST of a favorite to the reward
     */
    private class PostFavorite extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the method petition,
         *               params[2] is the username of the user
         *               params[3] is the id of the reward
         * @return "Error" if not favourited or "Correct" if it is favourited
         */
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

        /**
         * Called when doInBackground is finished, Toast an error if there is an error.
         *
         * @param result If is "Error" or "Correct" makes the toast.
         */
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(context, "Error al añadir el Reward a favoritos. Intentalo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(context, "Reward añadido a favoritos con exito.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Asynchronous Task for the petition DELETE of a favorite to the reward
     */
    private class DeleteFavorite extends AsyncTask<String, Void, String> {

        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the method petition,
         *               params[2] is the username of the user
         *               params[3] is the id of the reward
         * @return "Error" if not favourited or "Correct" if it is favourited
         */
        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> bodyParams = new HashMap<>();
            String url = params[0] + params [2] + "/favourite-rewards/" + params[3];
            String response = httpHandler.makeServiceCall(url, params[1], bodyParams, session.getToken());
            if (response != null) return "Correct";
            return "Error";
        }

        /**
         * Called when doInBackground is finished, Toast an error if there is an error.
         *
         * @param result If is "Error" or "Correct" makes the toast.
         */
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("Error")) {
                Toast.makeText(context, "Error al eliminar el Reward de favoritos. Intentalo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(context, "Reward eliminado de favoritos con exito.", Toast.LENGTH_LONG).show();
        }
    }
}
