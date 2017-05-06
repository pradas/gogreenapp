/*
 * All right reserverd to GoBros Devevelopers team.
 * This code is free software; you can redistribute it and/or modify itunder the terms of
 * the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

package pes.gogreenapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.Exceptions.NullParametersException;

/**
 * @author Albert
 */
public class ActualUserData {

    /**
     * Method to create a new User on the table Users of gogreen.db
     *
     * @param username username of the new User
     * @param email    email of the new User
     * @param token    API token of the new User
     * @param points   points of the new User
     * @param context  context of the Android APP
     * @throws NullParametersException throws NullParametersException when any parameter desired
     *                                 is null
     */
    public static void createUser(String username, String email, String token, Integer points,
                                  Context context) throws NullParametersException {

        if (username == null || token == null || points == null || context == null) {
            throw new NullParametersException("The username, token, points and context " +
                    "parameters can't be null");
        }

        SQLiteDatabase db = MySQLiteHelper.getInstance(context).getReadableDatabase();
        ContentValues values = new ContentValues();

        Log.d("Creating", "Creating USER: " + username);

        // Insert data
        values.put(MySQLiteHelper.COLUMN_USERNAME, username);
        values.put(MySQLiteHelper.COLUMN_EMAIL, email);
        values.put(MySQLiteHelper.COLUMN_TOKEN, token);
        values.put(MySQLiteHelper.COLUMN_POINTS, points);

        // Actual insertion of the data using the values variable
        db.insert(MySQLiteHelper.TABLE_USERS, null, values);

        // Close the database
        db.close();
    }

    /**
     * Method to delete a User with username = this.username from the table Users of gogreen.db
     *
     * @param username the username of the User that wanna be deleted
     * @param context  context of the Android APP
     * @throws NullParametersException throws NullParametersException when any parameter desired
     *                                 is null
     */
    public static void deleteUser(String username, Context context) throws NullParametersException {

        if (username == null || context == null) {
            throw new NullParametersException("The username and context parameters can't be null");
        }

        SQLiteDatabase db = MySQLiteHelper.getInstance(context).getReadableDatabase();

        Log.d("Deleting", "Username deleted with username: " + username);

        // Deleting the User with username = this.username
        db.delete(MySQLiteHelper.TABLE_USERS, MySQLiteHelper.COLUMN_USERNAME
                + " = \"" + username + "\"", null);

        // Close the database
        db.close();
    }

    /**
     * Method to get all the usernames that don't coincide with the username of the actual
     * User logged
     *
     * @param context        context of the Android APP
     * @param actualUsername username of the actual User logged
     * @return a list of String containing the usernames
     * @throws NullParametersException throws NullParametersException when any parameter desired
     *                                 is null
     */
    public static List<String> getUsernames(Context context, String actualUsername)
            throws NullParametersException {

        if (actualUsername == null || context == null) {
            throw new NullParametersException("The username and context parameters can't be null");
        }

        List<String> usernames = new ArrayList<>();
        SQLiteDatabase db = MySQLiteHelper.getInstance(context).getReadableDatabase();

        // Creates a cursor to iterate the result of the query
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS,
                new String[]{MySQLiteHelper.COLUMN_USERNAME}, null, null, null, null, null);

        // Iterate the cursor and fill the usernames list
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (!(actualUsername.equals(cursor.getString(0)))) {
                usernames.add(cursor.getString(0));
            }

            cursor.moveToNext();
        }

        // close the cursor
        cursor.close();

        // close the database
        db.close();

        return usernames;
    }

}
