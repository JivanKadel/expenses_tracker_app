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

public class LoginFragment extends Fragment {
    EditText etUsername, etPassword;
    Button btnLogin;

    UserRepository userRepository;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        userRepository = new UserRepository(getContext());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (!uname.trim().isEmpty() && !password.trim().isEmpty()) {
                    if (userRepository.loginUser(uname, password)) {
                        SessionManager sessionManager = new SessionManager(requireContext());
                        sessionManager.login(sessionManager.getUser());
                        Intent intent = new Intent(requireActivity(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    } else {
                        etUsername.setError("Invalid username or password");
                    }
                }else{
                    etUsername.setError("The fields are empty!");
                }

            }
        });

        TextView signupRedirect = view.findViewById(R.id.signupRedirect);
        String fullText = "No Account? Signup Now";
        String signupText = "Signup Now";
        SpannableString spannableText = getSpannableString(fullText, signupText);

        signupRedirect.setText(spannableText);
        signupRedirect.setMovementMethod(LinkMovementMethod.getInstance());
        signupRedirect.setHighlightColor(Color.TRANSPARENT);

    }

    @NonNull
    private SpannableString getSpannableString(String fullText, String signupText) {
        SpannableString spannableText = new SpannableString(fullText);

        int startIndex = fullText.indexOf(signupText);
        int endIndex = startIndex + signupText.length();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {

                if (getActivity() instanceof AuthActivity) {
                    AuthActivity authActivity = (AuthActivity) getActivity();
                    authActivity.switchToSignup();
                }
            }

        };
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableText;
    }
}