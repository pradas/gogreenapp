package pes.gogreenapp;

import android.os.AsyncTask;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.EditText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import pes.gogreenapp.Activities.LoginActivity;
import pes.gogreenapp.Activities.MainActivity;
import pes.gogreenapp.Fragments.RegisterFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
/**
 * Created by Jorge Alvarez on 27/04/2017.
 *
 * Checks and validates each functionality of the Register
 * @see  RegisterFragment
 */

public class RegisterActivityTest {
    /**
     * Checks if the params parsed are equal to the error returned of the view
     *
     * @param expected The error content to match
     * @return true if error matches, false if View is not an EditText or error dosen't match
     */
    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText editText = (EditText) view;
                return editText.getError().toString().equals(expected);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    /**
     * Checks if the View has an error or not.
     *
     * @return true if View dosen't have any error, false if view is not an EditText or View has error.
     */
    private static Matcher<View> notHasError() {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText editText = (EditText) view;
                return editText.getError() == null;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    /**
     * At start, enter to the register view
     */
    @Before
    public void setup() {
        try {
            /*onView(withId(R.id.drawer_layout))
                    .perform(DrawerActions.open());*/
            onView(withId(R.id.buttonRegister))
                    .perform(click());
            RegisterFragment.testMode = true;
        } catch (NoMatchingViewException e) {
        }
    }

    @Test
    /**
     * Validates if there's no data on the field "Nombre"
     * return message error "Campo necesario"
     */
    public void noNameInRegister() {
        onView(withId(R.id.create_user_button))
                .perform(click());
        onView(withId(R.id.editName))
                .check(matches(withError("Campo necesario")));
    }

    @Test
    /**
     * Validates if there's no data on the field "Nombre de usuario"
     * return message error "Campo necesario"
     */
    public void noUsernameInRegister() {
        onView(withId(R.id.create_user_button))
                .perform(click());
        onView(withId(R.id.editUsername))
                .check(matches(withError("Campo necesario")));
    }

    @Test
    /**
     * Validates if there's no data on the field "Correo electronico"
     * return message error "Campo necesario"
     */
    public void noEmailInRegister() {
        onView(withId(R.id.create_user_button))
                .perform(click());
        onView(withId(R.id.editEmail))
                .check(matches(withError("Campo necesario")));

    }
    @Test
    /**
     * Validates if the data put on the input field "Correo electronico " is actually an Email
     * return message error "Email no valido"
     */
    public void noVaildEmailInRegister() {
        onView(withId(R.id.editEmail))
                .perform(clearText(), typeText("iLikeToCallMyselfPrincess"));
        onView(withId(R.id.create_user_button))
                .perform(click());
        onView(withId(R.id.editEmail))
                .check(matches(withError("Email no valido")));
    }

    @Test
    /**
     * Validates if there's no data on the field "Contraseña"
     * return message error "Campo necesario"
     */
    public void noPasswordInRegister() {
        onView(withId(R.id.create_user_button))
                .perform(click());
        onView(withId(R.id.editContraseña))
                .check(matches(withError("Campo necesario")));

    }

    @Test
    /**
     * Validates if there's no data on the field "Confirmar Contraseña"
     * return message error "Campo necesario"
     */
    public void noPasswordConfirmInRegister() {
        onView(withId(R.id.create_user_button))
                .perform(click());
        onView(withId(R.id.editContraseñaConfirmar))
                .check(matches(withError("Campo necesario")));
    }

    @Test
    /**
     * Validates if data inside the fields "Contraseña" and "Confirmar Contraseña"
     * return message error "Campo necesario"
     */
    public void PasswordsAreNotEqualsInRegister() {
        onView(withId(R.id.editContraseña))
                .perform(clearText(), typeText("There's only"));
        onView(withId(R.id.editContraseñaConfirmar))
                .perform(clearText(), typeText("2 Genders"));
        onView(withId(R.id.create_user_button))
                .perform(click());
        onView(withId(R.id.editContraseña))
                .check(matches(withError("La contraseña no es la misma")));
    }

    @Test
    /**
     * Validates if data inside the field "Fecha de nacimiento" has data
     * return message error "La contraseña no es la misma"
     */
    public void noDateInRegister(){
        onView(withId(R.id.create_user_button))
                .perform(click());
        onView(withId(R.id.editFechaNacimiento))
                .check(matches(withError("Campo necesario")));
    }

    @Test
    /**
     * Check if every data is inserted correctly
     */
    public void checkRegisterValid(){
        //hasError
        onView(withId(R.id.editName))
                .perform(clearText(), typeText("Test"));
        onView(withId(R.id.editUsername))
                .perform(clearText(), typeText("Test"));
        onView(withId(R.id.editEmail))
                .perform(clearText(), typeText("test@test.com"));
        onView(withId(R.id.editContraseña))
                .perform(clearText(), typeText("1234"));
        onView(withId(R.id.editContraseñaConfirmar))
                .perform(clearText(), typeText("1234"));

        onView(withId(R.id.datePickerButton))
                .perform(click());
        onView(withText("OK")).perform(click());

        //Start the creation
        onView(withId(R.id.create_user_button))
                .perform(click());

        //Check everything is K
        onView(withId(R.id.editName))
                .check(matches(notHasError()));
        onView(withId(R.id.editUsername))
                .check(matches(notHasError()));
        onView(withId(R.id.editEmail))
                .check(matches(notHasError()));
        onView(withId(R.id.editContraseña))
                .check(matches(notHasError()));
        onView(withId(R.id.editContraseñaConfirmar))
                .check(matches(notHasError()));
    }
    @After
    public void unsetup(){
        RegisterFragment.testMode = false;
    }
}
