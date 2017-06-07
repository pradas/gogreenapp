package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pes.gogreenapp.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pes.gogreenapp.Utils.EspressoTestsMatchers.withError;

/**
 * Created by Adrian on 07/06/2017.
 */

public class ForgottenPasswordTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Before the tests the Navigation Drawer is open, if cant be open it is because there isn't
     * a valid user logged. Due this, the setup do the Login with
     * the username user and the password Password12
     */
    @Before
    public void setup() {
        try {
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.nvView))
                    .perform(NavigationViewActions.navigateTo(R.id.log_out));
            onView(withId(R.id.forgotPassword))
                    .perform(click());
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.forgotPassword))
                    .perform(click());
        }
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasEditTextUserName() {
        onView(withId(R.id.username_edit_text_forgot_password)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasEditTextEmail() {
        onView(withId(R.id.email_edit_text_forgot_password)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasButton() {
        onView(withId(R.id.reSendPassword)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void errorEmptyUsername() {
        onView(withId(R.id.reSendPassword)).perform(click());
        onView(withId(R.id.username_edit_text_forgot_password)).check(matches(withError("Username necesario")));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void errorEmptyEmail() {
        onView(withId(R.id.reSendPassword)).perform(click());
        onView(withId(R.id.email_edit_text_forgot_password)).check(matches(withError("Correo necesario")));
    }
}
