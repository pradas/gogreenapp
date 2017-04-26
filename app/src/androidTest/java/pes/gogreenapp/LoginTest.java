package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.EditText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pes.gogreenapp.Activities.LoginActivity;
import pes.gogreenapp.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class LoginTest {

    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText editText = (EditText) view;
                return editText.getError().toString().equals(expected);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    /**
     * If is logged in, log out.
     */
    @Before
    public void setup() {
        try {
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.nvView))
                    .perform(NavigationViewActions.navigateTo(R.id.log_out));
        } catch (NoMatchingViewException e) {
        }
    }

    @Test
    public void CorrectLogin() {
        onView(withId(R.id.username_edit_text))
                .perform(clearText(), typeText("admin"));
        onView(withId(R.id.password_user_text))
                .perform(clearText(), typeText("Password12"));
        onView(withId(R.id.buttonLogin))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isDisplayed()));

    }

    @Test
    public void IncorrectLogin() {
        onView(withId(R.id.username_edit_text))
                .perform(clearText(), typeText("a"));
        onView(withId(R.id.password_user_text))
                .perform(clearText(), typeText("a"));
        onView(withId(R.id.buttonLogin))
                .perform(click());
        onView(withText("Nombre o password incorrecto")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    @Test
    public void noPasswordAndNoUsername() {
        onView(withId(R.id.buttonLogin))
                .perform(click());
        onView(withId(R.id.password_user_text))
                .check(matches(withError("Contraseña necesaria")));
        onView(withId(R.id.username_edit_text))
                .check(matches(withError("Nombre necesario")));
    }

    @Test
    public void noPassword() {
        onView(withId(R.id.username_edit_text))
                .perform(clearText(), typeText("admin"));
        onView(withId(R.id.buttonLogin))
                .perform(click());
        onView(withId(R.id.password_user_text))
                .check(matches(withError("Contraseña necesaria")));
    }

    @Test
    public void noUsername() {
        onView(withId(R.id.password_user_text))
                .perform(clearText(), typeText("Password12"));
        onView(withId(R.id.buttonLogin))
                .perform(click());
        onView(withId(R.id.username_edit_text))
                .check(matches(withError("Nombre necesario")));
    }

    /**
     * Check if the Log in is open.
     */
    @Test
    public void checkLogInIsOpen() {
        onView(withId(R.id.TitleLogIn))
                .check(matches(isDisplayed()));
    }


}
