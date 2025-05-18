package com.jivan.expense_tracker.util.format;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CurrencyFormatter {
    public Context context;
    private float currencyExchangeRate = 1.0f;
    private String currentCurrency = "eur";

    public float getCurrencyExchangeRate() {
        return currencyExchangeRate;
    }

    public void setCurrencyExchangeRate(float currencyExchangeRate) {
        this.currencyExchangeRate = currencyExchangeRate;
    }

    public CurrencyFormatter(Context context) {
        this.context = context;
    }

    public String getCurrentCurrency() {
        return currentCurrency;
    }

    public void setCurrentCurrency(String currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    public interface ExchangeRateCallback {
        void onRatesReceived(Map<String, Float> rates);
    }

    public void getExchangeRates(String url, ExchangeRateCallback callback) {
        Map<String, Float> euroBasedExchangeRates = new HashMap<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            JSONObject eurRates;
            try {
                eurRates = response.getJSONObject("eur");
                float inr = (float) eurRates.getDouble("inr");
                float npr = (float) eurRates.getDouble("npr");
                float usd = (float) eurRates.getDouble("usd");
                float jpy = (float) eurRates.getDouble("jpy");
                float eur = 1.0f; // Base currency
                euroBasedExchangeRates.put("eur", eur);
                euroBasedExchangeRates.put("inr", inr);
                euroBasedExchangeRates.put("npr", npr);
                euroBasedExchangeRates.put("usd", usd);
                euroBasedExchangeRates.put("jpy", jpy);

                callback.onRatesReceived(euroBasedExchangeRates);

            } catch (JSONException e) {
                Log.e("JSON EXCEPTION", "getExchangeRates: ", e);
                callback.onRatesReceived(null);
            }

        }, error -> {
            // Handle error
            Toast.makeText(context, "Error fetching currencies", Toast.LENGTH_SHORT).show();
            callback.onRatesReceived(null);
        });

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }

}
