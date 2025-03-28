package com.Gallery.utilities;

public class PasswordValidator {

    private static final String regexPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=]).{8,}$";
    public static boolean validatePassword(String password) {
        return password.matches(regexPattern);
    }
}
