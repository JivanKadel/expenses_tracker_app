package com.jivan.expense_tracker.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButtonToggleGroup;

import com.jivan.expense_tracker.R;
import com.jivan.expense_tracker.data.expenses.ExpenseRepository;
import com.jivan.expense_tracker.domain.expenses.Expense;
import com.jivan.expense_tracker.util.reports.ReportUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ReportsFragment extends Fragment {

    ExpenseRepository expenseRepository;

    private PieChart pieChart;
    private BarChart barChart;
    private LineChart lineChart;
    private TextView tvTotalSpent, tvMostSpentCategory;
    private MaterialButtonToggleGroup toggleGroup;

    private SharedPreferences userPreferences;
    private String currency = "eur";
    private float exchangeRate = 1.0f;

    List<Expense> allExpenses;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toggleGroup = view.findViewById(R.id.reportToggleGroup);
        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);
        lineChart = view.findViewById(R.id.lineChart);
        tvTotalSpent = view.findViewById(R.id.tvTotalSpent);
        tvMostSpentCategory = view.findViewById(R.id.tvMostSpentCategory);

        if (getContext().getSharedPreferences("user_settings", Context.MODE_PRIVATE) != null) {

            userPreferences = getContext().getSharedPreferences("user_settings", Context.MODE_PRIVATE);
            exchangeRate = Float.parseFloat(userPreferences.getString("euro_base_exchange_rate", String.valueOf(exchangeRate)));
            currency = userPreferences.getString("currency", currency);
        }


        expenseRepository = new ExpenseRepository(getContext());
        allExpenses = expenseRepository.getAllExpenses();

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            if (checkedId == R.id.btnDaily) {
                loadDailyReport();
            } else if (checkedId == R.id.btnWeekly) {
                loadWeeklyReport();
            } else if (checkedId == R.id.btnMonthly) {
                loadMonthlyReport();
            }
        });

        toggleGroup.check(R.id.btnDaily);

    }

    private void loadDailyReport() {

        LocalDate today = LocalDate.now();
        List<Expense> dailyExpenses = ReportUtils.getDailyExpenses(allExpenses, today);
        float total = ReportUtils.getTotal(dailyExpenses);

        String topCategory = ReportUtils.getTopCategory(dailyExpenses);

        updateSummary(String.valueOf(total), topCategory);
        showPieChart(dailyExpenses);
        barChart.setVisibility(View.GONE);
        lineChart.setVisibility(View.GONE);

    }

    private void loadWeeklyReport() {
        LocalDate today = LocalDate.now();
        List<Expense> weeklyExpenses = ReportUtils.getWeeklyExpenses(allExpenses, today);
        float total = ReportUtils.getTotal(weeklyExpenses);

        String topCategory = ReportUtils.getTopCategory(weeklyExpenses);

        updateSummary(String.valueOf(total), topCategory);
        showPieChart(weeklyExpenses);
        showWeeklyBarChart(weeklyExpenses);
        showLineChart(weeklyExpenses);
    }

    private void loadMonthlyReport() {
        YearMonth thisMonth = YearMonth.now();
        List<Expense> monthlyExpenses = ReportUtils.getMonthlyExpense(allExpenses, thisMonth);
        float total = ReportUtils.getTotal(monthlyExpenses);

        String topCategory = ReportUtils.getTopCategory(monthlyExpenses);

        updateSummary(String.valueOf(total), topCategory);
        showPieChart(monthlyExpenses);
        showMonthlyBarChart(monthlyExpenses);
        showLineChart(monthlyExpenses);

    }

    private void updateSummary(String total, String category) {
        tvTotalSpent.setText(String.format("Total Spent: " + currency + " " + Float.parseFloat(total) * exchangeRate));
        tvMostSpentCategory.setText(String.format("Top Category: %s", category));
    }

    private void showPieChart(List<Expense> filteredExpenses) {

        Map<String, Float> categoryWiseSummary = ReportUtils.getCategoryWiseSummary(filteredExpenses);
        List<PieEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Float> entry : categoryWiseSummary.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieChart.setData(new PieData(dataSet));

        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }


    private void showWeeklyBarChart(List<Expense> weeklyExpenses) {
        Map<Integer, Float> dailyData = ReportUtils.getWeeklyReportData(weeklyExpenses);

        List<BarEntry> entries = new ArrayList<>();
        for (Map.Entry<Integer, Float> entry : dailyData.entrySet()) {
            entries.add(new BarEntry(entry.getKey(), entry.getValue()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Daily Expenses");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.teal_700));
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // Set bar width

        barChart.setVisibility(View.VISIBLE);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        // Set custom day labels
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            private final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return (index >= 0 && index < days.length) ? days[index] : "";
            }
        });

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setDrawGridLines(false);

        barChart.invalidate(); // Refresh
    }

    private void showMonthlyBarChart(List<Expense> monthlyExpenses) {
        YearMonth thisMonth = YearMonth.now();
        Map<Integer, Float> weeklyData = ReportUtils.getMonthlyReportData(monthlyExpenses, thisMonth);

        List<BarEntry> entries = new ArrayList<>();
        for (Map.Entry<Integer, Float> entry : weeklyData.entrySet()) {
            entries.add(new BarEntry(entry.getKey(), entry.getValue()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Weekly Totals");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.teal_700));
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setVisibility(View.VISIBLE);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        // X Axis configuration
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // Set custom week labels
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "Week " + ((int) value + 1);
            }
        });

        barChart.invalidate(); // Refresh the chart
    }


    private void showLineChart(List<Expense> filteredExpenses) {
        lineChart.setVisibility(View.VISIBLE);
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < filteredExpenses.size(); i++) {
            entries.add(new Entry(i, (float) filteredExpenses.get(i).getAmount()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Expenses Trend");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.RED);
        lineChart.setData(new LineData(dataSet));
        lineChart.getDescription().setEnabled(false);

        lineChart.invalidate();
    }
}