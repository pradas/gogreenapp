/*
  All right reserverd to GoBros Devevelopers team.
  This code is free software; you can redistribute it and/or modify itunder the terms of
  the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

package pes.gogreenapp;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Exceptions.NullParametersException;
import pes.gogreenapp.Exceptions.UserNotExistException;
import pes.gogreenapp.Utils.MySQLiteHelper;
import pes.gogreenapp.Utils.SessionManager;
import pes.gogreenapp.Utils.UserData;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.doubleClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pes.gogreenapp.EspressoTestsMatchers.withDrawable;

/**
 * @author Albert
 */
@RunWith(AndroidJUnit4.class)
public class SwitchTest {

    private final String usernameShopper = "shopper";
    private final String usernameManager = "manager";
    private Integer idShopper = 0;
    private Integer idManager = 0;

    @Rule
    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Rule
    public TestName testName = new TestName();

    /**
     * Setup the database to test mode and inserts the data needed.
     * Before the tests if he Navigation Drawer is open, if cant be open it is because there isn't
     * a valid user logged. Due this, the setup do the Login with the username user
     * and the password Password12
     */
    @Before
    public void setUp() {

        MySQLiteHelper.changeToTestDatabase(myActivityRule.getActivity().getApplicationContext());
        SessionManager instance = SessionManager.getInstance(myActivityRule.getActivity().getApplicationContext());

        // see if the APP isn't in log in screen
        if (instance.isLoggedIn()) {
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.nvView)).perform(navigateTo(R.id.log_out));
        }

        try {
            String usernameUser = "user";
            if (testName.getMethodName().equals("checkUserSwitchToShopper")) {
                //the test check the switch functionality user to shopper
                UserData.createUser(usernameShopper, "token", 0, "shopper",
                        myActivityRule.getActivity().getApplicationContext());
                idShopper = UserData.getUserIdByUsername(usernameShopper,
                        myActivityRule.getActivity().getApplicationContext());

                // log as user
                onView(withId(R.id.username_edit_text)).perform(clearText(), typeText(usernameUser));
            } else if (testName.getMethodName().equals("checkUserSwitchToManager")) {
                //the test check the switch functionality user to manager
                UserData.createUser(usernameManager, "token", 0, "manager",
                        myActivityRule.getActivity().getApplicationContext());
                idManager = UserData.getUserIdByUsername(usernameManager,
                        myActivityRule.getActivity().getApplicationContext());

                // log as user
                onView(withId(R.id.username_edit_text)).perform(clearText(), typeText(usernameUser));
            } else if (testName.getMethodName().equals("checkArrowUpOnFirstClick") ||
                    testName.getMethodName().equals("checkArrowDownOnSecondClick") ||
                    testName.getMethodName().equals("checkArrowDownOnInitialState")) {
                //the test check that the arrow is up on first click
                // log as user
                onView(withId(R.id.username_edit_text)).perform(clearText(), typeText(usernameUser));
            }
        } catch (NullParametersException | UserNotExistException e) {
            System.out.println(e.getMessage());
        }

        // log in and opend the Drawer Layout
        String password = "Password12";
        onView(withId(R.id.password_user_text)).perform(clearText(), typeText(password));
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
    }

    /**
     * Clean the insert done on setUp method and logout
     */
    @After
    public void clean() {

        onView(withId(R.id.nvView)).perform(navigateTo(R.id.log_out));
        MySQLiteHelper.changeToDevelopmentDatabase(myActivityRule.getActivity().getApplicationContext());
    }

    /**
     * Check that user can switch to shopper correctly
     */
    @Test
    public void checkUserSwitchToShopper() {

        onView(withId(R.id.arrow_switch)).perform(click());
        onView(withId(R.id.nvView)).perform(navigateTo(idShopper));
        onView(withId(R.id.header_username)).check(matches(withText(usernameShopper)));

    }

    /**
     * Check that ser can switch to manager correctly
     */
    @Test
    public void checkUserSwitchToManager() {

        onView(withId(R.id.arrow_switch)).perform(click());
        onView(withId(R.id.nvView)).perform(navigateTo(idManager));
        onView(withId(R.id.header_username)).check(matches(withText(usernameManager)));

    }

    /**
     * Check if the icon of the arrow on the header is down on the second click
     */
    @Test
    public void checkArrowDownOnInitialState() {

        onView(withId(R.id.arrow_switch)).check(matches(withDrawable(R.drawable.ic_arrow_drop_down_black_24dp)));

    }

    /**
     * Check if the icon of the arrow on the header is up on the first click
     */
    @Test
    public void checkArrowUpOnFirstClick() {

        onView(withId(R.id.arrow_switch)).perform(click());
        onView(withId(R.id.arrow_switch)).check(matches(withDrawable(R.drawable.ic_arrow_drop_up_black_24dp)));

    }

    /**
     * Check if the icon of the arrow on the header is down on the second click
     */
    @Test
    public void checkArrowDownOnSecondClick() {

        onView(withId(R.id.arrow_switch)).perform(doubleClick());
        onView(withId(R.id.arrow_switch)).check(matches(withDrawable(R.drawable.ic_arrow_drop_down_black_24dp)));

    }
}
