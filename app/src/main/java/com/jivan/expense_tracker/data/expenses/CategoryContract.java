package com.jivan.expense_tracker.data.expenses;

public class CategoryContract {
    public static final String TABLE_NAME = "categories";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GROUP = "group_name";
    public static final String COLUMN_IS_CUSTOM = "is_custom";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_GROUP + " TEXT, " + COLUMN_IS_CUSTOM + " INTEGER DEFAULT 0 )";
}
