package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pes.gogreenapp.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.EasyMock2Matchers.equalTo;

/**
 * Created by Adrian on 20/05/2017.
 */

public class GivePointsTest {
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
                    .perform(NavigationViewActions.navigateTo(R.id.log_out));
            onView(withId(R.id.username_edit_text))
                    .perform(clearText(), typeText("manager"));
            onView(withId(R.id.password_user_text))
                    .perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin))
                    .perform(click());
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.nvView))
                    .perform(NavigationViewActions.navigateTo(R.id.give_points_fragment));
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
                    .perform(NavigationViewActions.navigateTo(R.id.give_points_fragment));
        }
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasSwitch() {
        onView(withId(R.id.switchModeItem)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the list
     */
    @Test
    public void fragmentHasList() {
        onView(withId(R.id.listViewGivePoints)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have button to show another user
     */
    @Test
    public void fragmentHasButtonAnotherUser() {
        onView(withId(R.id.action_add_user)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have button to grant points
     */
    @Test
    public void fragmentHasButtonGrantPoints() {
        onView(withId(R.id.grantPointsToUsers)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button in events position by default
     */
    @Test
    public void switchEventsDefault() {
        onView(withId(R.id.switchModeItem)).check(matches(isNotChecked()));
    }

    /**
     * ItemEvents have username
     */
    @Test
    public void listEventsHasUserNameEditText() {
        onView(withId(R.id.switchModeItem)).check(matches(isNotChecked()));
        onView(withId(R.id.listViewGivePoints)).check(matches(hasDescendant(withId(R.id.userNameToGiveByEvents))));
    }

    /**
     * ItemEvents have username with hint Nombre de usuario
     */
    @Test
    public void listEventsHasUserNameEditTextWithHint() {
        onView(withId(R.id.switchModeItem)).check(matches(isNotChecked()));
        onView(withId(R.id.listViewGivePoints)).check(matches(hasDescendant(withHint("Nombre de usuario"))));
    }

    /**
     * ItemEvents have spinner with the events
     */
    @Test
    public void listEventsSpinner() {
        onView(withId(R.id.switchModeItem)).check(matches(isNotChecked()));
        onView(withId(R.id.listViewGivePoints)).check(matches(hasDescendant(withId(R.id.spinnerEvents))));
    }

    /**
     * ItemPoints have EditText username
     */
    @Test
    public void listItemPointsHasUserNameEditText() {
        onView(withId(R.id.switchModeItem)).perform(click());
        onView(withId(R.id.listViewGivePoints)).check(matches(hasDescendant(withId(R.id.userNameToGiveByPoints))));
    }

    /**
     * ItemPoints have EditText username with hint Nombre de usuario
     */
    @Test
    public void listItemPointsHasUserNameEditTextWithHint() {
        onView(withId(R.id.switchModeItem)).perform(click());
        onView(withId(R.id.listViewGivePoints)).check(matches(hasDescendant(withHint("Nombre de usuario"))));
    }

    /**
     * ItemPoints have EditText points
     */
    @Test
    public void listItemPointsHasPointsEditText() {
        onView(withId(R.id.switchModeItem)).perform(click());
        onView(withId(R.id.listViewGivePoints)).check(matches(hasDescendant(withId(R.id.pointsToGive))));
    }

    /**
     * ItemPoints have EditText points with hint Puntos
     */
    @Test
    public void listItemPointsHasPointsEditTextWithHint() {
        onView(withId(R.id.switchModeItem)).perform(click());
        onView(withId(R.id.listViewGivePoints)).check(matches(hasDescendant(withHint("Puntos"))));
    }

    /**
     * Another User button displays another user
     */
    @Test
    public void anotherUserDisplaysAnotherItem() {
        onView(withId(R.id.action_add_user)).perform(click());
        onView(withText("Usuario nº2")).check(matches(isDisplayed()));
    }
}
