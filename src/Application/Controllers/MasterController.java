package Application.Controllers;

import Application.Data.Info;
import Application.Data.WithdrawalInfo;
import Application.Utility.Currency;
import Application.Utility.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class MasterController {

    @FXML
    private Label lblWelcome;

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
    private void onWithdrawExpress() {
        // WithdrawalInfo.getInstance().setCurrency(Currency.CHF);
        // !!!
    }
}
