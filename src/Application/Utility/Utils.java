package Application.Utility;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Control;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.PrintWriter;
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
import java.util.Scanner;
import java.util.stream.Collectors;

public class Utils {

    public static FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");

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

    public static void moveFocus(Control from, BorderPane to) {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        from.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                to.requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    public static String read(File file) {
        try {
            Scanner sc = new Scanner(file);
            StringBuilder sb = new StringBuilder();
            while(sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            sc.close();
            return String.valueOf(sb);
        } catch (Exception e) {
            return "";
        }
    }

    public static void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static StringConverter<Double> getStringConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Double d) {
                if (d < 16.5) {
                    return "10er";
                } else if (d < 49.5) {
                    return "20er";
                } else if (d < 82.5) {
                    return "50er";
                } else if (d <= 100) {
                    return "Alle";
                }
                return null;
            }

            @Override
            public Double fromString(String string) {
                return switch (string) {
                    case "10er" -> 1d;
                    case "20er" -> 2d;
                    case "50er" -> 3d;
                    case "Alle" -> 4d;
                    default -> null;
                };
            }
        };
    }

    public static String colorToHex(Color color) {
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }
}
