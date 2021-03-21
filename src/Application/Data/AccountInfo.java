package Application.Data;

import Application.Utility.Salutation;

public class AccountInfo {

    private static int accountID;
    private static String IBAN;
    private static String bank;

    public static void setAccount(String cardNr) {
        String[] info = Database.getAccountInfo(cardNr);
        if (info != null) {
            setAccountID(Integer.parseInt(info[0]));
            setIBAN(info[1]);
            setBank(info[2]);
        }
    }

    public static int getAccountID() {
        return accountID;
    }

    private static void setAccountID(int accountID) {
        AccountInfo.accountID = accountID;
    }

    public static String getIBAN() {
        return IBAN;
    }

    private static void setIBAN(String IBAN) {
        AccountInfo.IBAN = IBAN;
    }

    public static String getBank() {
        return bank;
    }

    private static void setBank(String bank) {
        AccountInfo.bank = bank;
    }
}
