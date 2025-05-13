package com.jivan.expense_tracker.data.expenses;

import android.provider.BaseColumns;

public class ExpensesContract implements BaseColumns {
    public static final String TABLE_NAME = "expenses";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PAYMENT_METHOD = "payment_method";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_CURRENCY = "currency";
    public static final String COLUMN_IS_RECURRING = "is_recurring";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_AMOUNT + " REAL, " +
            COLUMN_CATEGORY_ID + " INTEGER, " +
            COLUMN_DATE + " TEXT, " +  // ISO 8601 (yyyy-MM-dd) recommended
            COLUMN_PAYMENT_METHOD + " TEXT, " +
            COLUMN_NOTE + " TEXT, " +
            COLUMN_CURRENCY + " TEXT, " +
            COLUMN_IS_RECURRING + " INTEGER DEFAULT 0, " +
            "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " +
            CategoryContract.TABLE_NAME + "(" + CategoryContract.COLUMN_ID + "))";
}
