/*
 * All right reserverd to GoBros Devevelopers team.
 * This code is free software; you can redistribute it and/or modify itunder the terms of
 * the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

package pes.gogreenapp.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.Exceptions.NullParametersException;
import pes.gogreenapp.Exceptions.UserNotExistException;
import pes.gogreenapp.Fragments.AboutUsFragment;
import pes.gogreenapp.Fragments.AccountManagerFragment;
import pes.gogreenapp.Fragments.CreateEventFragment;
import pes.gogreenapp.Fragments.EditEventFragment;
import pes.gogreenapp.Fragments.RewardsListFragment;
import pes.gogreenapp.Fragments.SettingsFragment;
import pes.gogreenapp.Fragments.UserProfileFragment;
import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.SessionManager;
import pes.gogreenapp.Utils.UserData;

/**
 * @author Albert
 */
public class MainActivity extends AppCompatActivity {

    private static final String ROLE_MANAGER = "manager";
    private static final String ROLE_SHOPPER = "shopper";
    private static final String ROLE_USER = "user";
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private Menu menu;
    private View headerView;
    private boolean switchActive = false;
    private SessionManager session;

    /**
     * onCreate method to initialize the Activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this
     *                           Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get instance of Session Manager and check if user is logged
        session = SessionManager.getInstance(getApplicationContext());
        session.checkLogin();

        /* Set main layout */
        setContentView(R.layout.main_activity);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Get the header view
        headerView = nvDrawer.getHeaderView(0);

        // Set the username on the header view
        TextView username = (TextView) headerView.findViewById(R.id.header_username);
        username.setText(session.getUsername());

        // Switch and other roles menu set to no visible
        menu = nvDrawer.getMenu();
        onSwitchRefreshMenusVisibility();

