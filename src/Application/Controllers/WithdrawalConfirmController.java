package Application.Controllers;

import Application.Data.CurrencyAPI;
import Application.Data.Database;
import Application.Data.Info;
import Application.Data.WithdrawalInfo;
import Application.Utility.Currency;
import Application.Utility.Navigation;
import Application.Utility.Operation;
import Application.Utility.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class WithdrawalConfirmController {

    @FXML
    private Label lblCurrAmount, lblError;
    @FXML
    private Button btnBack;
    @FXML
    private BorderPane root;

    private String formattedAmount;

    @FXML
    private void initialize() {
        formattedAmount = WithdrawalInfo.getInstance().getCurrency() + " " + Utils.formatMoney(WithdrawalInfo.getInstance().getAmount());
        lblCurrAmount.setText(formattedAmount);
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        btnBack.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                root.requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    @FXML
    private void onBack() throws IOException {
        Navigation.switchToView("Withdrawal");
    }

    @FXML
    private void onCancel() throws IOException {
        WithdrawalInfo.getInstance().setAmount(0);
        Navigation.switchToView("Master");
    }

    @FXML
    private void confirm() throws Exception {
        withdraw();
    }

    @FXML
    private void confirmReceipt() throws Exception {
        withdraw();
        // print receipt !!!
    }

    private void withdraw() throws IOException {
        lblError.setVisible(false);
        int[] banknotes = payout((int) WithdrawalInfo.getInstance().getAmount());
        boolean enoughInStock = Database.checkMoneystock(banknotes, WithdrawalInfo.getInstance().getCurrency().toString()); // error handling if moneyStock allows the withdrawal
        double amount = WithdrawalInfo.getInstance().getAmount();
        double amountInCHF;
        if (WithdrawalInfo.getInstance().getCurrency() == Currency.Euro) { // Euro -> CHF
            Double exRate = CurrencyAPI.getExRate();
            amountInCHF = amount * exRate;
        } else {
            amountInCHF = amount;
        }
        boolean enoughBalance = (Database.getBalance(Info.getAccountID()) >= amountInCHF);
        System.out.println(enoughBalance);
        if (!enoughInStock) {
            lblError.setText("Der Automat hat nicht mehr genügend Noten für Ihre Anfrage, probieren Sie es später erneut.         Wir entschuldigen uns für die Unannehmlichkeiten.");
            lblError.setVisible(true);
        } else if (!enoughBalance) {
            lblError.setText("Ihr Kontostand ist zu niedrig für diesen Bezug!");
            lblError.setVisible(true);
        } else {
            Database.updateBalance(Operation.withdraw, amountInCHF, Info.getAccountID()); // Abzug vom Konto
            Database.updateMoneyStock(Operation.withdraw, banknotes, WithdrawalInfo.getInstance().getCurrency()); // Abzug vom MoneyStock
            Database.insertTransaction(Operation.withdraw, amountInCHF, Info.getCardID());
            // Auszahlung
            printWithdrawal(banknotes);
            Navigation.switchToView("TransactionSuccess");
        }
    }

    private int[] payout(int restAmount) {
        int thousand = 0;
        if (WithdrawalInfo.getInstance().getCurrency() == Currency.CHF) { // thousand bank note only if CHF
            thousand = restAmount / 1000; restAmount = restAmount % 1000;
        }
        int twoHundred = restAmount / 200; restAmount = restAmount % 200;
        int hundred = restAmount / 100; restAmount = restAmount % 100;
        int fifty = restAmount / 50; restAmount = restAmount % 50;
        int twenty = restAmount / 20; restAmount = restAmount % 20;
        int ten = restAmount / 10;

        return new int[] { thousand, twoHundred, hundred, fifty, twenty, ten };
    }

    private void printWithdrawal(int[] payout) {
        System.out.println("Sie heben einen Betrag von " + formattedAmount + " " + WithdrawalInfo.getInstance().getCurrency() + " ab.");
        System.out.println("Notenausgabe: ");
        if (payout[0] > 0){
            System.out.println("1000er: " + payout[0]);
        }
        if (payout[1] > 0){
            System.out.println("200er: " + payout[1]);
        }
        if (payout[2] > 0){
            System.out.println("100er: " + payout[2]);
        }
        if (payout[3] > 0){
            System.out.println("50er: " + payout[3]);
        }
        if (payout[4] > 0){
            System.out.println("20er: " + payout[4]);
        }
        if (payout[5] > 0){
            System.out.println("10er: " + payout[5]);
        }
    }
}
