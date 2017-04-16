package pes.gogreenapp;


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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import static pes.gogreenapp.R.id.rewardPoints;


import org.junit.Before;
import org.junit.Rule;


import android.support.test.rule.ActivityTestRule;

import pes.gogreenapp.Activities.ListActivity;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestFiltroRewards {
    @Before
    public void startTest(){

    }

    @Rule
    public ActivityTestRule<ListActivity> mActivityRule = new ActivityTestRule<>(
            ListActivity.class);

    @Test
    public void PuntosDesc() {

        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("PUNTOS ↓")));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("300"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("300"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withText("200"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("200"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("200"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("100"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withText("100"))));
    }
    @Test
    public void PuntosAsc() {

        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("PUNTOS ↓")));
        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("PUNTOS ↑")));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withText("300"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withText("300"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("200"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("200"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withText("200"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("100"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("100"))));
    }
    @Test
    public void NoOrdenarPuntos() {

        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("PUNTOS ↓")));
        onView(withId(R.id.orderPointsButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("PUNTOS ↑")));
        onView(withId(R.id.orderDateButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.orderPointsButton))
                .check(matches(withText("PUNTOS")));
    }
    @Test
    public void FechaAsc() {

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
    public void FechaDesc() {

        onView(withId(R.id.orderDateButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("FECHA ↑")));
        onView(withId(R.id.orderDateButton))
                .perform(scrollTo())
                .perform(click())
                .check(matches(withText("FECHA ↓")));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("11/07/2008"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("01/01/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withText("01/01/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("01/12/2018"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("01/12/2018"))));
    }
    @Test
    public void NoOrdenarFecha() {

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

    @Test
    public void DialogCategorias(){
        onView(withId(R.id.showCategoriesButton))
                .perform(scrollTo())
                .perform(click());
        onView(withText("SELECCIONA UNA CATEGORIA"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void SeleccionarCategoriaOcio(){
        onView(withId(R.id.showCategoriesButton))
                .perform(scrollTo())
                .perform(click());
        onView(withText("Ocio"))
                .perform(click());
        onView(withText("SELECCIONAR CATEGORIA"))
                .perform(click());
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("Ocio"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("Ocio"))));
    }
    @Test
    public void SeleccionarCategoriaTransporte(){
        onView(withId(R.id.showCategoriesButton))
                .perform(scrollTo())
                .perform(click());
        onView(withText("Transporte"))
                .perform(click());
        onView(withText("SELECCIONAR CATEGORIA"))
                .perform(click());
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withText("Transporte"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withText("Transporte"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withText("Transporte"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText("Transporte"))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withText("Transporte"))));
    }
    @Test
    public void VerTodos(){
        onView(withId(R.id.showAllButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(0)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(1)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(2)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(5)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.scrollToPosition(6)).check(matches(hasDescendant(withId(R.id.rewardTitle))));
    }
}