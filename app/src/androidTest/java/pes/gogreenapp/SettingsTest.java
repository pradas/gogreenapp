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
import static pes.gogreenapp.Utils.EspressoTestsMatchers.withError;

/**
 * Created by Adrian on 07/06/2017.
 */

public class SettingsTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Before the tests the Navigation Drawer is open, if cant be open it is because there isn't
     * a valid user logged. Due this, the setup do the Login with
     * the username manager and the password Password12
     */
    @Before
    public void setup() {
        try {
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.nvView))
                    .perform(NavigationViewActions.navigateTo(R.id.settings_fragment));
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.username_edit_text))
                    .perform(clearText(), typeText("manager"));
            onView(withId(R.id.password_user_text))
                    .perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin))
                    .perform(click());
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.nvView))
                    .perform(NavigationViewActions.navigateTo(R.id.settings_fragment));
        }
    }

    /**
     * Check if the xml have the new pass EditText
     */
    @Test
    public void fragmentHasNewPassword() {
        onView(withId(R.id.newPass)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the confirm pass EditText
     */
    @Test
    public void fragmentHasConfirmNewPass() {
        onView(withId(R.id.confirmNewPass)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the button change
     */
    @Test
    public void fragmentHasButtonChange() {
        onView(withId(R.id.changePass)).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking the button change with empty new pass display error
     */
    @Test
    public void errorEmptyPass() {
        onView(withId(R.id.changePass)).perform(click());
        onView(withId(R.id.newPass)).check(matches(withError("La nueva contraseña es necesaria")));
    }

    /**
     * Check if clicking the button change with empty confirm pass display error
     */
    @Test
    public void errorEmptyConfirmPass() {
        onView(withId(R.id.changePass)).perform(click());
        onView(withId(R.id.confirmNewPass)).check(matches(withError("Es necesario confirmar la nueva contraseña")));
    }

    /**
     * Check if typing different passwords display error
     */
    @Test
    public void passwordDontMatch() {
        onView(withId(R.id.newPass)).perform(clearText(),typeText("b"));
        onView(withId(R.id.confirmNewPass)).perform(clearText(),typeText("a"));
        onView(withId(R.id.changePass)).perform(click());
        onView(withId(R.id.confirmNewPass)).check(matches(withError("Las contraseñas no coinciden")));
    }

}
