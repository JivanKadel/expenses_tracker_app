package com.jivan.expense_tracker.data.expenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jivan.expense_tracker.data.AppDatabaseHelper;
import com.jivan.expense_tracker.domain.expenses.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private SQLiteDatabase db;
    AppDatabaseHelper dbHelper;

    public CategoryRepository(Context context) {
        dbHelper = new AppDatabaseHelper(context);
    }

    public int addCategory(Category category) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CategoryContract.COLUMN_NAME, category.getName());
        values.put(CategoryContract.COLUMN_GROUP, category.getGroup());
        values.put(CategoryContract.COLUMN_IS_CUSTOM, category.isCustom());
        return (int) db.insert(CategoryContract.TABLE_NAME, null, values);
    }

    public List<Category> getAllCategories() {
        db = dbHelper.getReadableDatabase();
        List<Category> categories = new ArrayList<>();
        Cursor cursor = db.query(CategoryContract.TABLE_NAME, null, null, null, null, null, null);
        int idIndex = cursor.getColumnIndexOrThrow(CategoryContract.COLUMN_ID);
        int nameIndex = cursor.getColumnIndexOrThrow(CategoryContract.COLUMN_NAME);
        int groupIndex = cursor.getColumnIndexOrThrow(CategoryContract.COLUMN_GROUP);
        int customIndex = cursor.getColumnIndexOrThrow(CategoryContract.COLUMN_IS_CUSTOM);
        while (cursor.moveToNext()) {
            Category category = new Category(
                    cursor.getInt(idIndex),
                    cursor.getString(nameIndex),
                    cursor.getString(groupIndex),
                    cursor.getInt(customIndex) == 1
            );
            categories.add(category);
        }
        cursor.close();
        return categories;
    }

    public int updateCategory(Category category) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CategoryContract.COLUMN_NAME, category.getName());
        values.put(CategoryContract.COLUMN_GROUP, category.getGroup());
        String selection = CategoryContract.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(category.getId())};
        return db.update(CategoryContract.TABLE_NAME, values, selection, selectionArgs);
    }

    public int deleteCategory(int id) {
        db = dbHelper.getWritableDatabase();
        return db.delete(CategoryContract.TABLE_NAME, CategoryContract.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
