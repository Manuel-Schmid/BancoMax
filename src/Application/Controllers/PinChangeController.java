package Application.Controllers;

import Application.Data.Database;
import Application.Data.Info;
import Application.Utility.Navigation;
import Application.Utility.Security;
import Application.Utility.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class PinChangeController {

    @FXML
    private Label lblError, lblSuccess;
    @FXML
    private TextField tfCardNr;
    @FXML
    private PasswordField pfPIN, pfPINconfirm;
    @FXML
    private Button btnConfirm;
    @FXML
    private BorderPane root;

    private boolean success = false;

    @FXML
    private void initialize() {
        tfCardNr.setText(Info.getCardNr());
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        pfPIN.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                root.requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    @FXML
    private void onConfirm() {
        btnConfirm.requestFocus();
        lblSuccess.setVisible(false);
        if(pfPIN.getText().isEmpty() || pfPINconfirm.getText().isEmpty()) {
            lblError.setText("Bitte alle Felder ausfüllen!");
            lblError.setVisible(true);
        } else if (!Utils.isNumeric(pfPIN.getText()) || !Utils.isNumeric(pfPINconfirm.getText())) {
            lblError.setText("Falsches Format!");
            lblError.setVisible(true);
        } else if (!pfPIN.getText().equals(pfPINconfirm.getText())) {
            lblError.setText("Die PINs müssen übereinstimmen");
            lblError.setText("PINs stimmen nicht überein");
            lblError.setVisible(true);
        } else {
            try {
                byte[] PINsalt = Security.createSalt();
                byte[] PINhash = Security.hash(pfPIN.getText(), PINsalt);
                Database.changePIN(Info.getCardID(), PINhash, PINsalt);
                lblError.setVisible(false);
                lblSuccess.setVisible(true);
                success = true;
                pfPIN.clear();
                pfPINconfirm.clear();
            } catch (Exception e) {
                lblError.setText("Fehler!");
                lblError.setVisible(true);
            }
        }
    }

    public void onCancel() throws IOException {
        Navigation.switchToView("Master");
    }

    @FXML
    private void paneClicked() {
        hideSuccess();
    }

    private void hideSuccess() {
        if(success) {
            lblSuccess.setVisible(false);
            success = false;
        }
    }
}
