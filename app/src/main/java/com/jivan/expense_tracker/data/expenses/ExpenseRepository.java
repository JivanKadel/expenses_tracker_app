package com.jivan.expense_tracker.data.expenses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jivan.expense_tracker.domain.expenses.Category;
import com.jivan.expense_tracker.domain.expenses.Expense;
import com.jivan.expense_tracker.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {
    private SQLiteDatabase db;
    private CategoryRepository categoryRepo;

    public ExpenseRepository(SQLiteDatabase db) {
        this.db = db;
        this.categoryRepo = new CategoryRepository(db);
    }

    public long addExpense(Expense expense) {
        ContentValues values = new ContentValues();

        values.put(ExpensesContract.COLUMN_TITLE, expense.getTitle());
        values.put(ExpensesContract.COLUMN_AMOUNT, expense.getAmount());
        values.put(ExpensesContract.COLUMN_CATEGORY_ID, expense.getCategory().getId());
        values.put(ExpensesContract.COLUMN_DATE, Helper.dataToString(expense.getDate()));
        values.put(ExpensesContract.COLUMN_PAYMENT_METHOD, expense.getPaymentMethod());
        values.put(ExpensesContract.COLUMN_NOTE, expense.getNote());
        values.put(ExpensesContract.COLUMN_CURRENCY, expense.getCurrency());
        values.put(ExpensesContract.COLUMN_IS_RECURRING, expense.isRecurring() ? 1 : 0);

        return db.insert(ExpensesContract.TABLE_NAME, null, values);
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT e.*, c." + CategoryContract.COLUMN_NAME + ", c." + CategoryContract.COLUMN_GROUP +
                ", c." + CategoryContract.COLUMN_IS_CUSTOM + " FROM " + ExpensesContract.TABLE_NAME +
                " e " + " INNER JOIN " + CategoryContract.TABLE_NAME + " c " +
                " On e." + ExpensesContract.COLUMN_CATEGORY_ID + " = c." + CategoryContract.COLUMN_ID;

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int cIdIndex = cursor.getColumnIndexOrThrow(ExpensesContract.COLUMN_CATEGORY_ID);
            int cNameIndex = cursor.getColumnIndexOrThrow(CategoryContract.COLUMN_NAME);
            int cGroupIndex = cursor.getColumnIndexOrThrow(CategoryContract.COLUMN_GROUP);
            int cIsCustomIndex = cursor.getColumnIndexOrThrow(CategoryContract.COLUMN_IS_CUSTOM);

            Category category = new Category(
                    cursor.getInt(cIdIndex),
                    cursor.getString(cNameIndex),
                    cursor.getString(cGroupIndex),
                    cursor.getInt(cIsCustomIndex) == 1);

            int eIdIndex = cursor.getColumnIndexOrThrow(ExpensesContract.COLUMN_ID);
            int eTitleIndex = cursor.getColumnIndexOrThrow(ExpensesContract.COLUMN_TITLE);
            int eAmountIndex = cursor.getColumnIndexOrThrow(ExpensesContract.COLUMN_AMOUNT);
            int eDateIndex = cursor.getColumnIndexOrThrow(ExpensesContract.COLUMN_DATE);
            int ePaymentMethodIndex = cursor.getColumnIndexOrThrow(ExpensesContract.COLUMN_PAYMENT_METHOD);
            int eNoteIndex = cursor.getColumnIndexOrThrow(ExpensesContract.COLUMN_NOTE);
            int eCurrencyIndex = cursor.getColumnIndexOrThrow(ExpensesContract.COLUMN_CURRENCY);
            int eIsRecurringIndex = cursor.getColumnIndexOrThrow(ExpensesContract.COLUMN_IS_RECURRING);

            Expense expense = new Expense(
                    cursor.getInt(eIdIndex),
                    cursor.getString(eTitleIndex),
                    cursor.getDouble(eAmountIndex),
                    category,
                    Helper.stringToDate(cursor.getString(eDateIndex)),
                    cursor.getString(ePaymentMethodIndex),
                    cursor.getString(eNoteIndex),
                    cursor.getString(eCurrencyIndex),
                    cursor.getInt(eIsRecurringIndex) == 1
            );
            expenses.add(expense);
        }
        cursor.close();
        return expenses;
    }

    public int deleteExpense(int expenseId) {
        return db.delete(ExpensesContract.TABLE_NAME, ExpensesContract.COLUMN_ID + " = ?", new String[]{String.valueOf(expenseId)});
    }


}
