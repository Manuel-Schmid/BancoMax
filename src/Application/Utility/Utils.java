package Application.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double number = Double.parseDouble(strNum);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getCurrentTimeStamp(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date()); // dd.MM.yyyy HH:mm
    }
}
