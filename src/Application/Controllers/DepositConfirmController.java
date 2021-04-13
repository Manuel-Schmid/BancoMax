package Application.Controllers;

import Application.Data.*;
import Application.Utility.Currency;
import Application.Utility.Navigation;
import Application.Utility.Operation;
import Application.Utility.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class DepositConfirmController {

    @FXML
    private Label lblAmount, lblText, lblError;

    @FXML
    private void initialize() {
        lblText.setText("Sie sind dabei, den folgenden Betrag auf das Konto " + Info.getIBAN() + " zu überweisen");
        String formattedAmount = DepositInfo.getInstance().getCurrency() + " " + Utils.formatMoney(DepositInfo.getInstance().getAmount());
        lblAmount.setText(formattedAmount);
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
        DepositInfo.getInstance().setAmount(0);
        Navigation.switchToView("Master");
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
        Database.updateBalance(Operation.deposit, amountInCHF, Info.getAccountID()); // Einzahlung mit Konto verrechnen
        Database.updateMoneyStock(Operation.deposit, DepositInfo.getInstance().getBanknotes(), DepositInfo.getInstance().getCurrency()); // Einzahlung mit moneyStock verrechnen
        Database.insertTransaction(Operation.deposit, amountInCHF, Info.getCardID());
        Navigation.switchToView("TransactionSuccess");
    }
}
