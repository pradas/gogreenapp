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
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static pes.gogreenapp.Utils.EspressoTestsMatchers.clickChildViewWithId;
import static pes.gogreenapp.Utils.EspressoTestsMatchers.withDrawable;

/**
 * @author Adrian
 */

public class ListRewardTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Before the tests the Navigation Drawer is open, to enter to the rewards_list if cant be open
     * it is because there isn't a valid user logged. Due this, the setup do the Login with
     * the username user and the password Password12
     */
    @Before
    public void setup() {

        try {
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.rewards_list_fragment));
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.username_edit_text)).perform(clearText(), typeText("user"));
            onView(withId(R.id.password_user_text)).perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin)).perform(click());
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.nvView)).perform(NavigationViewActions.navigateTo(R.id.rewards_list_fragment));
        }
    }

    /**
     * Check if the rewards_list is displayed
     */
    @Test
    public void showsAllRewards() {
        onView(withId(R.id.rewards_list)).check(matches(isDisplayed()));
    }

    /**
     * Check if the card_view of the rewards_list have title
     */
    @Test
    public void rewardHasCorrectAttributes() {
        onView(withId(R.id.rv)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
    }

    /**
     * Check if the card_view of the rewards_list have points
     */
    @Test
    public void rewardHasPoints() {

        onView(withId(R.id.rv)).check(matches(hasDescendant(withId(R.id.rewardPoints))));
    }

    /**
     * Check if the card_view of the rewards_list have category
     */
    @Test
    public void rewardHasCategory() {

        onView(withId(R.id.rv)).check(matches(hasDescendant(withId(R.id.rewardCategory))));
    }

    /**
     * Check if the card_view of the rewards_list have end_date
     */
    @Test
    public void rewardHasEndDate() {

        onView(withId(R.id.rv)).check(matches(hasDescendant(withId(R.id.rewardEndDate))));
    }

    /**
     * Check if the card_view of the rewards_list have favorite button
     */
    @Test
    public void rewardHasFavoriteButton() {

        onView(withId(R.id.rv)).check(matches(hasDescendant(withId(R.id.favoriteButton))));
    }

    /**
     * Check if the card_view of the rewards_list have exchange button
     */
    @Test
    public void rewardHasExchangeButton() {

        onView(withId(R.id.rv)).check(matches(hasDescendant(withId(R.id.exchangeButton))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void eachRewardInACardView() {

        onView(withId(R.id.rv)).check(matches(hasDescendant(withId(R.id.card_view))));
    }

    /**
     * Check if clicking in a card_view displays RewardDetailedFragment
     */
    @Test
    public void clickInRewardGoesToRewardDetail() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rewardDetailedFragment)).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in a card_view favorite button displays the favorite button filled
     */
    @Test
    public void FavButtonChangeToFavoriteFilled() {

        onView(withId(R.id.rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.favoriteButton)));
        onView(withRecyclerView(R.id.rv).atPositionOnView(0, R.id.favoriteButton))
                .check(matches(withDrawable(R.drawable.ic_fav_filled)));
    }

    /**
     * Check if clicking in a card_view favorite button two times displays the favorite button
     */
    @Test
    public void FavButtonChangeToFavoriteEmpty() {

        onView(withId(R.id.rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.favoriteButton)));
        onView(withRecyclerView(R.id.rv).atPositionOnView(0, R.id.favoriteButton))
                .check(matches(withDrawable(R.drawable.ic_fav_void)));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {

        return new RecyclerViewMatcher(recyclerViewId);
    }
}
