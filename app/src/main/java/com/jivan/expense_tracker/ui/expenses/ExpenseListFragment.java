package com.jivan.expense_tracker.ui.expenses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jivan.expense_tracker.R;
import com.jivan.expense_tracker.data.expenses.ExpenseRepository;
import com.jivan.expense_tracker.domain.expenses.Expense;

import java.util.List;

public class ExpenseListFragment extends Fragment {

    FloatingActionButton fabAddExpense;
    private RecyclerView recyclerExpensesList;
    private ExpenseAdapter adapter;
    private ExpenseRepository expenseRepository;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_expense_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerExpensesList = view.findViewById(R.id.recyclerExpensesList);
        recyclerExpensesList.setLayoutManager(new LinearLayoutManager(getContext()));


        expenseRepository = new ExpenseRepository(getContext());

        List<Expense> expenses = expenseRepository.getAllExpenses();

        adapter = new ExpenseAdapter(expenses, expense -> {
            Bundle bundle = new Bundle();
            bundle.putInt("expense_id", expense.getId());

            ExpenseDetailFragment detailFragment = new ExpenseDetailFragment();
            detailFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.expensesFragmentContainer, detailFragment).addToBackStack("expense_detail").commit();
        });

        recyclerExpensesList.setAdapter(adapter);

        // Attach swipe-to-delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Expense deletedExpense = expenses.get(position);

                // Temporarily remove the expense
                expenses.remove(position);
                adapter.notifyItemRemoved(position);

                // Show snackbar with Undo option
                Snackbar.make(recyclerExpensesList, "Deleted: " + deletedExpense.getTitle(), Snackbar.LENGTH_LONG).setAction("Undo", v -> {
                    expenses.add(position, deletedExpense);
                    adapter.notifyItemInserted(position);
                }).addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        // If not undone, delete from DB
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            expenseRepository.deleteExpenseById(deletedExpense.getId());
                        }
                    }
                }).show();
            }
        }).attachToRecyclerView(recyclerExpensesList);

        fabAddExpense = view.findViewById(R.id.fabAddExpense);

        fabAddExpense.setOnClickListener(v -> {
            requireParentFragment().getChildFragmentManager().beginTransaction()
                    .replace(R.id.expensesFragmentContainer, new AddExpenseFragment())
                    .addToBackStack("expense_form")
                    .commit();
        });
    }
}
