package Application.Data;

import Application.Utility.Currency;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class Transaction extends RecursiveTreeObject<Transaction> {

    private final SimpleStringProperty date;
    private final SimpleStringProperty action;
    private final SimpleStringProperty currency;
    private final SimpleStringProperty amount;

    Transaction (String ndate, String naction, String ncurrency, String namount){
        this.date = new SimpleStringProperty(ndate);
        this.action = new SimpleStringProperty(naction);
        this.currency = new SimpleStringProperty(ncurrency);
        this.amount = new SimpleStringProperty(namount);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String nDate) {
        date.set(nDate);
    }

    public String getAction() {
        return action.get();
    }

    public void setAction(String nAction) {
        date.set(nAction);
    }

    public String getAmount() {
        return amount.get();
    }

    public void setAmount(String nAmount) {
        date.set(nAmount);
    }

    public String getCurrency() {
        return currency.get();
    }

    public void setCurrency(String nCurrency) {
        currency.set(nCurrency);
    }
}
