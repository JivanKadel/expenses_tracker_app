package com.jivan.expense_tracker.util.reports;


import com.jivan.expense_tracker.domain.expenses.Expense;
import com.jivan.expense_tracker.util.Helper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportUtils {

    public static List<Expense> getDailyExpenses(List<Expense> allExpenses, LocalDate date) {
        return allExpenses.stream().filter(expense ->
                LocalDate.parse(Helper.dateToString(expense.getDate())).equals(date)
        ).collect(Collectors.toList());
    }

    public static List<Expense> getWeeklyExpenses(List<Expense> allExpenses, LocalDate dateInWeek) {
        LocalDate startOfWeek = dateInWeek.minusDays(dateInWeek.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return allExpenses.stream().filter(expense -> {
            LocalDate expenseDate = LocalDate.parse(Helper.dateToString(expense.getDate()));
            return !expenseDate.isBefore(startOfWeek) && !expenseDate.isAfter(endOfWeek);

        }).collect(Collectors.toList());
    }

    public static List<Expense> getMonthlyExpense(List<Expense> allExpenses, YearMonth month) {
        return allExpenses.stream().filter(
                expense -> YearMonth.from(LocalDate.parse(Helper.dateToString(expense.getDate()))).equals(month)
        ).collect(Collectors.toList());
    }

    public static Map<String, Float> getCategoryWiseSymmary(List<Expense> expenses) {
        Map<String, Float> summary = new HashMap<>();
        for (Expense e : expenses) {
            summary.put(e.getCategory().getName(), (float) (summary.getOrDefault(e.getCategory().getName(), 0f) + e.getAmount()));
        }
        return summary;
    }

    public static float getTotal(List<Expense> expenses) {
        float total = 0;
        for (Expense e : expenses) {
            total += (float) e.getAmount();
        }
        return total;
    }


}
