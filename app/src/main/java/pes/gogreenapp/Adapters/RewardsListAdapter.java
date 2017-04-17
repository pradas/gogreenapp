package pes.gogreenapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pes.gogreenapp.Fragments.RewardDetailedFragment;
import pes.gogreenapp.R;
import pes.gogreenapp.Objects.Reward;

/**
 * Created by Albert on 19/03/2017.
 */
public class RewardsListAdapter extends RecyclerView.Adapter<RewardsListAdapter.ViewHolder> {
    private List<Reward> rewards;
    private Context context;

    /**
     * Constructor that set the List of Rewards.
     *
     * @param rewards non-null List of the Rewards.
     */
    public RewardsListAdapter(Context context, List<Reward> rewards) {
        this.context = context;
        this.rewards = rewards;
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
        Button exchange;
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
            exchange = (Button) itemView.findViewById(R.id.exchangeButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);

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
        holder.fav.setOnClickListener(v -> {
            if (holder.fav.getTag().equals("favorite")) {
                holder.fav.setImageResource(R.mipmap.favoritefilled);
                holder.fav.setTag("favoritefilled");
            } else {
                holder.fav.setImageResource(R.mipmap.favorite);
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
     * Returns the total number of rewards in the data set held by the adapter.
     *
     * @return The total number of rewards in this adapter.
     */
    @Override
    public int getItemCount() {
        return rewards.size();
    }
}
