package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pes.gogreenapp.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static pes.gogreenapp.Utils.EspressoTestsMatchers.withError;

public class CreateEventTest {
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
                    .perform(NavigationViewActions.navigateTo(R.id.create_event_fragment));
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
                    .perform(NavigationViewActions.navigateTo(R.id.create_event_fragment));
        }
    }


    /**
     * Check error if the hour is incorrect
     */
    @Test
    public void IncorrectHour() {
        onView(withId(R.id.titleCreateEvent_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.DescriptionCreateEvent_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.PointsCreateEvent_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.DirectionCreateEvent_edit_text))
                .perform(clearText(), typeText("C/ Mayor 1"));
        onView(withId(R.id.CompanyCreateEvent_edit_text))
                .perform(clearText(), typeText("Green Peace"));
        onView(withId(R.id.editTextDateCreateEvent))
                .perform(clearText(), typeText("22-09-2017"));
        onView(withId(R.id.HourCreateEvent_edit_text))
                .perform(clearText(), typeText("25"));
        onView(withId(R.id.MinCreateEvent_edit_text))
                .perform(clearText(), typeText("30"));
        onView(withId(R.id.ScrollViewCreateEvent)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateEvent))
                .perform(scrollTo(), click());
        onView(withId(R.id.HourCreateEvent_edit_text))
                .check(matches(withError("Hora incorrecta")));
    }

    /**
     * Check error if the minuts are incorrect
     */
    @Test
    public void IncorrectMin() {
        onView(withId(R.id.titleCreateEvent_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.DescriptionCreateEvent_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.PointsCreateEvent_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.DirectionCreateEvent_edit_text))
                .perform(clearText(), typeText("C/ Mayor 1"));
        onView(withId(R.id.CompanyCreateEvent_edit_text))
                .perform(clearText(), typeText("Green Peace"));
        onView(withId(R.id.editTextDateCreateEvent))
                .perform(clearText(), typeText("22-09-2017"));
        onView(withId(R.id.HourCreateEvent_edit_text))
                .perform(clearText(), typeText("22"));
        onView(withId(R.id.MinCreateEvent_edit_text))
                .perform(clearText(), typeText("70"));
        onView(withId(R.id.ScrollViewCreateEvent)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateEvent))
                .perform(scrollTo(), click());
        onView(withId(R.id.MinCreateEvent_edit_text))
                .check(matches(withError("Minutos incorrectos")));
    }
    /**
     * Check error if the minuts are put but is not put the hour
     */
    @Test
    public void MinButNotHour() {
        onView(withId(R.id.titleCreateEvent_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.DescriptionCreateEvent_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.PointsCreateEvent_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.DirectionCreateEvent_edit_text))
                .perform(clearText(), typeText("C/ Mayor 1"));
        onView(withId(R.id.CompanyCreateEvent_edit_text))
                .perform(clearText(), typeText("Green Peace"));
        onView(withId(R.id.editTextDateCreateEvent))
                .perform(clearText(), typeText("22-09-2017"));
        onView(withId(R.id.MinCreateEvent_edit_text))
                .perform(clearText(), typeText("30"));
        onView(withId(R.id.ScrollViewCreateEvent)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateEvent))
                .perform(scrollTo(), click());
        onView(withId(R.id.HourCreateEvent_edit_text))
                .check(matches(withError("Hora necesaria")));
    }

    /**
     * Check error if the hour is put but is not put the minuts
     */
    @Test
    public void HourButNotMin() {
        onView(withId(R.id.titleCreateEvent_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.DescriptionCreateEvent_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.PointsCreateEvent_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.DirectionCreateEvent_edit_text))
                .perform(clearText(), typeText("C/ Mayor 1"));
        onView(withId(R.id.CompanyCreateEvent_edit_text))
                .perform(clearText(), typeText("Green Peace"));
        onView(withId(R.id.editTextDateCreateEvent))
                .perform(clearText(), typeText("22-09-2017"));
        onView(withId(R.id.HourCreateEvent_edit_text))
                .perform(clearText(), typeText("22"));
        onView(withId(R.id.ScrollViewCreateEvent)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateEvent))
                .perform(scrollTo(), click());
        onView(withId(R.id.MinCreateEvent_edit_text))
                .check(matches(withError("Minutos necesarios")));
    }

    /**
     * Check error if the date is not good formated
     */
    @Test
    public void IncorrectDate() {
        onView(withId(R.id.titleCreateEvent_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.DescriptionCreateEvent_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.PointsCreateEvent_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.DirectionCreateEvent_edit_text))
                .perform(clearText(), typeText("C/ Mayor 1"));
        onView(withId(R.id.CompanyCreateEvent_edit_text))
                .perform(clearText(), typeText("Green Peace"));
        onView(withId(R.id.editTextDateCreateEvent))
                .perform(clearText(), typeText("22-19-2017"));
        onView(withId(R.id.HourCreateEvent_edit_text))
                .perform(clearText(), typeText("22"));
        onView(withId(R.id.MinCreateEvent_edit_text))
                .perform(clearText(), typeText("30"));
        onView(withId(R.id.ScrollViewCreateEvent)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateEvent))
                .perform(scrollTo(), click());
        onView(withId(R.id.editTextDateCreateEvent))
                .check(matches(withError("Fecha invalida (dd-mm-yyyy)")));
    }
    /**
     * Check error if the date is not put
     */
    @Test
    public void NoDate() {
        onView(withId(R.id.titleCreateEvent_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.DescriptionCreateEvent_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.PointsCreateEvent_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.DirectionCreateEvent_edit_text))
                .perform(clearText(), typeText("C/ Mayor 1"));
        onView(withId(R.id.CompanyCreateEvent_edit_text))
                .perform(clearText(), typeText("Green Peace"));
        onView(withId(R.id.HourCreateEvent_edit_text))
                .perform(clearText(), typeText("22"));
        onView(withId(R.id.MinCreateEvent_edit_text))
                .perform(clearText(), typeText("30"));
        onView(withId(R.id.ScrollViewCreateEvent)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateEvent))
                .perform(scrollTo(), click());
        onView(withId(R.id.editTextDateCreateEvent))
                .check(matches(withError("Fecha necesaria")));
    }
    /**
     * Check error if the points are not put
     */
    @Test
    public void NoPoints() {
        onView(withId(R.id.titleCreateEvent_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.DescriptionCreateEvent_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.DirectionCreateEvent_edit_text))
                .perform(clearText(), typeText("C/ Mayor 1"));
        onView(withId(R.id.CompanyCreateEvent_edit_text))
                .perform(clearText(), typeText("Green Peace"));
        onView(withId(R.id.editTextDateCreateEvent))
                .perform(clearText(), typeText("22-09-2017"));
        onView(withId(R.id.HourCreateEvent_edit_text))
                .perform(clearText(), typeText("22"));
        onView(withId(R.id.MinCreateEvent_edit_text))
                .perform(clearText(), typeText("30"));
        onView(withId(R.id.ScrollViewCreateEvent)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateEvent))
                .perform(scrollTo(), click());
        onView(withId(R.id.PointsCreateEvent_edit_text))
                .check(matches(withError("Puntos necesarios")));
    }

    /**
     * Check error if the description is not put
     */
    @Test
    public void NoDescription() {
        onView(withId(R.id.titleCreateEvent_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.PointsCreateEvent_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.DirectionCreateEvent_edit_text))
                .perform(clearText(), typeText("C/ Mayor 1"));
        onView(withId(R.id.CompanyCreateEvent_edit_text))
                .perform(clearText(), typeText("Green Peace"));
        onView(withId(R.id.editTextDateCreateEvent))
                .perform(clearText(), typeText("22-09-2017"));
        onView(withId(R.id.HourCreateEvent_edit_text))
                .perform(clearText(), typeText("22"));
        onView(withId(R.id.MinCreateEvent_edit_text))
                .perform(clearText(), typeText("30"));
        onView(withId(R.id.ScrollViewCreateEvent)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateEvent))
                .perform(scrollTo(), click());
        onView(withId(R.id.DescriptionCreateEvent_edit_text))
                .check(matches(withError("Descripción necesaria")));
    }

    /**
     * Check error if the title is not put
     */
    @Test
    public void NoTitle() {
        onView(withId(R.id.DescriptionCreateEvent_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.PointsCreateEvent_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.DirectionCreateEvent_edit_text))
                .perform(clearText(), typeText("C/ Mayor 1"));
        onView(withId(R.id.CompanyCreateEvent_edit_text))
                .perform(clearText(), typeText("Green Peace"));
        onView(withId(R.id.editTextDateCreateEvent))
                .perform(clearText(), typeText("22-09-2017"));
        onView(withId(R.id.HourCreateEvent_edit_text))
                .perform(clearText(), typeText("22"));
        onView(withId(R.id.MinCreateEvent_edit_text))
                .perform(clearText(), typeText("30"));
        onView(withId(R.id.ScrollViewCreateEvent)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateEvent))
                .perform(scrollTo(),click());
        onView(withId(R.id.titleCreateEvent_edit_text))
                .check(matches(withError("Título necesario")));
    }


    /**
     * Check if the the time is displayed
     */
    @Test
    public void CheckTimeIsDisplayed(){
        onView(withId(R.id.TimeTextCreateEvent))
                .check(matches(isDisplayed()));
        onView(withId(R.id.HourCreateEvent_edit_text))
                .check(matches(isDisplayed()));
        onView(withId(R.id.MinCreateEvent_edit_text))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the the date is displayed
     */
    @Test
    public void CheckDateIsDisplayed(){
        onView(withId(R.id.DateTextCreateEvent))
                .check(matches(isDisplayed()));
        onView(withId(R.id.editTextDateCreateEvent))
                .check(matches(isDisplayed()));
        onView(withId(R.id.DateCreateEvent))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the the company is displayed
     */
    @Test
    public void CheckCompanyIsDisplayed(){
        onView(withId(R.id.CompanyCreateEvent))
                .check(matches(isDisplayed()));
        onView(withId(R.id.CompanyCreateEvent_edit_text))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the the direction is displayed
     */
    @Test
    public void CheckDirectionIsDisplayed(){
        onView(withId(R.id.DirectionCreateEvent))
                .check(matches(isDisplayed()));
        onView(withId(R.id.DirectionCreateEvent_edit_text))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the the image button is displayed
     */
    @Test
    public void CheckImageIsDisplayed(){
        onView(withId(R.id.ScrollViewCreateEvent)).perform(swipeUp());
        onView(withId(R.id.ImageCreateEventButton))
                .check(matches(isDisplayed()));
    }
    /**
     * Check if the the title is displayed
     */
    @Test
    public void CheckPointsAreDisplayed(){
        onView(withId(R.id.PointsCreateEvent))
                .check(matches(isDisplayed()));
        onView(withId(R.id.PointsCreateEvent_edit_text))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the the description is displayed
     */
    @Test
    public void CheckDescriptionIsDisplayed(){
        onView(withId(R.id.DescriptionCreateEvent))
                .check(matches(isDisplayed()));
        onView(withId(R.id.DescriptionCreateEvent_edit_text))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the the title is displayed
     */
    @Test
    public void CheckTitleIsDisplayed(){
        onView(withId(R.id.TitleCreateEvent))
                .check(matches(isDisplayed()));
        onView(withId(R.id.titleCreateEvent_edit_text))
                .check(matches(isDisplayed()));
    }

    
}
