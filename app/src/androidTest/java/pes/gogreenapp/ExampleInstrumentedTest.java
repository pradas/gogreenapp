package pes.gogreenapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import static pes.gogreenapp.R.id.rewardPoints;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import pes.gogreenapp.Activities.RewardsList;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {
    @Before
    public void startTest(){

    }

    @Rule
    public ActivityTestRule<RewardsList> mActivityRule = new ActivityTestRule<>(
            RewardsList.class);

    @Test
    public void PuntosAsc() {

        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("PUNTOS ↑")));
    }
    @Test
    public void PuntosDesc() {

        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("PUNTOS ↑")));
        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("PUNTOS ↓")));
    }
    @Test
    public void NoPuntos() {

        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("PUNTOS ↑")));
        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("PUNTOS ↓")));
        onView(withId(R.id.orderDateButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.orderPointsButton))
                .check(matches(withText("PUNTOS")));
    }
    @Test
    public void DateAsc() {

        onView(withId(R.id.orderDateButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("FECHA ↑")));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("01/01/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("01/01/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("01/12/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withText("01/12/2018"))));
    }
    @Test
    public void DateDesc() {

        onView(withId(R.id.orderDateButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("FECHA ↑")));
        onView(withId(R.id.orderDateButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("FECHA ↓")));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("01/01/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withText("01/01/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("01/12/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("01/12/2018"))));
    }
    @Test
    public void NoDate() {

        onView(withId(R.id.orderDateButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("FECHA ↑")));
        onView(withId(R.id.orderDateButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("FECHA ↓")));
        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.orderDateButton))
                .check(matches(withText("FECHA")));
    }
}