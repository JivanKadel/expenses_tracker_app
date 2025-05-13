package com.jivan.expense_tracker.ui.expenses;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jivan.expense_tracker.R;
import com.jivan.expense_tracker.data.expenses.CategoryRepository;
import com.jivan.expense_tracker.data.expenses.ExpenseRepository;
import com.jivan.expense_tracker.data.expenses.ExpensesDatabaseHelper;
import com.jivan.expense_tracker.domain.expenses.Category;
import com.jivan.expense_tracker.domain.expenses.Expense;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddExpenseFragment extends Fragment {
    private EditText etTitle, etAmount, etDate, etPaymentMethod, etNote;
    private Spinner spinnerCategory;
    private SwitchCompat switchRecurring;
    private Button btnSave;

    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;
    private SQLiteDatabase db;
    private List<Category> categoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        // Initialize views
        etTitle = view.findViewById(R.id.etExpenseTitle);
        etAmount = view.findViewById(R.id.etExpenseAmount);
        etDate = view.findViewById(R.id.etExpenseDate);
        etPaymentMethod = view.findViewById(R.id.etExpensePaymentMethod);
        etNote = view.findViewById(R.id.etExpenseNote);
        spinnerCategory = view.findViewById(R.id.spinnerExpenseCategory);
        switchRecurring = view.findViewById(R.id.switchExpenseIsRecurring);
        btnSave = view.findViewById(R.id.btnSaveExpense);

        // Initialize database and repositories
        db = new ExpensesDatabaseHelper(requireContext()).getWritableDatabase();
        expenseRepository = new ExpenseRepository(getContext());
        categoryRepository = new CategoryRepository(getContext());

        setupCategorySpinner();

        btnSave.setOnClickListener(v -> saveExpense());

        return view;
    }

    private void setupCategorySpinner() {
        categoryList = categoryRepository.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category c : categoryList) {
            categoryNames.add(c.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void saveExpense() {
        String title = etTitle.getText().toString();
        double amount = Double.parseDouble(etAmount.getText().toString());
        String dateStr = etDate.getText().toString();
        String paymentMethod = etPaymentMethod.getText().toString();
        String note = etNote.getText().toString();
        boolean isRecurring = switchRecurring.isChecked();
        String currency = "USD"; // hardcoded or get from settings

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(dateStr);

            int selectedIndex = spinnerCategory.getSelectedItemPosition();
            Category selectedCategory = categoryList.get(selectedIndex);

            Expense expense = new Expense();
            expense.setTitle(title);
            expense.setAmount(amount);
            expense.setDate(date);
            expense.setCategory(selectedCategory);
            expense.setPaymentMethod(paymentMethod);
            expense.setNote(note);
            expense.setRecurring(isRecurring);
            expense.setCurrency(currency);

            expenseRepository.addExpense(expense);

            Toast.makeText(getContext(), "Expense saved!", Toast.LENGTH_SHORT).show();
        } catch (ParseException | NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }
}
