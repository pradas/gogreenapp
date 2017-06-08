package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pes.gogreenapp.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Adrian on 01/06/2017.
 */

public class ProfileTest {

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
            onView(withId(R.id.profile_image))
                    .perform(click());

        } catch (NoMatchingViewException e) {
            onView(withId(R.id.username_edit_text))
                    .perform(clearText(), typeText("user"));
            onView(withId(R.id.password_user_text))
                    .perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin))
                    .perform(click());
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.profile_image))
                    .perform(click());
        }
    }

    /**
     * Check if the xml have image
     */
    @Test
    public void profileHasImage(){
        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeDown());
        onView(withId(R.id.user_image))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have name
     */
    @Test
    public void profileHasName(){
        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeDown());
        onView(withId(R.id.user_name))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have nickname
     */
    @Test
    public void profileHasNickName(){
        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeDown());
        onView(withId(R.id.user_nickname))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have total points
     */
    @Test
    public void profileHasTotalPoints(){
        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeDown());
        onView(withId(R.id.user_total_points))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have actual points
     */
    @Test
    public void profileHasActualPoints(){
        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeDown());
        onView(withId(R.id.user_actual_points))
                .check(matches(isDisplayed()));
    }
    /**
     * Check if the xml have edit button
     */
    @Test
    public void profileHasEditButton(){
        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeDown());
        onView(withId(R.id.edit_profile_button))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have birth date
     */
    @Test
    public void profileHasBirthDate(){
        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.user_birthdate))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have email
     */
    @Test
    public void profileHasEmail(){
        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeDown());
        onView(withId(R.id.user_email))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the rewards exchanged is displayed
     */
    @Test
    public void checkRewardsExchangedsIsDisplayed() {
        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.rewardsExchangedFragment)).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking the edit profile show the edit profile fragment
     */
    @Test
    public void pressButtonEditDisplaysEditFragment() {
        onView(withId(R.id.scrollUserProfile)).perform(ViewActions.swipeUp());
        onView(withId(R.id.edit_profile_button))
                .perform(scrollTo()).perform(click());
        onView(withId(R.id.layoutEditProfile))
                .perform(scrollTo()).check(matches(isDisplayed()));
    }
}
