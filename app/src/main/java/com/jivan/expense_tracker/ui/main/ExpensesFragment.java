package com.jivan.expense_tracker.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jivan.expense_tracker.R;
import com.jivan.expense_tracker.ui.expenses.AddExpenseFragment;
import com.jivan.expense_tracker.ui.expenses.ExpenseListFragment;


public class ExpensesFragment extends Fragment {


    public ExpensesFragment() {
        // Required empty public constructor
    }

    public static ExpensesFragment newInstance(String param1, String param2) {
        ExpensesFragment fragment = new ExpensesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        if (savedInstanceState == null) {
            loadChildFragment(new ExpenseListFragment());
        }

        return view;
    }

    private void loadChildFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.expensesFragmentContainer, fragment).addToBackStack(null).commit();
    }
}