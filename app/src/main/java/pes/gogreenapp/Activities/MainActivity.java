package pes.gogreenapp.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import pes.gogreenapp.Fragments.RewardsList;
import pes.gogreenapp.R;

/**
 * Created by Albert on 13/04/2017.
 */

public class MainActivity extends Activity {
    private String[] myDrawerTitles;
    private CharSequence myTitle;
    private ActionBarDrawerToggle myDrawerToggle;
    private CharSequence myDrawerTitle;
    private DrawerLayout myDrawerLayout;
    private ListView myDrawerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDrawerTitles = getResources().getStringArray(R.array.navigation_drawer_items);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        myDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.activity_navigation_drawer, myDrawerTitles));
        // Set the list's click listener
        myDrawerList.setOnItemClickListener((parent, view, position, id) -> {
            selectItem(position);
        });

    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new RewardsList();
        Bundle args = new Bundle();
        args.putInt(RewardsList.ARG_REWARDS_LIST_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        myDrawerList.setItemChecked(position, true);
        setTitle(myDrawerTitles[position]);
        myDrawerLayout.closeDrawer(myDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        myTitle = title;
        getActionBar().setTitle(myTitle);
    }
}
