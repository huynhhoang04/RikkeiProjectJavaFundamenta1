package util;

import java.util.regex.Pattern;

public class checkEmailValid {
    private static final String email_regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern email_pattern = Pattern.compile(email_regex);

    //kiểm tra email vói định dangk regex
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return email_pattern.matcher(email).matches();
    }
}
