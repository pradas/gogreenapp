package pes.gogreenapp.Utils;

import android.app.Application;
import android.content.Context;

import pes.gogreenapp.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Usuario on 11/05/2017.
 */


public class GoGreenApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //setting on default the custom font added to the project
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        //Parse SDK stuff goes here
    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}