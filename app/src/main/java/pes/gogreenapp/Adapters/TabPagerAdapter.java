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

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

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

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
