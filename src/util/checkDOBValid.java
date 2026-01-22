package util;

import java.util.regex.Pattern;

public class checkDOBValid {
    private static final String dob_regex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
    private static final Pattern dob_pattern = Pattern.compile(dob_regex);

    //kiểm tra dob vói định dangk regex
    public static boolean isValidDOB(String dob) {
        if (dob == null) {
            return false;
        }
        return dob_pattern.matcher(dob).matches();
    }
}
