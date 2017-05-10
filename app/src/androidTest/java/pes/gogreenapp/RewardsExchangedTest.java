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
     * Before the tests the Navigation Drawer is open, if cant be open it is because there isn't
     * a valid user logged. Due this, the setup do the Login with
     * the username user and the password Password12
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
     * Check if the Navigation Drawer is open.
     */
    @Test
    public void checkRewardsExchangedsIsDisplayed() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rewardsExchangedFragment)).check(matches(isDisplayed()));
    }

    /**
     * Check if the Navigation Drawer is open.
     */
    @Test
    public void CardViewHasTitle() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withRecyclerView(R.id.rvExchanged).atPosition(0))
                .check(matches(hasDescendant(withId(R.id.rewardTitle))));
    }

    @Test
    public void CardViewHasPoints() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withRecyclerView(R.id.rvExchanged).atPosition(0))
                .check(matches(hasDescendant(withId(R.id.rewardPoints))));
    }

    @Test
    public void CardViewHasCategory() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withRecyclerView(R.id.rvExchanged).atPosition(0))
                .check(matches(hasDescendant(withId(R.id.rewardCategory))));

    }

    @Test
    public void CardViewHasEndDate() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withRecyclerView(R.id.rvExchanged).atPosition(0))
                .check(matches(hasDescendant(withId(R.id.rewardEndDate))));
    }

    @Test
    public void CardViewHasFavoriteButton() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withRecyclerView(R.id.rvExchanged).atPosition(0))
                .check(matches(hasDescendant(withId(R.id.favoriteButton))));
    }

    @Test
    public void CardViewHasExchangeButton() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withRecyclerView(R.id.rvExchanged).atPosition(0))
                .check(matches(hasDescendant(withId(R.id.exchangeButton))));
    }

    /**
     * Check if the Navigation Drawer is open.
     */
    @Test
    public void openRewardDetailed() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rvExchanged)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rewardDetailedFragment)).check(matches(isDisplayed()));
    }

    /**
     * Check if the Navigation Drawer is open.
     */
    @Test
    public void RewardDetailedHasUseButton() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rvExchanged)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("UTILIZAR")).check(matches(isDisplayed()));
    }

    /**
     * Check if the Navigation Drawer is open.
     */
    @Test
    public void UseButtonDisplaysQRCode() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rvExchanged))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.exchangeButton)));
        onView(withId(R.id.qrCode)).check(matches(isDisplayed()));
    }

    /**
     * Check if the Navigation Drawer is open.
     */
    @Test
    public void UseButtonDisplaysQRCodeInRewardDetaied() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rvExchanged)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.actionDetailReward)).perform(click());
        onView(withId(R.id.qrCode)).check(matches(isDisplayed()));
    }

    /**
     * Check if the Navigation Drawer is open.
     */
    @Test
    public void FavButtonChangeToFavoriteFilled() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rvExchanged))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.favoriteButton)));
        onView(withRecyclerView(R.id.rvExchanged).atPositionOnView(0, R.id.favoriteButton))
                .check(matches(withDrawable(R.mipmap.favoritefilled)));
    }

    /**
     * Check if the Navigation Drawer is open.
     */
    @Test
    public void FavButtonChangeToFavoriteEmpty() {

        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rvExchanged))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.favoriteButton)));
        onView(withId(R.id.rvExchanged))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.favoriteButton)));
        onView(withRecyclerView(R.id.rvExchanged).atPositionOnView(0, R.id.favoriteButton))
                .check(matches(withDrawable(R.mipmap.favorite)));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {

        return new RecyclerViewMatcher(recyclerViewId);
    }
}
