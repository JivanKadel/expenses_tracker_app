package com.jivan.expense_tracker.ui.expenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jivan.expense_tracker.R;
import com.jivan.expense_tracker.domain.expenses.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    public interface OnExpenseClickListener {
        void onExpenseClick(Expense expense);
    }

    private List<Expense> expenses = new ArrayList<>();
    private OnExpenseClickListener listener;

    public ExpenseAdapter(List<Expense> expenses, OnExpenseClickListener listener) {
        this.expenses = expenses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.bind(expense, listener);
    }

    //    @Override
//    public int getItemCount() {a
//        return expenses.size();
//    }
    @Override
    public int getItemCount() {
        return expenses != null ? expenses.size() : 0;
    }


    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView expenseTitle, expenseAmount, expenseDate;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseTitle = itemView.findViewById(R.id.expenseTitle);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            expenseDate = itemView.findViewById(R.id.expenseDate);
        }

        public void bind(final Expense expense, final OnExpenseClickListener listener) {
            expenseTitle.setText(expense.getTitle());
            expenseAmount.setText(String.format(Locale.getDefault(), "%.2f %s", expense.getAmount(), expense.getCurrency()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            expenseDate.setText(sdf.format(expense.getDate()));

            itemView.setOnClickListener(v -> listener.onExpenseClick(expense));
        }
    }
}

