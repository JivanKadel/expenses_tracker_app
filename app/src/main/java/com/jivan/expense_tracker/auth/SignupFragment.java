package com.jivan.expense_tracker.auth;

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

public class SignupFragment extends Fragment {


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
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        assert container != null;
        TextView loginRedirect = view.findViewById(R.id.loginRedirect);
        String fullText = "Already have an Account? Login instead";
        String loginText = "Login instead";

        SpannableString spannableText = getSpannableTextString(fullText, loginText);
        loginRedirect.setText(spannableText);
        loginRedirect.setMovementMethod(LinkMovementMethod.getInstance());
        loginRedirect.setHighlightColor(Color.TRANSPARENT);

        return view;
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