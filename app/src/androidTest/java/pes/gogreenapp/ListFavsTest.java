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
import static pes.gogreenapp.ListRewardTest.withRecyclerView;
import static pes.gogreenapp.Utils.EspressoTestsMatchers.clickChildViewWithId;
import static pes.gogreenapp.Utils.EspressoTestsMatchers.withDrawable;

/**
 * Created by Adrian on 05/06/2017.
 */

public class ListFavsTest {

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
                    .perform(NavigationViewActions.navigateTo(R.id.favs_list_fragment));
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
                    .perform(NavigationViewActions.navigateTo(R.id.favs_list_fragment));
        }
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasTabs() {
        onView(withId(R.id.tabLayoutFavsList)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasRewardsByDefault() {
        onView(withId(R.id.rvRewardsFavList)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasRewards() {
        onView(withText("REWARDS")).perform(click());
        onView(withId(R.id.rvRewardsFavList)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasEvents() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasDeals() {
        onView(withText("OFERTAS")).perform(click());
        onView(withId(R.id.rvDealsFavList)).check(matches(isDisplayed()));
    }


    @Test
    public void FavButtonChangeToFavoriteEmptyInRewardsTab() {
        onView(withId(R.id.rvRewardsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.favoriteButton)));
        onView(withRecyclerView(R.id.rvRewardsFavList).atPositionOnView(0, R.id.favoriteButton))
                .check(matches(withDrawable(R.drawable.ic_fav_void)));
        onView(withId(R.id.rvRewardsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.favoriteButton)));
    }


    @Test
    public void FavButtonChangeToFavoriteFilledInRewardsTab() {
        onView(withId(R.id.rvRewardsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.favoriteButton)));
        onView(withId(R.id.rvRewardsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.favoriteButton)));
        onView(withRecyclerView(R.id.rvRewardsFavList).atPositionOnView(0, R.id.favoriteButton))
                .check(matches(withDrawable(R.drawable.ic_fav_filled)));
    }


    @Test
    public void FavButtonChangeToFavoriteEmptyInEventsTab() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.eventFavoriteButton)));
        onView(withRecyclerView(R.id.rvEventsFavList).atPositionOnView(0, R.id.eventFavoriteButton))
                .check(matches(withDrawable(R.drawable.ic_fav_void)));
        onView(withId(R.id.rvEventsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.eventFavoriteButton)));
    }


    @Test
    public void FavButtonChangeToFavoriteFilledInEventsTab() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.eventFavoriteButton)));
        onView(withId(R.id.rvEventsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.eventFavoriteButton)));
        onView(withRecyclerView(R.id.rvEventsFavList).atPositionOnView(0, R.id.eventFavoriteButton))
                .check(matches(withDrawable(R.drawable.ic_fav_filled)));
    }


    @Test
    public void FavButtonChangeToFavoriteEmptyInDealsTab() {
        onView(withText("OFERTAS")).perform(click());
        onView(withId(R.id.rvDealsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.ofertaFavoriteButton)));
        onView(withRecyclerView(R.id.rvDealsFavList).atPositionOnView(0, R.id.ofertaFavoriteButton))
                .check(matches(withDrawable(R.drawable.ic_fav_void)));
        onView(withId(R.id.rvDealsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.ofertaFavoriteButton)));
    }


    @Test
    public void FavButtonChangeToFavoriteFilledInDealsTab() {
        onView(withText("OFERTAS")).perform(click());
        onView(withId(R.id.rvDealsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.ofertaFavoriteButton)));
        onView(withId(R.id.rvDealsFavList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.ofertaFavoriteButton)));
        onView(withRecyclerView(R.id.rvDealsFavList).atPositionOnView(0, R.id.ofertaFavoriteButton))
                .check(matches(withDrawable(R.drawable.ic_fav_filled)));
    }

    /**
     * Check if clicking in a card_view favorite button two times displays the favorite button
     */
    @Test
    public void clickOnRewardDisplayRewardDetailed() {
        onView(withId(R.id.rvRewardsFavList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rewardDetailedFragment)).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in a card_view favorite button two times displays the favorite button
     */
    @Test
    public void clickOnEventDisplayEventDetailed() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.eventDetailedFragment)).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in a card_view favorite button two times displays the favorite button
     */
    @Test
    public void clickOnDealDisplayDealDetailed() {
        onView(withText("OFERTAS")).perform(click());
        onView(withId(R.id.rvDealsFavList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ofertaDetailedFragment)).check(matches(isDisplayed()));
    }

    /**
     * Check if the card_view of the rewards_list have title
     */
    @Test
    public void rewardHasImage() {
        onView(withId(R.id.rvRewardsFavList)).check(matches(hasDescendant(withId(R.id.rewardBackgroundImage))));
    }

    /**
     * Check if the card_view of the rewards_list have title
     */
    @Test
    public void rewardHasTitle() {
        onView(withId(R.id.rvRewardsFavList)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
    }

    /**
     * Check if the card_view of the rewards_list have points
     */
    @Test
    public void rewardHasPoints() {
        onView(withId(R.id.rvRewardsFavList)).check(matches(hasDescendant(withId(R.id.rewardPoints))));
    }

    /**
     * Check if the card_view of the rewards_list have category
     */
    @Test
    public void rewardHasCategory() {
        onView(withId(R.id.rvRewardsFavList)).check(matches(hasDescendant(withId(R.id.rewardCategory))));
    }

    /**
     * Check if the card_view of the rewards_list have end_date
     */
    @Test
    public void rewardHasEndDate() {
        onView(withId(R.id.rvRewardsFavList)).check(matches(hasDescendant(withId(R.id.rewardEndDate))));
    }

    /**
     * Check if the card_view of the rewards_list have favorite button
     */
    @Test
    public void rewardHasFavoriteButton() {
        onView(withId(R.id.rvRewardsFavList)).check(matches(hasDescendant(withId(R.id.favoriteButton))));
    }

    /**
     * Check if the card_view of the rewards_list have exchange button
     */
    @Test
    public void rewardHasExchangeButton() {
        onView(withId(R.id.rvRewardsFavList)).check(matches(hasDescendant(withId(R.id.exchangeButton))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void eventHasImage() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList)).check(matches(hasDescendant(withId(R.id.eventImage))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void eventHasTitle() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList)).check(matches(hasDescendant(withId(R.id.eventTitle))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void eventHasPoints() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList)).check(matches(hasDescendant(withId(R.id.eventPoints))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void eventHasCategory() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList)).check(matches(hasDescendant(withId(R.id.eventCategory))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void eventHasEndDate() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList)).check(matches(hasDescendant(withId(R.id.eventEndDate))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void eventHasHour() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList)).check(matches(hasDescendant(withId(R.id.eventHour))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void eventHasFavoriteButton() {
        onView(withText("EVENTOS")).perform(click());
        onView(withId(R.id.rvEventsFavList)).check(matches(hasDescendant(withId(R.id.eventFavoriteButton))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void dealHasImage() {
        onView(withText("OFERTAS")).perform(click());
        onView(withId(R.id.rvDealsFavList)).check(matches(hasDescendant(withId(R.id.ofertaImage))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void dealHasTitle() {
        onView(withText("OFERTAS")).perform(click());
        onView(withId(R.id.rvDealsFavList)).check(matches(hasDescendant(withId(R.id.ofertaTitle))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void dealHasPoints() {
        onView(withText("OFERTAS")).perform(click());
        onView(withId(R.id.rvDealsFavList)).check(matches(hasDescendant(withId(R.id.ofertaPoints))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void dealHasEndDate() {
        onView(withText("OFERTAS")).perform(click());
        onView(withId(R.id.rvDealsFavList)).check(matches(hasDescendant(withId(R.id.ofertaEndDate))));
    }

    /**
     * Check if the rewards_list have a card view
     */
    @Test
    public void dealHasFavorite() {
        onView(withText("OFERTAS")).perform(click());
        onView(withId(R.id.rvDealsFavList)).check(matches(hasDescendant(withId(R.id.ofertaFavoriteButton))));
    }

}
