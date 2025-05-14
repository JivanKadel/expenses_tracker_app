package com.jivan.expense_tracker.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.jivan.expense_tracker.R;
import com.jivan.expense_tracker.ui.main.MainActivity;
import com.jivan.expense_tracker.util.auth.SessionManager;

public class AuthActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnSignup;
    private TextView formTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        formTitle = findViewById(R.id.formTitle);

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.authRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            var padding = findViewById(R.id.authRoot).getPaddingLeft();
            v.setPadding(padding, systemBars.top, padding, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnLogin.setOnClickListener(v -> {
            loadFragment(new LoginFragment());
            updataUI(true);
        });

        btnSignup.setOnClickListener(v -> {
            loadFragment(new SignupFragment());
            updataUI(false);
        });

        // load login form by default
        btnLogin.performClick();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void updataUI(Boolean isLogin) {
        btnLogin.setSelected(isLogin);
        btnSignup.setSelected(!isLogin);
        formTitle.setText(isLogin ? "Login Form" : "Signup Form");

    }

    public void switchToSignup() {
        btnSignup.performClick();
    }

    public void switchToLogin() {
        btnLogin.performClick();
    }
}