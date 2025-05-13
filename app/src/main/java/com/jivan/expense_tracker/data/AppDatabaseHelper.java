package com.jivan.expense_tracker.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jivan.expense_tracker.data.expenses.ExpensesContract;
import com.jivan.expense_tracker.data.auth.AuthContract;
import com.jivan.expense_tracker.data.expenses.CategoryContract;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense_tracker.db";
    private static final int DATABASE_VERSION = 1;

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create all tables here
        db.execSQL(ExpensesContract.CREATE_TABLE);
        db.execSQL(CategoryContract.CREATE_TABLE);
        db.execSQL(AuthContract.CREATE_TABLE); // If you have a login table
        insertDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For development: drop and recreate all
        db.execSQL("DROP TABLE IF EXISTS " + ExpensesContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AuthContract.TABLE_NAME);

        onCreate(db);
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + CategoryContract.TABLE_NAME +
                " (" + CategoryContract.COLUMN_NAME + ", " + CategoryContract.COLUMN_GROUP + ") VALUES " +
                "('Rent/Mortgage', 'Essentials')," +
                "('Utilities', 'Essentials')," +
                "('Groceries', 'Essentials')," +
                "('Transportation', 'Essentials')," +
                "('Dining Out', 'Lifestyle')," +
                "('Shopping', 'Lifestyle')," +
                "('Travel', 'Lifestyle')," +
                "('Subscriptions', 'Lifestyle')," +
                "('Loan Payments', 'Financial')," +
                "('Investments', 'Financial')," +
                "('Emergency Fund', 'Financial')," +
                "('Insurance', 'Financial')," +
                "('Healthcare', 'Personal')," +
                "('Education', 'Personal')," +
                "('Gifts & Donations', 'Personal')," +
                "('Hobbies', 'Personal')");
    }
}
