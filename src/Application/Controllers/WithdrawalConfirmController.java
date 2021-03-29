package Application.Controllers;

import Application.Data.WithdrawalInfo;
import Application.Utility.Navigation;
import Application.Utility.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class WithdrawalConfirmController {

    @FXML
    private Label lblCurrAmount;

    @FXML
    private void initialize() {
        lblCurrAmount.setText(WithdrawalInfo.getInstance().getCurrency() + " " + Utils.formatMoney(WithdrawalInfo.getInstance().getAmount()));
    }

    @FXML
    private void onBack() throws IOException {
        Navigation.switchToView("Withdrawal");
    }

    @FXML
    private void onCancel() throws IOException {
        Navigation.switchToView("Master");
    }

    @FXML
    private void confirm() {
        // error handling if moneyStock allows the withdrawal
    }

    @FXML
    private void confirmReceipt() {
        // error handling if moneyStock allows the withdrawal
        // print receipt
    }
}
