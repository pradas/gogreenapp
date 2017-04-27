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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by Usuario on 27/04/2017.
 */

public class PrivateProfileTest {
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
                    .perform(NavigationViewActions.navigateTo(R.id.user_profile_fragment));

        } catch (NoMatchingViewException e) {
            onView(withId(R.id.textNombre))
                    .perform(clearText(), typeText("user"));
            onView(withId(R.id.textContraseña))
                    .perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin))
                    .perform(click());
        }
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void checkImageIsShown(){
        onView(withId(R.id.user_image))
                .check(matches(isDisplayed()));
    }


    /**
     * Check if the user name is displayed
     */
    @Test
    public void checkUserNameIsShown(){
        onView(withId(R.id.user_name))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user nickname is displayed
     */
    @Test
    public void checkUserNicknameIsShown(){
        onView(withId(R.id.user_nickname))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the current points are displayed
     */
    @Test
    public void checkUserCurrentPointsIsShown(){
        onView(withId(R.id.user_points))
                .check(matches(isDisplayed()));
    }


    /**
     * Check if the user birth date is displayed
     */
    @Test
    public void checkBirthDateIsShown(){
        onView(withId(R.id.user_birthdate))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user current points are displayed
     */
    @Test
    public void checkCurrentPointsAreShown(){
        onView(withId(R.id.user_birthdate))
                .check(matches(isDisplayed()));
    }

    
}
