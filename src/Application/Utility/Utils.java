package Application.Utility;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

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

    public static String formatTimestamp(Timestamp tmstmp) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(tmstmp);
    }

    public static String formatMoney(double num) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(num);
    }

    public static String zeroHandling(char[] ca) {
        if (ca.length == 0) { return ""; }
        ArrayList<Character> charList = new ArrayList<>();
        boolean significant = false;
        for (char c : ca) {
            if (!significant) {
                if (c != '0') {
                    significant = true;
                    charList.add(c);
                }
            } else {
                charList.add(c);
            }
        }
        return charList.stream().map(Object::toString).collect(Collectors.joining());
    }

}
