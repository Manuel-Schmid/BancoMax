package Application.Data;

import Application.Utility.Salutation;

public class Info {

    // user
    private static int userID;
    private static String firstName, lastName;
    private static Salutation salutation;
    // account
    private static int accountID;
    private static String IBAN, bank;
    // card
    private static int cardID;
    private static String cardNr, cardtype;

    public static void setInfo(String cardNr) {
        String[] cInfo = Database.getCardInfo(cardNr);
        if (cInfo != null) {
            setCardID(Integer.parseInt(cInfo[0]));
            setCardNr(cInfo[1]);
            setCardtype(cInfo[2]);
        }
        String[] uInfo = Database.getUserInfo(cardNr);
        if (uInfo != null) {
            setUserID(Integer.parseInt(uInfo[0]));
            setFirstName(uInfo[1]);
            setLastName(uInfo[2]);
            setSalutation(Salutation.valueOf(uInfo[3]));
        }
        String[] aInfo = Database.getAccountInfo(cardNr);
        if (aInfo != null) {
            setAccountID(Integer.parseInt(aInfo[0]));
            setIBAN(aInfo[1]);
            setBank(aInfo[2]);
        }
    }

    public static int getUserID() {
        return userID;
    }

    private static void setUserID(int userID) {
        Info.userID = userID;
    }

    public static String getFirstName() {
        return firstName;
    }

    private static void setFirstName(String firstName) {
        Info.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    private static void setLastName(String lastName) {
        Info.lastName = lastName;
    }

    public static Salutation getSalutation() {
        return salutation;
    }

    private static void setSalutation(Salutation salutation) {
        Info.salutation = salutation;
    }

    public static int getAccountID() {
        return accountID;
    }

    private static void setAccountID(int accountID) {
        Info.accountID = accountID;
    }

    public static String getIBAN() {
        return IBAN;
    }

    private static void setIBAN(String IBAN) {
        Info.IBAN = IBAN;
    }

    public static String getBank() {
        return bank;
    }

    private static void setBank(String bank) {
        Info.bank = bank;
    }

    public static int getCardID() {
        return cardID;
    }

    private static void setCardID(int cardID) {
        Info.cardID = cardID;
    }

    public static String getCardNr() {
        return cardNr;
    }

    private static void setCardNr(String cardNr) {
        Info.cardNr = cardNr;
    }

    public static String getCardtype() {
        return cardtype;
    }

    private static void setCardtype(String cardtype) {
        Info.cardtype = cardtype;
    }

    public static void logout() {
        userID = 0;
        firstName = "";
        lastName = "";
        accountID = 0;
        IBAN = "";
        bank = "";
        cardID = 0;
        cardNr = "";
        cardtype = "";
    }
}
