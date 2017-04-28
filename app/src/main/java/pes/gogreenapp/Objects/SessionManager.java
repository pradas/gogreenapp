package pes.gogreenapp.Objects;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import pes.gogreenapp.Activities.LoginActivity;
import pes.gogreenapp.R;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    private Editor editor;

    // Context
    private Context _context;

    // Shared pref mode
    private static final int PRIVATE_MODE = 0;

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_POINTS = "points";

    /**
     *
     * @param context context of the APP
     * @param Username username of the shared preferences
     */
    public SessionManager(Context context, String Username) {
        this._context = context;
        pref = _context.getSharedPreferences(Username, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    /**
     * Create login_activity session
     */
    public void createLoginSession(String username, String token, int points) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_POINTS, points);
        editor.commit();
    }

    /**
     * Check login_activity method wil check user login_activity status
     * If false it will redirect user to login_activity page
     * Else won't do anything
     */
    public void checkLogin() {
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring LoginActivity Activity
            _context.startActivity(i);
        }
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring LoginActivity Activity
        _context.startActivity(i);
    }

    private boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, "");
    }

    public String getUserName() {
        return pref.getString(KEY_USERNAME, "");
    }

    public int getPoints() { return pref.getInt(KEY_POINTS, 0); }

    public void setKeyPoints (Integer points) {}
}