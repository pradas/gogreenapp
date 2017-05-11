package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pes.gogreenapp.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class NavigationDrawerTest {

    private final String username = "user";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Before the tests if he Navigation Drawer is open, if cant be open it is because there isn't
     * a valid user logged. Due this, the setup do the Login with the username user
     * and the password Password12
     */
    @Before
    public void setup() {

        try {
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.username_edit_text)).perform(clearText(), typeText(username));
            onView(withId(R.id.password_user_text)).perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin)).perform(click());
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        }
    }

    /**
     * Check if the Navigation Drawer is open.
     */
    @Test
    public void checkNavigationDrawerIsOpen() {

        onView(withId(R.id.nvView)).check(matches(isDisplayed()));
    }

    /**
     * Check if on Rewards menu item click the View rewards_list is displayed
     */
    @Test
    public void checkRewardsAccess() {

        onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.rewards_list_fragment));
        onView(withId(R.id.rewards_list)).check(matches(isDisplayed()));
    }

    /**
     * Check if on About Us menu item click the View about_us is displayed
     */
    @Test
    public void checkAboutUsAccess() {

        onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.about_us_fragment));
        onView(withId(R.id.about_us)).check(matches(isDisplayed()));
    }

    /**
     * Check if on Settings menu item click the View settings is displayed
     */
    @Test
    public void checkSettingsAccess() {

        onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.settings_fragment));
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }

    /**
     * Check if on Account Manager menu item click the View account_manager is displayed
     */
    @Test
    public void checkAccountManagerAccess() {

        onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.account_manager_fragment));
        onView(withId(R.id.account_manager)).check(matches(isDisplayed()));
    }

    /**
     * Check if on User Profile menu item click the View user_profile is displayed
     */
    @Test
    public void checkUserProfileAccess() {

        onView(withId(R.id.profile_image)).perform(click());
        onView(withId(R.id.user_profile)).check(matches(isDisplayed()));
    }

    /**
     * Check if the username is showed at the header
     */
    @Test
    public void checkUsernameShowedOnHeader() {

        onView(withId(R.id.header_username)).check(matches(withText(username)));
    }
}