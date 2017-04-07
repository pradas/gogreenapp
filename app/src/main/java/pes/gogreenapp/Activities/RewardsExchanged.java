package pes.gogreenapp.Activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import pes.gogreenapp.Adapters.RewardsExchangedAdapter;

/**
 * Created by Adry on 07/04/2017.
 */

public class RewardsExchanged extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RewardsExchangedAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private String TAG = RewardsExchanged.class.getSimpleName();

}
