package pes.gogreenapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pes.gogreenapp.Fragments.TabDealsFragment;
import pes.gogreenapp.Fragments.TabEventsFragment;
import pes.gogreenapp.Fragments.TabRewardsFragment;

/**
 * Created by Adrian on 23/05/2017.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    /**
     * Constructor of the adapter of the tabs
     * @param fm non-null FragmentManager that manage which tab it is pressed
     * @param numberOfTabs non-null number of tabs
     */
    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    /**
     * Obtains a position
     * @param position non-null tab pressed
     * @return the fragment associated with the tab pressed
     */
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                TabRewardsFragment tabRewardsFragment = new TabRewardsFragment();
                return tabRewardsFragment;
            case 1:
                TabEventsFragment tabEventsFragment = new TabEventsFragment();
                return tabEventsFragment;
            case 2:
                TabDealsFragment tabDealsFragment = new TabDealsFragment();
                return tabDealsFragment;
            default:
                return null;
        }
    }

    /**
     * @return the number of tabs
     */
    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
