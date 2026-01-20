package util;

import java.util.regex.Pattern;

public class checkPhoneValid {
    private static final String phone_regex = "^(03|05|07|08|09)\\d{8}$";
    private static final Pattern phone_pattern = Pattern.compile(phone_regex);

    public static boolean checkPhone(String phone) {
        if (phone.length() != 10) {
            return false;
        }
        return phone_pattern.matcher(phone).matches();
    }
}

