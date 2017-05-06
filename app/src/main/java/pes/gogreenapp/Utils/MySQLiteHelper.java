/*
 * All right reserverd to GoBros Devevelopers team.
 * This code is free software; you can redistribute it and/or modify itunder the terms of
 * the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

package pes.gogreenapp.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Albert
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static MySQLiteHelper instance;
    private static final String DATABASE_NAME = "gogreen.db";
    private static final int DATABASE_VERSION = 1;
    static final String TABLE_USERS = "users";
    static final String COLUMN_USERNAME = "username";
    static final String COLUMN_EMAIL = "email";
    static final String COLUMN_TOKEN = "token";
    static final String COLUMN_POINTS = "points";
    static final String COLUMN_ROLE = "role";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_USERS + "( "
            + COLUMN_USERNAME + " text primary key, "
            + COLUMN_EMAIL + " text, "
            + COLUMN_TOKEN + " text not null, "
            + COLUMN_POINTS + " integer not null, "
            + COLUMN_ROLE + " text not null "
            + ");";

    /**
     * Create a instance of MySQLiteHelper and create the database if not exist
     *
     * @param context context of the Android APP
     */
    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Return a MySQLiteHelper instance if exists, either create a new one and return it.
     *
     * @param context context of the Android APP
     * @return a instance of MySQLiteHelper
     */
    public static MySQLiteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MySQLiteHelper(context);
        }
        return instance;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version "
                        + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