        // On click image go to the profile fragment
        ImageView profileImage = (ImageView) headerView.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> {
            try {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, UserProfileFragment.class.newInstance()).commit();
                mDrawer.closeDrawers();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        // On click arrow to use the Switch functionality
        ImageView arrowSwitch = (ImageView) headerView.findViewById(R.id.arrow_switch);
        arrowSwitch.setOnClickListener((click) -> {

            List<Integer> ids = new ArrayList<>();

            try {
                ids = UserData.getIds(getApplicationContext(), session.getUsername());
            } catch (NullParametersException e) {
                System.out.println(e.getMessage());
            }

            if (switchActive) {
                // Load menu items of the current role
                onSwitchRefreshMenusVisibility();

                // delete all the menu items that are users on the menu_switch
                for (int id : ids) {
                    if (menu.findItem(id) != null) {
                        menu.removeItem(id);
                    }
                }

                arrowSwitch.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);

            } else {
                // Load menu items of the Switch functionally
                menu.setGroupVisible(R.id.menu_top, false);
                List<String> usernames = new ArrayList<>();

                try {
                    // Get the username of the users
                    usernames = UserData.getUsernames(getApplicationContext(), session.getUsername());
                } catch (NullParametersException e) {
                    System.out.println(e.getMessage());
                }

                // Iterate through users and ids and inserts the menu item that didn't exist yet
                for (int i = 0; i < ids.size(); ++i) {
                    if (menu.findItem(ids.get(i)) == null) {
                        menu.add(R.id.menu_switch, ids.get(i), i + 21, usernames.get(i));
                    }
                }

                // Set the menus visibility accordint to the role
                menu.setGroupVisible(R.id.menu_switch, true);
                if (ROLE_USER.equals(session.getRole())) {
                    menu.setGroupVisible(R.id.menu_top, false);
                } else if (ROLE_MANAGER.equals(session.getRole())) {
                    menu.setGroupVisible(R.id.menu_manager, false);
                } else if (ROLE_SHOPPER.equals(session.getRole())) {
                    menu.setGroupVisible(R.id.menu_shopper, false);
                }

                //Change the direction of the arrow icon
                arrowSwitch.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            }

            // Deactivate the boolean that marks the Switch
            switchActive = !switchActive;
        });
    }

    /**
     * Called on switch of role. Change the visibility of the different menus
     */
    private void onSwitchRefreshMenusVisibility() {

        menu.setGroupVisible(R.id.menu_switch, false);
        if (ROLE_SHOPPER.equals(session.getRole())) {
            menu.setGroupVisible(R.id.menu_shopper, true);
        } else {
            menu.setGroupVisible(R.id.menu_shopper, false);
        }
        if (ROLE_MANAGER.equals(session.getRole())) {
            menu.setGroupVisible(R.id.menu_manager, true);
        } else {
            menu.setGroupVisible(R.id.menu_manager, false);
        }
        if (ROLE_USER.equals(session.getRole())) {
            menu.setGroupVisible(R.id.menu_top, true);
        } else {
            menu.setGroupVisible(R.id.menu_top, false);
        }
    }

    /**
     * Construct a new ActionBarDrawerToggle with a Toolbar.
     *
     * @return the new Action Bar with all the components added.
     */
    @Contract(" -> !null")
    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     *
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Set a listener that will be notified when a menu item is selected.
     *
     * @param navigationView view of the Navigation Drawer.
     */
    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            selectDrawerItem(menuItem);
            return true;
        });
    }

    /**
     * Create a new fragment and specify the fragment to show based on nav item clicked
     *
     * @param menuItem item clicked on the Navigation Drawer
     */
    public void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass;
        if (menuItem.getItemId() == R.id.log_out) {
            session.logoutUser();

            // Delete the User from the SQLite
            try {
                UserData.deleteUser(session.getUsername(), getApplicationContext());
            } catch (NullParametersException e) {
                e.printStackTrace();
            }
        } else if (menuItem.getOrder() < 100 && menuItem.getOrder() > 20) {
            // The item clicked is a user to Switch
            try {
                // Get the new user data and change the Session Manager info
                User user = UserData.getUserByUsername(menuItem.getTitle().toString(), getApplicationContext());
                session.switchInfoLoginSession(user.getUsername(), user.getRole(), user.getToken(),
                        user.getCurrentPoints());

                // Refresh the Navigation Drawer menu items to apply the new role
                onSwitchRefreshMenusVisibility();

                // Refresh the header info of the Navigation Drawer menu
                TextView usernameHeader = (TextView) headerView.findViewById(R.id.header_username);
                usernameHeader.setText(session.getUsername());

                // Delete the actual menuitem
                menu.removeItem(menuItem.getItemId());

                //Change the Switch arrow to down
                ImageView arrowSwitch = (ImageView) headerView.findViewById(R.id.arrow_switch);
                arrowSwitch.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);

                // Deactivate the boolean that marks the Switch
                switchActive = !switchActive;

            } catch (NullParametersException | UserNotExistException e) {
                System.out.println(e.getMessage());
            }
        } else {
            switch (menuItem.getItemId()) {
                case R.id.rewards_list_fragment:
                    fragmentClass = RewardsListFragment.class;
                    break;
                case R.id.settings_fragment:
                    fragmentClass = SettingsFragment.class;
                    break;
                case R.id.about_us_fragment:
                    fragmentClass = AboutUsFragment.class;
                    break;
                case R.id.account_manager_fragment:
                    fragmentClass = AccountManagerFragment.class;
                    break;
                case R.id.profile_image:
                    fragmentClass = UserProfileFragment.class;
                    break;
                case R.id.edit_event_fragment:
                    fragmentClass = CreateEventFragment.class;
                    break;
                default:
                    fragmentClass = RewardsListFragment.class;
            }

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);

            // Set action bar title
            setTitle(menuItem.getTitle());

            // Close the navigation drawer
            mDrawer.closeDrawers();
        }
    }

    /**
     * Synchronize the state of the drawer indicator/affordance with the linked DrawerLayout.
     *
     * @param savedInstanceState last functional state of this activity.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    /**
     * Change the actual configuration of the Drawer Toggle.
     *
     * @param newConfig the new Configuration.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

}
