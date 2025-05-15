package com.jivan.expense_tracker.util.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "session_user";
    private static final String KEY_USER = "username";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void login(String username) {
        editor.putString(KEY_USER, username);
        editor.apply();
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.contains(KEY_USER);
    }

    public String getUser(){
        return sharedPreferences.getString(KEY_USER, null);
    }
}
