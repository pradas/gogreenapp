package pes.gogreenapp.Objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GlobalPreferences {
    // Shared Preferences
    SharedPreferences global;

    // Editor for Shared preferences
    private Editor editor;

    // Context
    private Context _context;

    // Shared pref mode
    private static final int PRIVATE_MODE = 0;

    private static final String GLOBAL_PREF = "global";
    private static final String USER = "user";

    public GlobalPreferences(Context context) {
        this._context = context;
        global = _context.getSharedPreferences(GLOBAL_PREF, PRIVATE_MODE);
        editor = global.edit();
        editor.apply();
    }

    public void setUser(String username) {
        editor.putString(USER, username);
        editor.commit();
    }

    public String getUser() {
        return global.getString(USER, "");
    }
}
