package pes.gogreenapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import pes.gogreenapp.Activities.RewardsList;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {


    @Rule
    public ActivityTestRule<RewardsList> mActivityRule = new ActivityTestRule<>(
            RewardsList.class);

    @Test
    public void PuntosAsc() {

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
     /*   onView(withRecyclerView(R.id.rv)
                .atPositionOnView(1, R.id.ofElementYouWantToCheck))
                .check(matches(withText("Test text")));*/
    }
}