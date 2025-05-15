package com.jivan.expense_tracker.ui.main;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButtonToggleGroup;

import com.jivan.expense_tracker.R;

import java.util.ArrayList;
import java.util.List;


public class ReportsFragment extends Fragment {


    private PieChart pieChart;
    private BarChart barChart;
    private LineChart lineChart;
    private TextView tvTotalSpent, tvMostSpentCategory;
    private MaterialButtonToggleGroup toggleGroup;

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
        updateSummary("1200", "Food");
        showPieChart();
        showBarChart();
        lineChart.setVisibility(View.GONE);
    }

    private void loadWeeklyReport() {
        updateSummary("5400", "Travel");
        showPieChart();
        showBarChart();
        showLineChart();
    }

    private void loadMonthlyReport() {
        updateSummary("13200", "Shopping");
        showPieChart();
        showBarChart();
        showLineChart();
    }

    private void updateSummary(String total, String category) {
        tvTotalSpent.setText("Total Spent: Rs." + total);
        tvMostSpentCategory.setText("Top Category: " + category);
    }

    private void showPieChart() {

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(500, "Food"));
        entries.add(new PieEntry(300, "Transport"));
        entries.add(new PieEntry(400, "Shopping"));
        entries.add(new PieEntry(500, "Dining"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieChart.setData(new PieData(dataSet));

        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }

    private void showBarChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 300));
        entries.add(new BarEntry(1, 500));
        entries.add(new BarEntry(2, 400));
        entries.add(new BarEntry(3, 700));

        BarDataSet set = new BarDataSet(entries, "Days");
        barChart.setData(new BarData(set));
        barChart.getDescription().setEnabled(false);

        barChart.invalidate();
    }

    private void showLineChart() {
        lineChart.setVisibility(View.VISIBLE);
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 100));
        entries.add(new Entry(1, 200));
        entries.add(new Entry(2, 150));
        entries.add(new Entry(3, 300));
        entries.add(new Entry(4, 400));
        entries.add(new Entry(5, 200));
        entries.add(new Entry(6, 500));

        LineDataSet dataSet = new LineDataSet(entries, "Expenses Trend");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.RED);
        lineChart.setData(new LineData(dataSet));
        lineChart.getDescription().setEnabled(false);

        lineChart.invalidate();
    }
}