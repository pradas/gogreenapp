package pes.gogreenapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

/**
 * Created by Adrian on 27/04/2017.
 */

public class EspressoTestsMatchers {
    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }

    public static class DrawableMatcher extends TypeSafeMatcher<View> {
        private final int expectedId;
        String resourceName;

        public DrawableMatcher(int resourceId) {
            super(View.class);
            this.expectedId = resourceId;
        }
        @Override
        public boolean matchesSafely(View target) {
            if (!(target instanceof ImageView || target instanceof ImageButton)){
                return false;
            }

            BitmapDrawable bmd = null;
            if (target instanceof  ImageView){
                ImageView imageView = (ImageView) target;
                if (expectedId < 0){
                    return imageView.getDrawable() == null;
                }
                bmd = (BitmapDrawable) imageView.getDrawable();
            } else{
                ImageButton imageButton = (ImageButton) target;
                if (expectedId < 0){
                    return imageButton.getDrawable() == null;
                }
                bmd = (BitmapDrawable) imageButton.getDrawable();
            }

            Resources resources = target.getContext().getResources();
            Drawable expectedDrawable = resources.getDrawable(expectedId);
            resourceName = resources.getResourceEntryName(expectedId);
            if (expectedDrawable == null) {
                return false;
            }

            Bitmap bitmap = bmd.getBitmap();
            BitmapDrawable expected = (BitmapDrawable) expectedDrawable;
            Bitmap otherBitmap = expected.getBitmap();
            return bitmap.sameAs(otherBitmap);
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
    }
}
