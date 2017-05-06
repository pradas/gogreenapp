/*
 * All right reserverd to GoBros Devevelopers team.
 * This code is free software; you can redistribute it and/or modify itunder the terms of
 * the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

package pes.gogreenapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import pes.gogreenapp.Activities.LoginActivity;

/**
 * @author Albert
 */

public class SessionManager {

    // Instance of Session Manager
    private static SessionManager instance;

    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private Editor editor;

    // Context
    private Context _context;

    // Shared pref mode
    private static final int PRIVATE_MODE = 0;

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "logged";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_POINTS = "points";
    private static final String KEY_ROLE = "role";
    private static final String SETTINGS_NAME = "default_settings";

    /**
     * Session Manager constructor
     *
     * @param context context of the Android APP
     */
    private SessionManager(Context context) {

        this._context = context;
        pref = _context.getSharedPreferences(SETTINGS_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    /**
     * Method to get the instance of Session Manager that corresponds to the Context argument,
     * if not exists, creates the instance
     *
     * @param context context of the Android APP
     * @return a instance of Session Manager
     */
    public static SessionManager getInstance(Context context) {

        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    /**
     * Method to get the instance of Session Manager
     *
     * @return a instance of Session Manager
     */
    public static SessionManager getInstance() {

        if (instance != null) {
            return instance;
        }
        throw new IllegalArgumentException("Should use getInstance(Context) at least " +
                "once before using this method.");
    }

    /**
     * Method to put all the info of the login session into the Shared Preferences
     *
     * @param username username of the Login Session
     * @param role     role of the Login Session
     * @param token    API token of the of the Login Session
     * @param points   points of the Login Session
     */
    public void putInfoLoginSession(String username, String role, String token, int points) {

        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_POINTS, points);
        editor.commit();
    }

    /**
     * Check if the User is logged, if not, redirects to a new Login Activity
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
     * Method to clear all the info of the Login Session when the user log out
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

    /**
     * Getter of the logged property
     *
     * @return true if User is logged, either return false
     */
    private boolean isLoggedIn() {

        return pref.getBoolean(IS_LOGIN, false);
    }

    /**
     * Getter of the token property
     *
     * @return the API token in a String
     */
    public String getToken() {

        return pref.getString(KEY_TOKEN, "");
    }

    /**
     * Getter of the username property
     *
     * @return the username in a String
     */
    public String getUsername() {

        return pref.getString(KEY_USERNAME, "");
    }

    /**
     * Getter of the points property
     *
     * @return the Integer value of points
     */
    public int getPoints() {

        return pref.getInt(KEY_POINTS, 0);
    }

    /**
     * Setter of the new value of points property
     *
     * @param points new value of points property
     */
    public void setPoints(Integer points) {
        editor.putInt(KEY_POINTS, points);
        editor.commit();
    }
}