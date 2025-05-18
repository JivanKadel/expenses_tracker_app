package com.jivan.expense_tracker.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.jivan.expense_tracker.R;
import com.jivan.expense_tracker.ui.auth.AuthActivity;
import com.jivan.expense_tracker.util.format.CurrencyFormatter;

import java.util.Map;


public class SettingsFragment extends Fragment {

    private Spinner spinnerCurrency;
    private Button btnLogout;
    private SharedPreferences preferences;

    Map<String, Float> exchangeRates;


    private static final String[] currencies = {"NPR", "USD", "EUR", "INR", "JPY"};
    public static final String URL = "https://latest.currency-api.pages.dev/v1/currencies/eur.json";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerCurrency = view.findViewById(R.id.spinnerCurrency);
        btnLogout = view.findViewById(R.id.btnLogout);

        preferences = requireContext().getSharedPreferences("user_settings", Context.MODE_PRIVATE);

        // Setup Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);

        CurrencyFormatter cf = new CurrencyFormatter(requireContext());
        cf.getExchangeRates(URL, rates -> {
            if (rates != null) {
                exchangeRates = rates;
            } else {
                Log.e("Error", "Looks like the rates is null!");
            }
        });

        // Set selected currency
        String currentCurrency = preferences.getString("currency", "NPR");
        int position = adapter.getPosition(currentCurrency);
        spinnerCurrency.setSelection(position);


        // Save currency change
        spinnerCurrency.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int pos, long id) {

                String selected = currencies[pos];
                if (exchangeRates != null) {
                    cf.setCurrencyExchangeRate(exchangeRates.get(selected.toLowerCase()));
                    cf.setCurrentCurrency(selected.toLowerCase());
                    preferences.edit().putString("currency", selected).apply();
                    preferences.edit().putString("euro_base_exchange_rate", String.valueOf(cf.getCurrencyExchangeRate())).apply();
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        // Logout logic
        btnLogout.setOnClickListener(v -> {
            preferences.edit().clear().apply(); // or selectively clear
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }
}