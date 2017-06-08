package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pes.gogreenapp.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static pes.gogreenapp.Utils.EspressoTestsMatchers.withError;

public class CreateDealTest {
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
                    .perform(NavigationViewActions.navigateTo(R.id.new_deal_fragment));
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.username_edit_text))
                    .perform(clearText(), typeText("manager"));
            onView(withId(R.id.password_user_text))
                    .perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin))
                    .perform(click());
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.nvView))
                    .perform(NavigationViewActions.navigateTo(R.id.new_deal_fragment));
        }
    }


    /**
     * Check error if the date is not good formated
     */
    @Test
    public void IncorrectDate() {
        onView(withId(R.id.titleCreateOferta_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.DescriptionCreateOferta_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.PointsCreateOferta_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.editTextDateCreateOferta))
                .perform(clearText(), typeText("22-19-2017"));
        onView(withId(R.id.create_oferta_sv)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateOferta))
                .perform(scrollTo(), click());
        onView(withId(R.id.editTextDateCreateOferta))
                .check(matches(withError("Fecha invalida (dd-mm-yyyy)")));
    }
    /**
     * Check error if the date is not put
     */
    @Test
    public void NoDate() {
        onView(withId(R.id.titleCreateOferta_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.DescriptionCreateOferta_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.PointsCreateOferta_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.create_oferta_sv)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateOferta))
                .perform(scrollTo(), click());
        onView(withId(R.id.editTextDateCreateOferta))
                .check(matches(withError("Fecha necesaria")));
    }
    /**
     * Check error if the points are not put
     */
    @Test
    public void NoPoints() {
        onView(withId(R.id.titleCreateOferta_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.DescriptionCreateOferta_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.editTextDateCreateOferta))
                .perform(clearText(), typeText("22-12-2017"));
        onView(withId(R.id.create_oferta_sv)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateOferta))
                .perform(scrollTo(), click());
        onView(withId(R.id.PointsCreateOferta_edit_text))
                .check(matches(withError("Descuento necesario")));
    }

    /**
     * Check error if the description is not put
     */
    @Test
    public void NoDescription() {
        onView(withId(R.id.titleCreateOferta_edit_text))
                .perform(clearText(), typeText("Title"));
        onView(withId(R.id.editTextDateCreateOferta))
                .perform(clearText(), typeText("22-12-2017"));
        onView(withId(R.id.PointsCreateOferta_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.create_oferta_sv)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateOferta))
                .perform(scrollTo(), click());
        onView(withId(R.id.DescriptionCreateOferta_edit_text))
                .check(matches(withError("Descripción necesaria")));
    }

    /**
     * Check error if the title is not put
     */
    @Test
    public void NoTitle() {
        onView(withId(R.id.DescriptionCreateOferta_edit_text))
                .perform(clearText(), typeText("Description"));
        onView(withId(R.id.PointsCreateOferta_edit_text))
                .perform(clearText(), typeText("100"));
        onView(withId(R.id.editTextDateCreateOferta))
                .perform(clearText(), typeText("22-12-2017"));
        onView(withId(R.id.create_oferta_sv)).perform(swipeUp());
        onView(withId(R.id.buttonSendCreateOferta))
                .perform(scrollTo(), click());
        onView(withId(R.id.titleCreateOferta_edit_text))
                .check(matches(withError("Título necesario")));
    }


    /**
     * Check if the the date is displayed
     */
    @Test
    public void CheckDateIsDisplayed(){
        onView(withId(R.id.DateTextCreateOferta))
                .check(matches(isDisplayed()));
        onView(withId(R.id.editTextDateCreateOferta))
                .check(matches(isDisplayed()));
        onView(withId(R.id.DateCreateOferta))
                .check(matches(isDisplayed()));
    }


    /**
     * Check if the the title is displayed
     */
    @Test
    public void CheckPointsAreDisplayed(){
        onView(withId(R.id.DiscountCreateOferta))
                .check(matches(isDisplayed()));
        onView(withId(R.id.PointsCreateOferta_edit_text))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the the description is displayed
     */
    @Test
    public void CheckDescriptionIsDisplayed(){
        onView(withId(R.id.DescriptionCreateOferta))
                .check(matches(isDisplayed()));
        onView(withId(R.id.DescriptionCreateOferta_edit_text))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the the title is displayed
     */
    @Test
    public void CheckTitleIsDisplayed(){
        onView(withId(R.id.TitleCreateOferta))
                .check(matches(isDisplayed()));
        onView(withId(R.id.titleCreateOferta_edit_text))
                .check(matches(isDisplayed()));
    }

    
}
