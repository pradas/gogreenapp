/*
 * All right reserverd to GoBros Devevelopers team.
 * This code is free software; you can redistribute it and/or modify itunder the terms of
 * the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

package pes.gogreenapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.R.attr.country;
import static android.R.attr.description;
import static android.R.attr.value;

/**
 * @author Albert
 */
public class ActualUserData {

    private static String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_USERNAME, MySQLiteHelper.COLUMN_EMAIL,
            MySQLiteHelper.COLUMN_TOKEN, MySQLiteHelper.COLUMN_POINTS};

    /**
     *
     * @param username username of the new User
     * @param email email of the new User
     * @param token API token of the new User
     * @param points points of the new User
     * @param context context of the Android APP
     */
    public static void createUser(String username, String email, String token, Integer points,
                                  Context context) throws Exception {
        if (username == null || token == null || points == null || context == null) {
            throw new Exception();
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
}
