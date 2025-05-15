package com.jivan.expense_tracker.ui.expenses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jivan.expense_tracker.R;
import com.jivan.expense_tracker.data.expenses.ExpenseRepository;
import com.jivan.expense_tracker.domain.expenses.Expense;
import com.jivan.expense_tracker.util.Helper;

public class ExpenseDetailFragment extends Fragment {


    public ExpenseDetailFragment() {
        // Required empty public constructor
        Log.d(String.valueOf(Log.ERROR), "ExpenseDetailFragment Constructor ");
    }


    public static ExpenseDetailFragment newInstance(String param1, String param2) {
        ExpenseDetailFragment fragment = new ExpenseDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_detail, container, false);

        if (getArguments() != null) {
            int expenseId = getArguments().getInt("expense_id", 0);

            Expense expense = new ExpenseRepository(getContext()).getExpenseById(expenseId);

            TextView tvTitle = view.findViewById(R.id.tvExpenseTitle);
            TextView tvAmount = view.findViewById(R.id.tvExpenseAmount);
            TextView tvDate = view.findViewById(R.id.tvExpenseDate);
            TextView tvCategory = view.findViewById(R.id.tvExpenseCategory);
            TextView tvNote = view.findViewById(R.id.tvExpenseNote);

            if (expense != null) {
                tvTitle.setText(expense.getTitle());
                tvAmount.setText(String.format("Rs. %s", expense.getAmount()));
                tvDate.setText(Helper.dataToString(expense.getDate())); // format if needed
                tvCategory.setText(expense.getCategory().getName());
                tvNote.setText(expense.getNote());
            } else {
                tvTitle.setText(R.string.expense_not_found);
            }
        }

        return view;
    }


}