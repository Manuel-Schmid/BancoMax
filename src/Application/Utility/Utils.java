package Application.Utility;

import java.math.BigInteger;

public class Utils {
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            BigInteger number = new BigInteger(strNum);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
