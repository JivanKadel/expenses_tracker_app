package com.jivan.expense_tracker.ui.auth;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jivan.expense_tracker.R;

public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        assert container != null;
        TextView signupRedirect = view.findViewById(R.id.signupRedirect);
        String fullText = "No Account? Signup Now";
        String signupText = "Signup Now";
        SpannableString spannableText = getSpannableString(fullText, signupText);

        signupRedirect.setText(spannableText);
        signupRedirect.setMovementMethod(LinkMovementMethod.getInstance());
        signupRedirect.setHighlightColor(Color.TRANSPARENT);

        return view;
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