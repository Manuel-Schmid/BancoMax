package Application.Data;

import Application.Utility.Currency;

import java.util.Arrays;

public class DepositInfo { // Singleton

    private DepositInfo() {}

    private static DepositInfo dp = new DepositInfo();

    public static DepositInfo getInstance() {
        return dp;
    }

    // default values
    private int amount = 0;
    private int[] banknotes = new int[6];
    private Currency currency = Currency.CHF;

    private boolean isAdmin = false;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int[] getBanknotes() {
        return banknotes;
    }

    public void setBanknotes(int[] banknotes) {
        this.banknotes = banknotes;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
