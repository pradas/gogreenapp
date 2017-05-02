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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pes.gogreenapp.EspressoTestsMatchers.withDrawable;

public class ListRewardTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Before the tests the Navigation Drawer is open, if cant be open it is because there isn't
     * a valid user logged. Due this, the setup do the Login with
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

    @Test
    public void showsAllRewards() {
        onView(withId(R.id.rewards_list))
                .check(matches(isDisplayed()));
    }

    @Test
    public void rewardHasCorrectAttributes() {
        onView(withId(R.id.rv))
                .check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv))
                .check(matches(hasDescendant(withId(R.id.rewardPoints))));
        onView(withId(R.id.rv))
                .check(matches(hasDescendant(withId(R.id.rewardCategory))));
        onView(withId(R.id.rv))
                .check(matches(hasDescendant(withId(R.id.rewardEndDate))));
        onView(withId(R.id.rv))
                .check(matches(hasDescendant(withId(R.id.favoriteButton))));
        onView(withId(R.id.rv))
                .check(matches(hasDescendant(withId(R.id.exchangeButton))));
    }

    @Test
    public void eachRewardInACardView() {
        onView(withId(R.id.rv))
                .check(matches(hasDescendant(withId(R.id.card_view))));
    }

    // TODO fix test
    @Test
    public void orderedByDate() {
        onView(withRecyclerView(R.id.rv).atPosition(0))
                .check(matches(hasDescendant(withText("07/07/2017"))));
        onView(withRecyclerView(R.id.rv).atPosition(1))
                .check(matches(hasDescendant(withText("13/07/2017"))));
    }

    @Test
    public void clickInRewardGoesToRewardDetail() {
        onView(withId(R.id.rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rewardDetailedFragment)).check(matches(isDisplayed()));
    }

    // TODO fix test
    @Test
    public void FavButtonChangeToFavoriteFilled() {
        onView(withId(R.id.rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.favoriteButton))
        ).check(matches(withDrawable(R.mipmap.favoritefilled)));
        onView(withId(R.id.favoriteButton)).check(matches(withDrawable(R.mipmap.favoritefilled)));
    }

    // TODO fix test
    @Test
    public void FavButtonChangeToFavoriteEmpty() {
        onView(withId(R.id.rv)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.favoriteButton)));
        onView(withId(R.id.rv)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.favoriteButton)));
        onView(withId(R.id.favoriteButton)).check(matches(withDrawable(R.mipmap.favorite)));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
