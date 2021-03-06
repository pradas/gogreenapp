package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pes.gogreenapp.Activities.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static pes.gogreenapp.Utils.EspressoTestsMatchers.withError;

public class LoginTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    /**
     * If is logged in, log out.
     */
    @Before
    public void setup() {

        try {
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.log_out));
        } catch (NoMatchingViewException e) {
        }
    }

    /**
     * Check that if the user put the username and
     * password correctly the principal layout is displayed.
     */
    @Test
    public void CorrectLogin() {

        onView(withId(R.id.username_edit_text)).perform(clearText(), typeText("manager"));
        onView(withId(R.id.password_user_text)).perform(clearText(), typeText("Password12"));
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));

    }

    /**
     * Check that if the user don't put the username and
     * password correctly there is an error in a Toast.
     */
    @Test
    public void IncorrectLogin() {

        onView(withId(R.id.username_edit_text)).perform(clearText(), typeText("a"));
        onView(withId(R.id.password_user_text)).perform(clearText(), typeText("a"));
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withText("Nombre o password incorrecto"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }

    /**
     * Check that if the user don't put the username and
     * password there is an error in the password_user_text and
     * username_edit_text.
     */
    @Test
    public void noPasswordAndNoUsername() {

        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.password_user_text)).check(matches(withError("Contraseña necesaria")));
        onView(withId(R.id.username_edit_text)).check(matches(withError("Nombre necesario")));
    }

    /**
     * Check that if the user don't put the password there is an error
     * in the password_user_text.
     */
    @Test
    public void noPassword() {

        onView(withId(R.id.username_edit_text)).perform(clearText(), typeText("user"));
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.password_user_text)).check(matches(withError("Contraseña necesaria")));
    }

    /**
     * Check that if the user don't put the username there is an error
     * in the username_edit_text.
     */
    @Test
    public void noUsername() {

        onView(withId(R.id.password_user_text)).perform(clearText(), typeText("Password12"));
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.username_edit_text)).check(matches(withError("Nombre necesario")));
    }

    /**
     * Check if the Log in is open.
     */
    @Test
    public void checkLogInIsOpen() {

        onView(withId(R.id.TitleLogIn)).check(matches(isDisplayed()));
    }


}
