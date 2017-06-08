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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Adrian on 01/06/2017.
 */

public class EditProfileTest {
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
            onView(withId(R.id.edit_profile_button)).perform(click());
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
            onView(withId(R.id.edit_profile_button)).perform(click());
        }
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void profileHasImage(){
        onView(withId(R.id.user_image_edit_user))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void profileHasName(){
        onView(withId(R.id.user_name_edit_user))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void profileHasNickName(){
        onView(withId(R.id.user_nickname_edit_user))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void profileHasTotalPoints(){
        onView(withId(R.id.user_total_points_edit_user))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void profileHasActualPoints(){
        onView(withId(R.id.user_current_points_edit_user))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void profileHasEditButton(){
        onView(withId(R.id.save_edit_profile_button))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void profileHasCreationDate(){
        onView(withId(R.id.gobro_since_edit_user))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void profileHasBirthDate(){
        onView(withId(R.id.birthdate_edit_user))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void profileHasBirthDateButton(){
        onView(withId(R.id.edit_birthdate_user_button))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void saveButtonDisplaysAlertDialogWithText(){
        onView(withId(R.id.save_edit_profile_button)).perform(click());
        onView(withText("¿Está seguro de que desea modificar su perfil?"));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void saveButtonDisplaysAlertDialogWithModifyButton(){
        onView(withId(R.id.save_edit_profile_button)).perform(click());
        onView(withText("MODIFICAR"));
    }

    /**
     * Check if the user image is displayed
     */
    @Test
    public void saveButtonDisplaysAlertDialogWithCancelButton(){
        onView(withId(R.id.save_edit_profile_button)).perform(click());
        onView(withText("CANCELAR"));
    }



    /**
     * Check if the user image is displayed
     */
    @Test
    public void cancelButtonOfAlertDialogDisplaysEditinfo(){
        onView(withId(R.id.save_edit_profile_button)).perform(click());
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.layoutEditProfile)).check(matches(isDisplayed()));
    }
}
