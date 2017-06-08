package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
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
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pes.gogreenapp.Utils.EspressoTestsMatchers.clickChildViewWithId;
import static pes.gogreenapp.Utils.EspressoTestsMatchers.withDrawable;

public class RewardsExchangedTest {

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
            onView(withId(R.id.profile_image)).perform(click());
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.username_edit_text)).perform(clearText(), typeText("user"));
            onView(withId(R.id.password_user_text)).perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin)).perform(click());
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.profile_image)).perform(click());
        }
    }

    /**
     * Check if the rewards exchanged is displayed
     */
    @Test
    public void checkRewardsExchangedsIsDisplayed() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rewardsExchangedFragment)).check(matches(isDisplayed()));
    }

   /**
     * Check if clicking in a card_view and clicking in the exchange button and clicking on the
     * exchange button of the card_view of the recycler view of the rewards exchanged display the
     * Rewards Detailed fragment
     */
    @Test
    public void openRewardDetailed() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rvExchanged)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rewardDetailedFragment)).check(matches(isDisplayed()));
    }


    /**
     * Check if clicking in a card_view and clicking in the exchange button and clicking on the
     * exchange button of the card_view of the recycler view of the rewards exchanged display the
     * QR Code
     */
    @Test
    public void UseButtonDisplaysQRCode() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rvExchanged))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.exchangeButton)));
        onView(withId(R.id.qrCode)).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in a card_view and clicking in the exchange button and clicking on the
     * exchange button of the card_view of the recycler view of the rewards exchanged and clicking
     * in the use button display the QR Code
     */
    @Test
    public void UseButtonDisplaysQRCodeInRewardDetaied() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rvExchanged)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.actionDetailReward)).perform(click());
        onView(withId(R.id.qrCode)).check(matches(isDisplayed()));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {

        return new RecyclerViewMatcher(recyclerViewId);
    }
}
