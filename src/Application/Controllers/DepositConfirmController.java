package Application.Controllers;

import Application.Data.*;
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

public class DepositConfirmController {

    @FXML
    private Label lblAmount, lblText;
    @FXML
    private Button btnBack;
    @FXML
    private BorderPane root;

    @FXML
    private void initialize() {
        if (DepositInfo.getInstance().isAdmin()) {
            lblText.setText("Sie sind dabei, den BancoMax-Automaten als Administrator um den folgenden Betrag aufzufüllen:");
        } else {
            lblText.setText("Sie sind dabei, den folgenden Betrag auf das Konto " + Info.getIBAN() + " zu überweisen:");
        }
        String formattedAmount = DepositInfo.getInstance().getCurrency() + " " + Utils.formatMoney(DepositInfo.getInstance().getAmount());
        lblAmount.setText(formattedAmount);
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
        DepositInfo.getInstance().setAmount(0);
        if (DepositInfo.getInstance().getCurrency() == Currency.CHF) {
            Navigation.switchToView("DepositCHF");
        } else {
            Navigation.switchToView("DepositEuro");
        }
    }

    @FXML
    private void onCancel() throws IOException {
        if (DepositInfo.getInstance().isAdmin()) {
            DepositInfo.getInstance().setAdmin(false);
            Navigation.switchToView("Admin");
        } else {
            Navigation.switchToView("Master");
        }
        DepositInfo.getInstance().setAmount(0);
    }

    @FXML
    private void confirm() throws Exception {
        double amount = DepositInfo.getInstance().getAmount();
        double amountInCHF;
        if (DepositInfo.getInstance().getCurrency() == Currency.Euro) { // Euro -> CHF
            Double exRate = CurrencyAPI.getExRate();
            amountInCHF = amount * exRate;
        } else {
            amountInCHF = amount;
        }
        Database.updateMoneyStock(Operation.deposit, DepositInfo.getInstance().getBanknotes(), DepositInfo.getInstance().getCurrency()); // Einzahlung mit moneyStock verrechnen
        if (!DepositInfo.getInstance().isAdmin()) {
            Database.updateBalance(Operation.deposit, amountInCHF, Info.getAccountID()); // Einzahlung mit Konto verrechnen
            Database.insertTransaction(Operation.deposit, DepositInfo.getInstance().getCurrency(), amount, Info.getCardID()); // Transaktion verbuchen
            Navigation.switchToView("TransactionSuccess");
        } else {
            DepositInfo.getInstance().setAdmin(false);
            AdminController.setWasSuccessful(true);
            Navigation.switchToView("Admin");
        }

    }
}
