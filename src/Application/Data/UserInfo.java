package Application.Data;

import Application.Utility.Salutation;

public class UserInfo {

    private static int userID;
    private static String firstName;
    private static String lastName;
    private static Salutation salutation;

    public static void setUser(String cardNr) {
        String[] info = Database.getUserInfo(cardNr);
        if (info != null) {
            setUserID(Integer.parseInt(info[0]));
            setFirstName(info[1]);
            setLastName(info[2]);
            setSalutation(Salutation.valueOf(info[3]));
        }
    }

    private static void setUserID(int userID) {
        UserInfo.userID = userID;
    }

    public static String getFirstName() {
        return firstName;
    }

    private static void setFirstName(String firstName) {
        UserInfo.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    private static void setLastName(String lastName) {
        UserInfo.lastName = lastName;
    }

    public static Salutation getSalutation() {
        return salutation;
    }

    private static void setSalutation(Salutation salutation) {
        UserInfo.salutation = salutation;
    }

    public int getUserID() {
        return userID;
    }

}
