package com.jivan.expense_tracker.util.reports;


import com.jivan.expense_tracker.domain.expenses.Expense;
import com.jivan.expense_tracker.util.Helper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    public static Map<String, Float> getCategoryWiseSummary(List<Expense> expenses) {
        Map<String, Float> summary = new HashMap<>();
        for (Expense e : expenses) {
            summary.put(e.getCategory().getName(), (float) (summary.getOrDefault(e.getCategory().getName(), 0f) + e.getAmount()));
        }
        return summary;
    }

    public static String getTopCategory(List<Expense> filteredExpenses) {
        Map<String, Float> categorySummary = getCategoryWiseSummary(filteredExpenses);

        if (categorySummary.isEmpty()) {
            return "No expenses recorded";
        }

        String topCategory = null;
        float maxAmount = -1;

        for (Map.Entry<String, Float> entry : categorySummary.entrySet()) {
            if (entry.getValue() > maxAmount) {
                maxAmount = entry.getValue();
                topCategory = entry.getKey();
            }
        }
        return topCategory;
    }


    public static Map<Integer, Float> getWeeklyReportData(List<Expense> weeklyExpenses) {
        Map<Integer, Float> dailyExpenses = new LinkedHashMap<>();

        for (Expense expense : weeklyExpenses) {
            // Convert to LocalDate (use helper if needed)
            LocalDate date = LocalDate.parse(Helper.dateToString(expense.getDate()));

            // Day of week: 1 = Monday, ..., 7 = Sunday
            int dayOfWeek = date.getDayOfWeek().getValue() - 1; // Make 0-based: 0 = Mon, 6 = Sun

            // Aggregate expense amount for the day
            float currentTotal = dailyExpenses.getOrDefault(dayOfWeek, 0f);
            dailyExpenses.put(dayOfWeek, currentTotal + (float) expense.getAmount());
        }

        return dailyExpenses; // Keys = 0 to 6, usable as X-axis positions
    }


    public static Map<Integer, Float> getMonthlyReportData(List<Expense> monthlyExpenses, YearMonth month) {
        Map<Integer, Float> weeklyExpenses = new LinkedHashMap<>();
        LocalDate firstDayOfMonth = month.atDay(1);
        LocalDate lastDayOfMonth = month.atEndOfMonth();

        LocalDate currentDay = firstDayOfMonth;
        int weekNumber = 0; // 0-based index

        while (!currentDay.isAfter(lastDayOfMonth)) {
            List<Expense> expensesInWeek = getWeeklyExpenses(monthlyExpenses, currentDay);
            weeklyExpenses.put(weekNumber, getTotal(expensesInWeek));
            currentDay = currentDay.plusWeeks(1);
            weekNumber++;
        }

        return weeklyExpenses; // keys = 0, 1, 2... usable in bar chart
    }

    public static float getTotal(List<Expense> expenses) {
        float total = 0;
        for (Expense e : expenses) {
            total += (float) e.getAmount();
        }
        return total;
    }


}
