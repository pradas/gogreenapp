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

public class RewardDetailedTests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Navigate to rewards_list_fragment.
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

    @Test
    public void openRewardDetailedCorrectInRewardsList() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rewardDetailedFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void rewardDetailedHasTitle() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.titleDetailReward)).check(matches(isDisplayed()));
    }

    @Test
    public void rewardDetailedHasDescription() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.descriptionDetailReward)).check(matches(isDisplayed()));
    }

    @Test
    public void rewardDetailedHasDateValid() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.dateValidDetailReward)).check(matches(isDisplayed()));
    }

    @Test
    public void rewardDetailedHasConsultWeb() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.consultWebDetailReward)).check(matches(isDisplayed()));
    }

    @Test
    public void rewardDetailedHasAdverts() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.advertDetailReward)).check(matches(isDisplayed()));
    }

    @Test
    public void rewardDetailedHasInstructions() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.instructionsDetailReward)).check(matches(isDisplayed()));
    }

    @Test
    public void rewardDetailedHasFavoriteButton() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.favoriteDetailButton)).check(matches(isDisplayed()));
    }

    @Test
    public void rewardDetailedHasButtonAction() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.actionDetailReward)).check(matches(isDisplayed()));
    }

    @Test
    public void exchangeButtonDisplayAlertDialogWithMessage() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.actionDetailReward)).perform(click());
        onView(withText("¿Está seguro de que desea canjear esta promoción?")).check(matches(isDisplayed()));
    }

    @Test
    public void exchangeButtonDisplayAlertDialogWithButtonExchange() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.actionDetailReward)).perform(click());
        onView(withText("CANJEAR")).check(matches(isDisplayed()));
    }

    @Test
    public void exchangeButtonDisplayAlertDialogWithButtonCancel() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.actionDetailReward)).perform(click());
        onView(withText("CANCELAR")).check(matches(isDisplayed()));
    }

    @Test
    public void actionCancelAlertDialog() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.actionDetailReward)).perform(click());
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.rewardDetailedFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void actionFavButtonMatchFavoriteFilled() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.favoriteDetailButton)).perform(click());
        onView(withId(R.id.favoriteDetailButton)).check(matches(withDrawable(R.mipmap.favoritefilled)));
    }

    @Test
    public void actionFavButtonMatchFavoriteEmpty() {

        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.favoriteDetailButton)).perform(click());
        onView(withId(R.id.favoriteDetailButton)).perform(click());
        onView(withId(R.id.favoriteDetailButton)).check(matches(withDrawable(R.mipmap.favorite)));
    }
}
