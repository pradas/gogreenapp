package pes.gogreenapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pes.gogreenapp.R;
import pes.gogreenapp.Objects.Reward;

/**
 * Created by Albert on 19/03/2017.
 */

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.ViewHolder> {

    private List<Reward> rewards;

    public RewardsAdapter(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public void setRewards(List<Reward> rewards) { this.rewards = rewards; }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView points;
        public TextView date;
        public TextView category;
        public ImageButton fav;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.rewardTitle);
            points = (TextView) itemView.findViewById(R.id.rewardPoints);
            date = (TextView) itemView.findViewById(R.id.rewardEndDate);
            category = (TextView) itemView.findViewById(R.id.rewardCategory);
            fav = (ImageButton) itemView.findViewById(R.id.favoriteButton);
        }
    }

    @Override
    public RewardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_rewards_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RewardsAdapter.ViewHolder holder, int position) {
        holder.title.setText(rewards.get(position).getTitle());
        holder.points.setText(String.valueOf(rewards.get(position).getPoints()));
        Date d = rewards.get(position).getEndDate();
        holder.date.setText(new SimpleDateFormat("dd/MM/yyyy").format(d));
        holder.category.setText(rewards.get(position).getCategory());
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.fav.getTag().equals("favorite")) {
                    holder.fav.setImageResource(R.mipmap.favoritefilled);
                    holder.fav.setTag("favoritefilled");
                } else {
                    holder.fav.setImageResource(R.mipmap.favorite);
                    holder.fav.setTag("favorite");
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return rewards.size();
    }
}
