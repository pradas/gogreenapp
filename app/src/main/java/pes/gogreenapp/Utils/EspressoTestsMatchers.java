package pes.gogreenapp.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.R.attr.bitmap;
import static android.R.attr.description;

/**
 * Created by Adrian on 28/04/2017.
 * import org.junit.internal.matchers.TypeSafeMatcher;
 * <p>
 * /**
 * Created by Adrian on 27/04/2017.
 */

public class EspressoTestsMatchers {

    public static Matcher<View> withDrawable(final int resourceId) {

        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {

        return new DrawableMatcher(-1);
    }

    public static Matcher<View> withError(final String expected) {

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

    private static class DrawableMatcher extends TypeSafeMatcher<View> {

        private final int expectedId;
        String resourceName;

        DrawableMatcher(int resourceId) {

            super(View.class);
            this.expectedId = resourceId;
        }

        @Override
        protected boolean matchesSafely(View target) {

            if (!(target instanceof ImageView || target instanceof ImageButton)) {
                return false;
            }

            BitmapDrawable bmd = null;
            Bitmap vectorBitmap = null;
            if (target instanceof ImageView) {
                ImageView imageView = (ImageView) target;
                if (expectedId < 0) {
                    return imageView.getDrawable() == null;
                }

                // VectorDrawableCompat to Bitmap for compare to another Bitmap
                String aux = imageView.getDrawable().getClass().getSimpleName();
                if (imageView.getDrawable().getClass().getSimpleName().contains("VectorDrawable")) {
                    vectorBitmap = vectorDrawableToBitmap(imageView.getDrawable());
                } else {
                    bmd = (BitmapDrawable) imageView.getDrawable();
                }
            } else {
                ImageButton imageButton = (ImageButton) target;
                if (expectedId < 0) {
                    return imageButton.getDrawable() == null;
                }
                bmd = (BitmapDrawable) imageButton.getDrawable();
            }

            Resources resources = target.getContext().getResources();
            Drawable expectedDrawable = resources.getDrawable(expectedId, null);
            resourceName = resources.getResourceEntryName(expectedId);
            if (expectedDrawable == null) {
                return false;
            }

            if (vectorBitmap == null) {
                Bitmap bitmap = bmd.getBitmap();
                BitmapDrawable expected = (BitmapDrawable) expectedDrawable;
                Bitmap otherBitmap = expected.getBitmap();
                return bitmap.sameAs(otherBitmap);
            } else {
                Bitmap expectedBitmap = vectorDrawableToBitmap(expectedDrawable);
                return expectedBitmap.sameAs(vectorBitmap);
            }
        }

        @Override
        public void describeTo(Description description) {

            description.appendText("with drawable from resource id: ");
            description.appendValue(expectedId);
            if (resourceName != null) {
                description.appendText("[");
                description.appendText(resourceName);
                description.appendText("]");
            }
        }

        /**
         * Transform a Vector Drawable into a Bitmap
         *
         * @param drawable Vector Drawable desired to transform to Bitmap
         *
         * @return the Bitmap of the Vector Drawable
         */
        private Bitmap vectorDrawableToBitmap(Drawable drawable) {

            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    public static ViewAction clickChildViewWithId(final int id) {

        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {

                return null;
            }

            @Override
            public String getDescription() {

                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {

                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}
