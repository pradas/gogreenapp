package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
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
import static pes.gogreenapp.Utils.EspressoTestsMatchers.withDrawable;

/**
 * @author Adrian
 */

public class EventDetailedTests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Before the tests the Navigation Drawer is open, to enter to the profile of the user if cant
     * be open it is because there isn't a valid user logged. Due this, the setup do the Login
     * with the username user and the password Password12
     */
    @Before
    public void setup() {

        try {
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.list_events_fragment));
            onView(withId(R.id.rv_events)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.username_edit_text)).perform(clearText(), typeText("user"));
            onView(withId(R.id.password_user_text)).perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin)).perform(click());
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.list_events_fragment));
            onView(withId(R.id.rv_events)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }
    }

    /**
     * Check if clicking in a card_view displays the image
     */
    @Test
    public void eventDetailedHasImage() {
        onView(withId(R.id.imageEventDetailed)).check(matches(isDisplayed()));
    }
    /**
     * Check if clicking in a card_view displays the title
     */
    @Test
    public void eventDetailedHasTitle() {
        onView(withId(R.id.titleEventDetailed)).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in a card_view displays the description
     */
    @Test
    public void eventDetailedHasDescription() {
        onView(withId(R.id.descriptionEventDetailed)).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in a card_view displays the direction
     */
    @Test
    public void eventDetailedHasDirection() {
        onView(withId(R.id.directionEventDetailed)).check(matches(isDisplayed()));
    }



    /**
     * Check if clicking in a card_view displays the date
     */
    @Test
    public void eventDetailedHasDateValid() {
        onView(withId(R.id.dateEventDetailed)).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in a card_view displays the date
     */
    @Test
    public void eventDetailedHasHour() {
        onView(withId(R.id.hourEventDetailed)).check(matches(isDisplayed()));
    }

}
