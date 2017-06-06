package pes.gogreenapp;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Utils.SessionManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class NavigationDrawerTest {

    private final String usernameUser = "user";
    private final String usernameShopper = "shoper";
    private final String usernameManager = "manager";
    private final String password = "Password12";

    @Rule
    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Rule
    public TestName testName = new TestName();

    /**
     * Before the tests if he Navigation Drawer is open, if cant be open it is because there isn't
     * a valid user logged. Due this, the setup do the Login with the username user
     * and the password Password12
     */
    @Before
    public void setup() {

        SessionManager instance = SessionManager.getInstance(myActivityRule.getActivity().getApplicationContext());

        if (testName.getMethodName().equals("checkNavigationDrawerIsOpen") ||
                testName.getMethodName().equals("checkAboutUsAccess") ||
                testName.getMethodName().equals("checkRewardsAccess") ||
                testName.getMethodName().equals("checkSettingsAccess") ||
                testName.getMethodName().equals("checkUserProfileAccess") ||
                testName.getMethodName().equals("checkUsernameUserShowedOnHeader")) {
            if (instance.isLoggedIn() && !usernameUser.equals(instance.getUsername())) {
                onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
                onView(withId(R.id.nvView)).perform(navigateTo(R.id.log_out));
            }
            if (!instance.isLoggedIn()) {
                onView(withId(R.id.username_edit_text)).perform(clearText(), typeText(usernameUser));
            }
        } else if (testName.getMethodName().equals("checkUsernameShopperShowedOnHeader") ||
                testName.getMethodName().equals("checkShopAccess") ||
                testName.getMethodName().equals("checkGivePointsAccess")) {
            if (instance.isLoggedIn() && !usernameShopper.equals(instance.getUsername())) {
                onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
                onView(withId(R.id.nvView)).perform(navigateTo(R.id.log_out));
            }
            if (!instance.isLoggedIn()) {
                onView(withId(R.id.username_edit_text)).perform(clearText(), typeText(usernameShopper));
            }
        } else if (testName.getMethodName().equals("checkUsernameManagerShowedOnHeader") ||
                testName.getMethodName().equals("checkCreateEventAccess") ||
                testName.getMethodName().equals("checkEmployeeManagerAccess") ||
                testName.getMethodName().equals("checkNewDealAccess")) {
            if (instance.isLoggedIn() && !usernameManager.equals(instance.getUsername())) {
                onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
                onView(withId(R.id.nvView)).perform(navigateTo(R.id.log_out));
            }
            if (!instance.isLoggedIn()) {
                onView(withId(R.id.username_edit_text)).perform(clearText(), typeText(usernameManager));
            }
        }

        if (!instance.isLoggedIn()) {
            onView(withId(R.id.password_user_text)).perform(clearText(), typeText(password));
            onView(withId(R.id.buttonLogin)).perform(click());
        }
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
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

        onView(withId(R.id.nvView)).perform(navigateTo(R.id.rewards_list_fragment));
        onView(withId(R.id.rewards_list)).check(matches(isDisplayed()));
    }

    /**
     * Check if on About Us menu item click the View about_us is displayed
     */
    @Test
    public void checkAboutUsAccess() {

        onView(withId(R.id.nvView)).perform(navigateTo(R.id.about_us_fragment));
        onView(withId(R.id.about_us)).check(matches(isDisplayed()));
    }

    /**
     * Check if on Settings menu item click the View settings is displayed
     */
    @Test
    public void checkSettingsAccess() {

        onView(withId(R.id.nvView)).perform(navigateTo(R.id.settings_fragment));
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }

    /**
     * Check if on Account Manager menu item click the View account_manager is displayed
     */
    @Test
    public void checkAccountManagerAccess() {

        onView(withId(R.id.nvView)).perform(navigateTo(R.id.account_manager_fragment));
        onView(withId(R.id.account_manager)).check(matches(isDisplayed()));
    }

    /**
     * Check if on User Profile menu item click the View user_profile is displayed
     */
    @Test
    public void checkUserProfileAccess() {

        onView(withId(R.id.profile_image)).perform(click());
        onView(withId(R.id.user_profile_layout)).check(matches(isDisplayed()));
    }

    /**
     * Check if the username of the User is showed at the header
     */
    @Test
    public void checkUsernameUserShowedOnHeader() {

        onView(withId(R.id.header_username)).check(matches(withText(usernameUser)));
    }

    /**
     * Check if the username of the Shopper is showed at the header
     */
    @Test
    public void checkUsernameShopperShowedOnHeader() {

        onView(withId(R.id.header_username)).check(matches(withText(usernameShopper)));
    }

    /**
     * Check if on Shop menu item click the View shop_view is displayed
     */
    @Test
    public void checkShopAccess() {

        onView(withId(R.id.nvView)).perform(navigateTo(R.id.shop_view_fragment));
        onView(withId(R.id.shop_view_fragment)).check(matches(isDisplayed()));
    }

    /**
     * Check if on Give Points menu item click the View give_points is displayed
     */
    @Test
    public void checkGivePointsAccess() {

        onView(withId(R.id.nvView)).perform(navigateTo(R.id.give_points_fragment));
        onView(withId(R.id.layoutGivePoints)).check(matches(isDisplayed()));
    }

    /**
     * Check if the username of the Manager is showed at the header
     */
    @Test
    public void checkUsernameManagerShowedOnHeader() {

        onView(withId(R.id.header_username)).check(matches(withText(usernameManager)));
    }

    /**
     * Check if on New Deal menu item click the View new_deal is displayed
     */
    @Test
    public void checkNewDealAccess() {

        onView(withId(R.id.nvView)).perform(navigateTo(R.id.new_deal_fragment));
        onView(withId(R.id.create_oferta_event)).check(matches(isDisplayed()));
    }

    /**
     * Check if on Create Event menu item click the View create_event is displayed
     */
    @Test
    public void checkCreateEventAccess() {

        onView(withId(R.id.nvView)).perform(navigateTo(R.id.create_event_fragment));
        onView(withId(R.id.ScrollViewCreateEvent)).check(matches(isDisplayed()));
    }

    /**
     * Check if on Gestor Empleados menu item click the View employee_manager is displayed
     */
    @Test
    public void checkEmployeeManagerAccess() {

        onView(withId(R.id.nvView)).perform(navigateTo(R.id.employee_manager_fragment));
        onView(withId(R.id.employee_manager_view)).check(matches(isDisplayed()));
    }

}