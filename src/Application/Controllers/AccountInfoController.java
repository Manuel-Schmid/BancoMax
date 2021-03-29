package Application.Controllers;

import Application.Data.Database;
import Application.Data.Info;
import Application.Utility.Navigation;
import Application.Utility.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class AccountInfoController {

    @FXML
    private Label lblAccount, lblBank, lblBalanceDate, lblBalance;

    @FXML
    private void initialize() {
        lblAccount.setText("Konto: " + Info.getIBAN());
        lblBank.setText("Bank: " + Info.getBank());
        lblBalanceDate.setText("Aktueller Saldo per " + Utils.getCurrentTimeStamp("dd.MM.yyyy")); // !!!
        lblBalance.setText("CHF " + Utils.formatMoney(Database.getBalance(Info.getCardNr()))); // !!!
    }

    @FXML
    private void onPrintReceipt() {
        // print receipt
    }

    @FXML
    private void onLastTransactions() {
        // show last transactions
    }

    @FXML
    private void toMaster() throws IOException {
        Navigation.switchToView("Master");
    }

}