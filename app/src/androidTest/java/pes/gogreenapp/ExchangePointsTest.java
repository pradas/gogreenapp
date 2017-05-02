package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

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

/**
 * Created by Adrian on 27/04/2017.
 */

public class ExchangePointsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Enter in rewards_list
     */
    @Before
    public void setup() {
        try {
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.nvView))
                    .perform(NavigationViewActions.navigateTo(R.id.rewards_list_fragment));
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.username_edit_text))
                    .perform(clearText(), typeText("user"));
            onView(withId(R.id.password_user_text))
                    .perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin))
                    .perform(click());
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.nvView))
                    .perform(NavigationViewActions.navigateTo(R.id.rewards_list_fragment));
        }
    }

    @Test
    public void exchangeButtonDisplaysAlertDialog() {
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,MyViewAction.clickChildViewWithId(R.id.exchangeButton)));
        onView(withText("¿Está seguro de que desea canjear esta promoción?")).check(matches(isDisplayed()));
        onView(withText("CANJEAR")).check(matches(isDisplayed()));
        onView(withText("CANCELAR")).check(matches(isDisplayed()));
    }

    /*
    @Test
    public void actionExchangeAlertDialog() {
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.actionDetailReward)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.rewards_list)).check(matches(isDisplayed()));
    }*/

    @Test
    public void actionCancelAlertDialog() {
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.actionDetailReward)).perform(click());
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.rewardDetailedFragment)).check(matches(isDisplayed()));
    }
}
