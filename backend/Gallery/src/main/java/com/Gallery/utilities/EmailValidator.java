package com.Gallery.utilities;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String regexPattern = "^(.+)@(\\S+)$";

    public static boolean validateEmail(String email) {
        return email != null && Pattern.compile(regexPattern).matcher(email).matches();
    }


}
