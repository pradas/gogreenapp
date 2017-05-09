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
     * Before the tests the Navigation Drawer is open, to enter to the rewards_list if cant be open
     * it is because there isn't a valid user logged. Due this, the setup do the Login with
     * the username user and the password Password12
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

    /**
     * Check if clicking in the exchange button shows an Alert Dialog
     */
    @Test
    public void exchangeButtonDisplaysAlertDialogWithTitle() {
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.exchangeButton)));
        onView(withText("¿Está seguro de que desea canjear esta promoción?")).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in the exchange button shows a button "Canjear"
     */
    @Test
    public void exchangeButtonDisplaysAlertDialogWithButtonExchange() {
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.exchangeButton)));
        onView(withText("CANJEAR")).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in the exchange button shows a button "Cancelar"
     */
    @Test
    public void exchangeButtonDisplaysAlertDialogWithButtonCancel() {
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.exchangeButton)));
        onView(withText("CANCELAR")).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in the exchange button of the Alert Dialog, the rewards_list is displayed
     */
    //TODO fix test
    @Test
    public void actionExchangeAlertDialog() {
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.actionDetailReward)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.rewards_list)).check(matches(isDisplayed()));

        //No funciona porque coge una reward que no tiene suficientes puntos para canjear.
    }

    /**
     * Check if clicking in the cancel button of the Alert Dialog, the RewardsDetailed is displayed
     */
    @Test
    public void actionCancelAlertDialog() {
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.actionDetailReward)).perform(click());
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.rewardDetailedFragment)).check(matches(isDisplayed()));
    }
}
