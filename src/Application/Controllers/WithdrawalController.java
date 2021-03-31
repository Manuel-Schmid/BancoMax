package Application.Controllers;

import Application.Data.WithdrawalInfo;
import Application.Utility.Navigation;
import Application.Utility.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;

import java.io.IOException;

public class WithdrawalController {

    @FXML
    private Button btnConfirm;
    @FXML
    private Label lblSuccess, lblError, lblCurrency;
    @FXML
    private TextField tfAmount;
    private boolean success = false;

    @FXML
    private void initialize() {
        lblCurrency.setText("Währung: " + WithdrawalInfo.getInstance().getCurrency().toString());
    }

    @FXML
    private void toMaster() throws IOException {
        WithdrawalInfo.getInstance().setAmount(0);
        Navigation.switchToView("Master");
    }

    @FXML
    private void onConfirm() throws IOException {
        Navigation.switchToView("WithdrawalConfirm");
    }

    @FXML
    private void setAmount(ActionEvent event) {
        hideSuccess();
        String s = ((Labeled)event.getTarget()).getText();
        s = s.replace("'", "");
        int i = Integer.parseInt(s);
        WithdrawalInfo.getInstance().setAmount(i);
        btnConfirm.setDisable(false);
    }

    private void hideSuccess() {
        if(success) {
            lblSuccess.setVisible(false);
            success = false;
        }
    }

    @FXML
    private void setCustomAmount() {
        hideSuccess();
        if (tfAmount.getText().isEmpty()) {
            lblError.setText("Betrag eingeben!");
            btnConfirm.setDisable(true);
            lblError.setVisible(true);
        } else if (!Utils.isNumeric(tfAmount.getText())) {
            lblError.setText("Falsches Format!");
            btnConfirm.setDisable(true);
            lblError.setVisible(true);
        } else {
            boolean isInt = false;
            int cusAmount = 0;
            try {
                cusAmount = Integer.parseInt(tfAmount.getText());
                isInt = true;
            } catch (NumberFormatException e) {
                lblError.setText("Nur Ganzzahlen verwenden!");
                btnConfirm.setDisable(true);
                lblError.setVisible(true);
            }
            if (isInt) {
                if (cusAmount > 2000) { // Amount greater than 2000
                    lblError.setText("Höchstbetrag: 2000");
                    btnConfirm.setDisable(true);
                    lblError.setVisible(true);
                } else if (cusAmount % 10 != 0) { // Amount is not divisible by 10
                    lblError.setText("Betrag nicht in Noten auszahlbar!");
                    btnConfirm.setDisable(true);
                    lblError.setVisible(true);
                } else { // Success
                    WithdrawalInfo.getInstance().setAmount(cusAmount);
                    btnConfirm.setDisable(false);
                    lblError.setVisible(false);
                    lblSuccess.setVisible(true);
                    success = true;
                }
            }
        }
    }
}
