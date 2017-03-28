package pes.gogreenapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pes.gogreenapp.AuxiliarTestMethods.EspressoTestsMatchers.withDrawable;



import org.junit.Before;
import org.junit.Rule;


import android.support.test.rule.ActivityTestRule;


import pes.gogreenapp.Activities.RewardsList;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestListadoRewards {
    @Before
    public void startTest(){

    }

    @Rule
    public ActivityTestRule<RewardsList> mActivityRule = new ActivityTestRule<>(
            RewardsList.class);

    @Test
    public void showAllRewards() {
        onView(withId(R.id.rv)).perform(click());   //Para que pueda comprobar, si no, no lo comprueba

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("reward 2"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("200"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("Transporte"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("11/07/2008"))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("reward 2"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("200"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("Transporte"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("11/07/2008"))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("reward 2"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("200"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("Transporte"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("11/07/2008"))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("Reward 1"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("100"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("Ocio"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("01/01/2018"))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("Reward 1"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("100"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("Ocio"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("01/01/2018"))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("reward 3"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("300"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("Transporte"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("01/12/2018"))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("reward 3"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("300"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("Transporte"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("01/12/2018"))));
    }

    @Test
    public void hasAllComponents () {
        onView(withId(R.id.rv)).perform(click()); //Para que pueda comprobar, si no, no lo comprueba

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withId(R.id.rewardPoints))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withId(R.id.rewardCategory))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withId(R.id.rewardEndDate))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withId(R.id.favoriteButton))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withId(R.id.exchangeButton))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withId(R.id.rewardPoints))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withId(R.id.rewardCategory))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withId(R.id.rewardEndDate))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withId(R.id.favoriteButton))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withId(R.id.exchangeButton))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withId(R.id.rewardPoints))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withId(R.id.rewardCategory))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withId(R.id.rewardEndDate))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withId(R.id.favoriteButton))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withId(R.id.exchangeButton))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withId(R.id.rewardPoints))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withId(R.id.rewardCategory))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withId(R.id.rewardEndDate))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withId(R.id.favoriteButton))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withId(R.id.exchangeButton))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withId(R.id.rewardPoints))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withId(R.id.rewardCategory))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withId(R.id.rewardEndDate))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withId(R.id.favoriteButton))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withId(R.id.exchangeButton))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withId(R.id.rewardPoints))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withId(R.id.rewardCategory))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withId(R.id.rewardEndDate))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withId(R.id.favoriteButton))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withId(R.id.exchangeButton))));

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withId(R.id.rewardPoints))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withId(R.id.rewardCategory))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withId(R.id.rewardEndDate))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withId(R.id.favoriteButton))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withId(R.id.exchangeButton))));
    }

    @Test
    public void rewardsIsolateds () {
        onView(withId(R.id.rv)).perform(click()); //Para que pueda comprobar, si no, no lo comprueba

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withId(R.id.card_view))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withId(R.id.card_view))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withId(R.id.card_view))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withId(R.id.card_view))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withId(R.id.card_view))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withId(R.id.card_view))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withId(R.id.card_view))));
    }

    @Test
    public void rewardsSortedByDefault () {
        onView(withId(R.id.rv)).perform(click()); //Para que pueda comprobar, si no, no lo comprueba

        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("01/01/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("01/01/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("01/12/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withText("01/12/2018"))));
    }

    @Test
    public void changeFavoriteButtonOnPress () {
        onView(withId(R.id.rv)).perform(click()); //Para que pueda comprobar, si no, no lo comprueba

        onView(withId(R.id.rv)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, AuxiliarTestMethods.MyViewAction.clickChildViewWithId(R.id.favoriteButton)))
                .check(matches(withDrawable(R.mipmap.favorite)));
    }
}