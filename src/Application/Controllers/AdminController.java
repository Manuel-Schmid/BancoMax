package Application.Controllers;

import Application.Data.Database;
import Application.Data.DepositInfo;
import Application.Utility.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class AdminController {

    // Account
    @FXML
    private TextField tfIBAN, tfBalance, tfBank, tfUserID;
    // User
    @FXML
    private TextField tfFirstName, tfLastName, tfSalutation;
    // Card
    @FXML
    private TextField tfCardNr, tfCardtype, tfPIN, tfAccountID;
    // success & error
    @FXML
    private Label lblSuccess, lblSuccess1, lblSuccess2, lblError, lblError1, lblError2, lblTransSuccessful;
    // Restock Buttons
    @FXML
    private Button btnRestock, btnRestockEuro, btnRestockCHF;
    // Root Pane
    @FXML
    private BorderPane root;

    private boolean success = false;
    private static boolean wasSuccessful = false;

    @FXML
    private void initialize() {
        if (wasSuccessful) {
            wasSuccessful = false;
            lblTransSuccessful.setVisible(true);
        }
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        tfIBAN.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                root.requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    @FXML
    private void onCreateCardClick() {
        hideSuccess();
        try {
            if(tfCardNr.getText().isEmpty() || tfCardtype.getText().isEmpty() || tfPIN.getText().isEmpty() || tfAccountID.getText().isEmpty()) {
                lblError.setText("Alle Felder müssen ausgefüllt sein!");
                lblError.setVisible(true);
            } else if (!Utils.isNumeric(tfCardNr.getText()) || !Utils.isNumeric(tfPIN.getText()) || !Utils.isNumeric(tfAccountID.getText())  || tfCardNr.getText().length() > 16) {
                lblError.setText("Falsches Format!");
                lblError.setVisible(true);
            } else if (Objects.requireNonNull(Database.getAllCardNrs()).contains(tfCardNr.getText())){
                lblError.setText("Kartennummer vergeben!");
                lblError.setVisible(true);
            }  else if (!Objects.requireNonNull(Database.getAllAccountIDs()).contains(Integer.parseInt(tfAccountID.getText()))) {
                lblError.setText("AccountID existiert nicht!");
                lblError.setVisible(true);
            } else { // Success
                byte[] PINsalt = Security.createSalt();
                byte[] PINhash = Security.hash(tfPIN.getText(), PINsalt);
                Database.insertCard(tfCardNr.getText(), tfCardtype.getText(), PINhash, PINsalt, Integer.parseInt(tfAccountID.getText()));
                lblError.setVisible(false);
                lblSuccess.setVisible(true);
                success = true;
            }
        } catch (Exception e) {
            lblSuccess.setVisible(false);
            lblError.setText("Fehler!");
            lblError.setVisible(true);
        }
    }

    @FXML
    private void onCreateUserClick() {
        hideSuccess();
        try {
            if(tfFirstName.getText().isEmpty() || tfLastName.getText().isEmpty() || tfSalutation.getText().isEmpty()) {
                lblError1.setText("Alle Felder müssen ausgefüllt sein!");
                lblError1.setVisible(true);
            } else if (tfSalutation.getText().equals("Herr") || tfSalutation.getText().equals("Frau")) { // Success
                Database.insertUser(tfFirstName.getText(), tfLastName.getText(), Salutation.valueOf(tfSalutation.getText()));
                lblError1.setVisible(false);
                lblSuccess1.setText("User erfolgreich erstellt!");
                lblSuccess1.setVisible(true);
                success = true;
            } else {
                lblError1.setText("Falsches Anrede-Format!");
                lblError1.setVisible(true);
            }
        } catch (Exception e) {
            lblSuccess1.setVisible(false);
            lblError1.setText("Fehler!");
            lblError1.setVisible(true);
        }
    }

    @FXML
    private void onCreateAccountClick() {
        hideSuccess();
        try {
            if(tfIBAN.getText().isEmpty() || tfBalance.getText().isEmpty() || tfBank.getText().isEmpty() || tfUserID.getText().isEmpty()) {
                lblError2.setText("Alle Felder müssen ausgefüllt sein!");
                lblError2.setVisible(true);
            } else if (!Utils.isNumeric(tfBalance.getText()) || !Utils.isNumeric(tfUserID.getText()) || tfIBAN.getText().length() > 21) {
                lblError2.setText("Falsches Format!");
                lblError2.setVisible(true);
            }  else if (!Objects.requireNonNull(Database.getAllUserIDs()).contains(Integer.parseInt(tfUserID.getText()))) {
                lblError.setText("UserID existiert nicht!");
                lblError.setVisible(true);
            } else { // Success
                Database.insertAccount(tfIBAN.getText(), Double.parseDouble(tfBalance.getText()), tfBank.getText(), Integer.parseInt(tfUserID.getText()));
                lblError2.setVisible(false);
                lblSuccess2.setVisible(true);
                success = true;
            }
        } catch (Exception e) {
            lblSuccess2.setVisible(false);
            lblError2.setText("Fehler!");
            lblError2.setVisible(true);
        }
    }
    
    @FXML
    private void paneClicked() {
        hideSuccess();
    }

    private void hideSuccess() {
        if(success) {
            lblSuccess.setVisible(false);
            lblSuccess1.setVisible(false);
            lblSuccess2.setVisible(false);
            lblTransSuccessful.setVisible(false);
            success = false;
        }
    }

    @FXML
    private void restock() {
        btnRestock.setVisible(false);
        btnRestockEuro.setVisible(true);
        btnRestockCHF.setVisible(true);
        root.requestFocus();
    }

    @FXML
    private void restockEuro() throws IOException {
        DepositInfo.getInstance().setCurrency(Currency.Euro);
        DepositInfo.getInstance().setAdmin(true);
        Navigation.switchToView("DepositEuro");
    }

    @FXML
    private void restockCHF() throws IOException {
        DepositInfo.getInstance().setCurrency(Currency.CHF);
        DepositInfo.getInstance().setAdmin(true);
        Navigation.switchToView("DepositCHF");
    }

    @FXML
    private void onBackToLogin() throws IOException {
        DepositInfo.getInstance().setAdmin(false);
        Navigation.switchToView("Login");
    }

    @FXML
    private void toDashboard() throws IOException {
        Navigation.switchToView("Dashboard");
    }

    @FXML
    private void clear() {
        tfIBAN.clear();
        tfBalance.clear();
        tfBank.clear();
        tfUserID.clear();
        tfFirstName.clear();
        tfLastName.clear();
        tfSalutation.clear();
        tfCardNr.clear();
        tfCardtype.clear();
        tfPIN.clear();
        tfAccountID.clear();
        lblSuccess.setVisible(false);
        lblSuccess1.setVisible(false);
        lblSuccess2.setVisible(false);
        lblError.setVisible(false);
        lblError1.setVisible(false);
        lblError2.setVisible(false);
        lblTransSuccessful.setVisible(false);
    }

    public static void setWasSuccessful(boolean b) {
        wasSuccessful = b;
    }
}
