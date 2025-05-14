package com.jivan.expense_tracker.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jivan.expense_tracker.R;
import com.jivan.expense_tracker.data.auth.UserRepository;
import com.jivan.expense_tracker.ui.main.MainActivity;
import com.jivan.expense_tracker.util.auth.SessionManager;
import com.jivan.expense_tracker.util.validation.InputValidator;

public class SignupFragment extends Fragment {
    private EditText etUsername, etEmail, etPassword, etConfirmedPassword;
    private Button btnSignupUser;

    UserRepository userRepository;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmedPassword = view.findViewById(R.id.etConfirmedPassword);

        userRepository = new UserRepository(getContext());

        btnSignupUser = view.findViewById(R.id.btnSignupUser);

        TextView loginRedirect = view.findViewById(R.id.loginRedirect);
        String fullText = "Already have an Account? Login instead";
        String loginText = "Login instead";

        SpannableString spannableText = getSpannableTextString(fullText, loginText);
        loginRedirect.setText(spannableText);
        loginRedirect.setMovementMethod(LinkMovementMethod.getInstance());
        loginRedirect.setHighlightColor(Color.TRANSPARENT);

        btnSignupUser.setOnClickListener(v -> {
            // get values when button clicked
            String uname = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String passwordConfirmed = etConfirmedPassword.getText().toString();

            // validations using helper class

            if (!InputValidator.isValidUsername(uname)) {
                etUsername.setError("Username invalid. Use letters, numbers, underscore.");
            } else if (!InputValidator.isValidEmail(email)) {
                etEmail.setError("Invalid Email.");
            } else if (!InputValidator.isValidPassword(password)) {
                etPassword.setError(InputValidator.passwordError);
            } else if (!InputValidator.doPasswordsMatch(password, passwordConfirmed)) {
                etPassword.setError("Passwords do not match");
            } else {

                if (userRepository.registerUser(uname, email, password)) {
                    SessionManager sessionManager = new SessionManager(requireContext());
                    sessionManager.login(sessionManager.getUser());
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    etUsername.setError("User exists");
                }
            }
        });
    }

    private SpannableString getSpannableTextString(String fullText, String loginText) {
        SpannableString spannableText = new SpannableString(fullText);

        int startIndex = fullText.indexOf(loginText);
        int endIndex = startIndex + loginText.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                if (getActivity() instanceof AuthActivity) {
                    AuthActivity authActivity = (AuthActivity) getActivity();
                    authActivity.switchToLogin();
                }
            }
        };
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableText;
    }
}