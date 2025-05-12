package com.jivan.expense_tracker.ui.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jivan.expense_tracker.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        loadFragment(new ExpensesFragment());

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavBar);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                int id = item.getItemId();
                if (id == R.id.nav_expenses) {
                    fragment = new ExpensesFragment();
                } else if (id == R.id.nav_reports) {
                    fragment = new ReportsFragment();
                } else if (id == R.id.nav_settings) {
                    fragment = new SettingsFragment();
                }
                return loadFragment(fragment);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            var padding = findViewById(R.id.main).getPaddingLeft();
            v.setPadding(padding, systemBars.top, padding, systemBars.bottom);
            return insets;
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flMainContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}