package pes.gogreenapp.Adapters;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.R;

/**
 * Created by Adry on 07/04/2017.
 */

public class RewardsExchangedAdapter extends RecyclerView.Adapter<RewardsExchangedAdapter.ViewHolder> {

    private List<Reward> rewards;

    public RewardsExchangedAdapter(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public void setRewards(List<Reward> rewards) { this.rewards = rewards; }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView direction;
        public TextView points;
        public ImageView rewardImage;
        public Button use;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.rewardTitleExchanged);
            direction = (TextView) itemView.findViewById(R.id.rewardDirectionExchanged);
            points = (TextView) itemView.findViewById(R.id.rewardPointsExchanged);
            rewardImage = (ImageView) itemView.findViewById(R.id.rewardImageExchanged);
            use = (Button) itemView.findViewById(R.id.useRewardButton);
        }
    }

    @Override
    public RewardsExchangedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_rewards_exchanged, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RewardsExchangedAdapter.ViewHolder holder, int position) {
        holder.title.setText(rewards.get(position).getTitle());
        holder.direction.setText(rewards.get(position).getCategory()); //tendra que ser direccion obviamente
        holder.points.setText(String.valueOf(rewards.get(position).getPoints()));
        holder.use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getRootView().getContext());
                LayoutInflater factory = LayoutInflater.from(v.getRootView().getContext());
                final View view = factory.inflate(R.layout.code_qr, null);
                mBuilder.setTitle("ESCANEA EL CODIGO QR PARA UTILIZAR ESTA PROMOCIÓN");
                mBuilder.setView(view);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }


}
