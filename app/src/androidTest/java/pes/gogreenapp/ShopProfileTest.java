package pes.gogreenapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pes.gogreenapp.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Adrian on 05/06/2017.
 */

public class ShopProfileTest {

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
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasImage() {
        onView(withId(R.id.shop_image)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasName() {
        onView(withId(R.id.shop_name)).check(matches(isDisplayed()));
    }

    /**
     *
     */
    @Test
    public void fragmentHasEmail() {
        onView(withId(R.id.shop_email)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasAddress() {
        onView(withId(R.id.shop_address)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasDeals() {
        onView(withId(R.id.rv_ofertas)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasEditButton() {
        onView(withId(R.id.editProfileShopButton)).check(matches(isDisplayed()));
    }

    /**
     * Check if the xml have the switch button
     */
    @Test
    public void fragmentHasntEditButtonAsEmployee() {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nvView))
                .perform(NavigationViewActions.navigateTo(R.id.log_out));
        onView(withId(R.id.username_edit_text))
                .perform(clearText(), typeText("shoper"));
        onView(withId(R.id.password_user_text))
                .perform(clearText(), typeText("Password12"));
        onView(withId(R.id.buttonLogin))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nvView))
                .perform(NavigationViewActions.navigateTo(R.id.shop_view_fragment));
        onView(withId(R.id.editProfileShopButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

}
