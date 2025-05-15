package com.jivan.expense_tracker.data.auth;

import android.provider.BaseColumns;

public class AuthContract implements BaseColumns {
    public static final String TABLE_NAME = "users";

    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL" +
                    ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}

