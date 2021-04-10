package Application.Controllers;

import Application.Data.Database;
import Application.Data.Info;
import Application.Data.WithdrawalInfo;
import Application.Utility.Currency;
import Application.Utility.Navigation;
import Application.Utility.Operation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class MasterController {

    @FXML
    private Label lblWelcome, lblError;

    String welcome = "Welcome " + Info.getSalutation() + " " + Info.getLastName();

    public void onBackToLogin(ActionEvent actionEvent) throws IOException {
        Navigation.switchToView("Login");
    }

    @FXML
    private void initialize() {
        lblWelcome.setText("Willkommen " + Info.getSalutation() + " " + Info.getLastName());
    }

    @FXML
    private void onShowAccountInfo() throws IOException {
        Navigation.switchToView("AccountInfo");
    }

    @FXML
    private void onPinChange() throws IOException {
        Navigation.switchToView("PinChange");
    }

    @FXML
    private void onWithdrawCHF() throws IOException {
        WithdrawalInfo.getInstance().setCurrency(Currency.CHF);
        Navigation.switchToView("Withdrawal");
    }

    @FXML
    private void onWithdrawEuro() throws IOException {
        WithdrawalInfo.getInstance().setCurrency(Currency.Euro);
        Navigation.switchToView("Withdrawal");
    }

    @FXML
    private void onWithdrawExpress() throws IOException {
        WithdrawalInfo.getInstance().setCurrency(Currency.CHF);
        WithdrawalInfo.getInstance().setAmount(50);
        int[] banknotes = {0, 0, 0, 1, 0, 0};
        boolean enoughInStock = Database.checkMoneystock(banknotes, WithdrawalInfo.getInstance().getCurrency().toString()); // error handling if moneyStock allows the withdrawal
        double amountInCHF = WithdrawalInfo.getInstance().getAmount();
        boolean enoughBalance = (Database.getBalance(Info.getAccountID()) >= amountInCHF);
        if (!enoughInStock) {
            lblError.setText("Der Automat hat nicht mehr genügend Noten für Ihre Anfrage! Probieren Sie es später erneut.");
            lblError.setVisible(true);
        } else if (!enoughBalance) {
            lblError.setText("Ihr Kontostand ist zu niedrig für diesen Bezug!");
            lblError.setVisible(true);
        } else {
            Database.updateBalance(Operation.withdraw, amountInCHF, Info.getAccountID()); // Abzug vom Konto
            Database.updateMoneyStock(Operation.withdraw, banknotes, Currency.CHF); // Abzug vom MoneyStock

            // Auszahlung & Transaktion erfolgreich
            System.out.println("Sie heben einen Betrag von " + WithdrawalInfo.getInstance().getAmount() + " " + WithdrawalInfo.getInstance().getCurrency() + " ab.");
            Navigation.switchToView("TransactionSuccess");
        }
    }
}
