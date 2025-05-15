package com.jivan.expense_tracker.data.auth;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jivan.expense_tracker.data.AppDatabaseHelper;

import org.mindrot.jbcrypt.BCrypt;

public class UserRepository {

    private final AppDatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new AppDatabaseHelper(context);
    }

    public boolean registerUser(String username, String email, String password) {
        if (userExists(username)) return false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        values.put(AuthContract.COLUMN_USERNAME, username);
        values.put(AuthContract.COLUMN_EMAIL, email);
        values.put(AuthContract.COLUMN_PASSWORD, hashedPassword);

        long result = db.insert(AuthContract.TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean loginUser(String username, String enteredPassword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                AuthContract.TABLE_NAME,
                new String[]{AuthContract.COLUMN_PASSWORD},
                AuthContract.COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            String storedHashedPassword = cursor.getString(cursor.getColumnIndexOrThrow(AuthContract.COLUMN_PASSWORD));
            cursor.close();

            return BCrypt.checkpw(enteredPassword, storedHashedPassword);
        }

        cursor.close();
        return false;
    }


    public boolean userExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                AuthContract.TABLE_NAME,
                null,
                AuthContract.COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
}

