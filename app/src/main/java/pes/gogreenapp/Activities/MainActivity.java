package pes.gogreenapp.Activities;

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
import pes.gogreenapp.Fragments.AboutUsFragment;
import pes.gogreenapp.Fragments.AccountManagerFragment;
import pes.gogreenapp.Fragments.RewardsListFragment;
import pes.gogreenapp.Fragments.SettingsFragment;
import pes.gogreenapp.Fragments.UserProfileFragment;
import pes.gogreenapp.Utils.SessionManager;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.UserData;

import static android.R.attr.id;
import static android.R.attr.subMenuArrow;
import static pes.gogreenapp.Utils.UserData.getUsernames;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private boolean switchActive = false;
    private SessionManager session;

    /**
     * onCreate method to initialize the Activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //-------------- Comprobar si esta logejat i fer login_activity sino ------------------
        session = SessionManager.getInstance(getApplicationContext());
        /*
          Call this function whenever you want to check user login_activity
          This will redirect user to LoginActivity is he is not
          logged in
          */
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
        View headerView = nvDrawer.getHeaderView(0);

        // Set the username on the header view
        TextView username = (TextView) headerView.findViewById(R.id.profile_username);
        username.setText(session.getUsername());

        // Switch menu set to no visible
        nvDrawer.getMenu().setGroupVisible(R.id.menu_switch, false);

        // On click image go to the profile fragment
        ImageView profileImage = (ImageView) headerView.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> {
            try {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, UserProfileFragment.class.newInstance())
                        .commit();
                mDrawer.closeDrawers();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        // On click arrow to use the Switch functionality
        ImageView arrowSwitch = (ImageView) headerView.findViewById(R.id.arrow_switch);
        arrowSwitch.setOnClickListener((click) -> {
            Menu menu = nvDrawer.getMenu();
            if (switchActive) {
                menu.setGroupVisible(R.id.menu_top, true);
                menu.setGroupVisible(R.id.menu_switch, false);
                arrowSwitch.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);

            } else {
                menu.setGroupVisible(R.id.menu_top, false);
                List<Integer> ids = new ArrayList<>();
                List<String> usernames = new ArrayList<>();

                try {
                    ids = UserData.getIds(getApplicationContext(),
                            session.getUsername());
                    usernames = UserData.getUsernames(getApplicationContext(),
                            session.getUsername());
                } catch (NullParametersException e) {
                    System.out.println(e.getMessage());
                }

                for (int i = 0; i < ids.size(); ++i) {
                    if (menu.findItem(ids.get(i)) == null) {
                        menu.add(R.id.menu_switch, ids.get(i), i + 50, usernames.get(i));
                    }
                }
                menu.setGroupVisible(R.id.menu_switch, true);
                arrowSwitch.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            }
            switchActive = !switchActive;
        });
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
     * @return boolean Return false to allow normal menu processing to proceed,
     * true to consume it here.
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
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
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
        } else if (menuItem.getOrder() < 100 && menuItem.getOrder() > 5) {
            // The item clicked is a user to Switch
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
