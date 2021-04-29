package Application.Data;

import Application.Utility.Currency;

public class WithdrawalInfo { // Singleton

    private WithdrawalInfo(){}

    private static final WithdrawalInfo wd = new WithdrawalInfo();

    public static WithdrawalInfo getInstance() {
        return wd;
    }

    // default values
    private int noteSize = 0;
    private double amount = 0;
    private Currency currency = Currency.CHF;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getNoteSize() {
        return noteSize;
    }

    public void setNoteSize(int noteSize) {
        this.noteSize = noteSize;
    }
}
