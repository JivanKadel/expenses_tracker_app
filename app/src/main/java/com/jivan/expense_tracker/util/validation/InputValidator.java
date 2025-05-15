package com.jivan.expense_tracker.util.validation;

import android.util.Patterns;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InputValidator {
    private static final int MIN_LENGTH = 6;
    private static final Set<String> COMMON_PASSWORDS = new HashSet<>(Arrays.asList(
            "password", "123456", "qwerty", "admin", "secret", "admin", "user", "admin123", "user123", "nepal123", "admin@123", "user@123"// Add more common passwords
    ));
    public static String passwordError = "";
    public static String passwordWarning = "";

    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]{3,15}$");
    }

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null) {
            passwordError = "The password field is empty!";
            return false;
        }

        // 1. Check Minimum Length
        if (password.length() < MIN_LENGTH) {
            passwordError = "Password must be at least " + MIN_LENGTH + " chars long!";
            return false;
        }

        // 2. Don't Accept Common Passwords
        if (COMMON_PASSWORDS.contains(password.toLowerCase())) {
            passwordError = "Please avoid common, unsafe passwords";
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        String specialCharacters = "@#$%^&+=!";

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialCharacters.contains(String.valueOf(c))) {
                hasSpecial = true;
            }
        }

        if (!hasLetter || !hasDigit || !hasSpecial) {
            passwordWarning = "Password with letters, digits, special chars is recommended";
        }

        return true;
    }

    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }
}
