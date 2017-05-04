package pes.gogreenapp.Adapters;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pes.gogreenapp.Fragments.QRCodeFragment;
import pes.gogreenapp.Fragments.RewardDetailedFragment;
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.GlobalPreferences;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.Objects.SessionManager;
import pes.gogreenapp.R;

/**
 * Created by Adry on 07/04/2017.
 */

public class RewardsExchangedAdapter extends RecyclerView.Adapter<RewardsExchangedAdapter.ViewHolder> {

    private List<Reward> rewards;
    private Context context;
    private String userName;
    private SessionManager session;


    public RewardsExchangedAdapter(Context context, List<Reward> rewards) {
        this.context = context;
        this.rewards = rewards;
        this.session = new SessionManager(context, new GlobalPreferences(context).getUser());
        userName = session.getUserName();
    }

    public void setRewards(List<Reward> rewards) { this.rewards = rewards; }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView category;
        public TextView points;
        public TextView endDate;
        public ImageView rewardImage;
        public Button use;
        public ImageButton fav;
        public Integer id;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.rewardTitle);
            category = (TextView) itemView.findViewById(R.id.rewardCategory);
            points = (TextView) itemView.findViewById(R.id.rewardPoints);
            endDate = (TextView) itemView.findViewById(R.id.rewardEndDate);
            rewardImage = (ImageView) itemView.findViewById(R.id.rewardImage);
            use = (Button) itemView.findViewById(R.id.exchangeButton);
            use.setText("Utilizar");
            fav = (ImageButton) itemView.findViewById(R.id.favoriteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    bundle.putString("parent", "exchangeds");

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

    @Override
    public RewardsExchangedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_list_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RewardsExchangedAdapter.ViewHolder holder, int position) {
        holder.id = rewards.get(position).getId();
        holder.title.setText(rewards.get(position).getTitle());
        holder.category.setText(rewards.get(position).getCategory());
        Date d = rewards.get(position).getEndDate();
        holder.endDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(d));
        holder.points.setText(String.valueOf(rewards.get(position).getPoints()));
        if (rewards.get(position).isFavorite()) {
            holder.fav.setTag("favoritefilled");
            holder.fav.setImageResource(R.mipmap.favoritefilled);
        }
        else {
            holder.fav.setImageResource(R.mipmap.favorite);
            holder.fav.setTag("favorite");
        }
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.fav.getTag().equals("favorite")) {
                    new PostFavorite().execute("http://10.4.41.145/api/users/", "POST",
                            session.getUserName(), holder.id.toString());
                    holder.fav.setImageResource(R.mipmap.favoritefilled);
                    holder.fav.setTag("favoritefilled");
                } else {
                    holder.fav.setImageResource(R.mipmap.favorite);
                    holder.fav.setTag("favorite");
                }
            }
        });
        holder.use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.4.41.145/api/users/" + userName + "/rewards/" + rewards.get(position).getId();
                Bundle bundle = new Bundle();
                bundle.putString("url", url);

                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = (Fragment) new QRCodeFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.flContent, fragment);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return rewards.size();
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
}
