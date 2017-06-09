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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Adrian on 05/06/2017.
 */

public class ModifyShopProfileTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Before the tests the Navigation Drawer is open, if cant be open it is because there isn't
     * a valid user logged. Due this, the setup do the Login with
     * the username manager and the password Password12
     */
    @Before
    public void setup() {
        try {
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.nvView))
                    .perform(NavigationViewActions.navigateTo(R.id.log_out));
            onView(withId(R.id.username_edit_text))
                    .perform(clearText(), typeText("manager"));
            onView(withId(R.id.password_user_text))
                    .perform(clearText(), typeText("Password12"));
            onView(withId(R.id.buttonLogin))
                    .perform(click());
            onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.nvView))
                    .perform(NavigationViewActions.navigateTo(R.id.shop_view_fragment));
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
                    .perform(NavigationViewActions.navigateTo(R.id.shop_view_fragment));
        }
    }

    /**
     * Check if the xml have the image
     */
    @Test
    public void fragmentHasImage() {
        onView(withId(R.id.editProfileShopButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.shop_image_edit_profile))
                .perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have name
     */
    @Test
    public void fragmentHasName() {
        onView(withId(R.id.editProfileShopButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.shop_name_edit_profile))
                .perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have email
     */
    @Test
    public void fragmentHasEmail() {
        onView(withId(R.id.editProfileShopButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.shop_email_edit_profile))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have address
     */
    @Test
    public void fragmentHasAddress() {
        onView(withId(R.id.editProfileShopButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.shop_address_edit_profile))
                .check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have save button
     */
    @Test
    public void fragmentHasSaveButton() {
        onView(withId(R.id.editProfileShopButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.saveEditProfileButton))
                .perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in save displays alert dialog with text
     */
    @Test
    public void saveButtonDisplayAlertDialogWithText() {
        onView(withId(R.id.editProfileShopButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.saveEditProfileButton))
                .perform(scrollTo())
                .perform(click());
        onView(withText("¿Está seguro de que desea modificar su perfil?")).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in save displays alert dialog with modify button
     */
    @Test
    public void saveButtonDisplayAlertDialogWithModifyButton() {
        onView(withId(R.id.editProfileShopButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.saveEditProfileButton))
                .perform(scrollTo())
                .perform(click());
        onView(withText("MODIFICAR")).check(matches(isDisplayed()));
    }

    /**
     * Check if clicking in save displays alert dialog with cancel button
     */
    @Test
    public void saveButtonDisplayAlertDialogWithCancelButton() {
        onView(withId(R.id.editProfileShopButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.saveEditProfileButton))
                .perform(scrollTo())
                .perform(click());
        onView(withText("CANCELAR"))
                .perform(scrollTo())
                .check(matches(isDisplayed()));
    }



    /**
     * Check if edit the name of shop without confirm stays in the same fragment with the same changes
     */
    @Test
    public void cancelButtonOfAlertDialogDisplayTheSameInfo() {
        onView(withId(R.id.editProfileShopButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.shop_name_edit_profile))
                .perform(clearText(), typeText("Tienda Oficial"));
        onView(withId(R.id.saveEditProfileButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(android.R.id.button2))
                .perform(scrollTo())
                .perform(click());
        onView(withText("Tienda Oficial"))
                .perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    /**
     * Check if edit the name of shop without confirm stays in the same fragment
     */
    @Test
    public void cancelButtonOfAlertDialogDisplayShopInfoFragment() {
        onView(withId(R.id.editProfileShopButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.saveEditProfileButton))
                .perform(scrollTo())
                .perform(click());
        onView(withId(android.R.id.button2))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.shopEditProfileFragment))
                .check(matches(isDisplayed()));
    }

}
