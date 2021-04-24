package Application.Utility;

import Application.Data.Transaction;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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

    public static int getMonthInt(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue();
    }

    public static String getFormattedMonth() {
        LocalDateTime time = LocalDateTime.now();
        return time.format(DateTimeFormatter.ofPattern("MMMM", Locale.GERMAN));
    }

    public static int getMonthDayCount() {
        LocalDateTime now = LocalDateTime.now();
        YearMonth yearMonthObject = YearMonth.of(now.getYear(), now.getMonth());
        return yearMonthObject.lengthOfMonth();
    }
}
