package Application.Controllers;

import Application.Data.Database;
import Application.Main;
import Application.Utility.Salutation;
import Application.Utility.Security;
import Application.Utility.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
    private Label lblSuccess, lblSuccess1, lblError, lblError1;

    private boolean success = false;

    @FXML
    private void onCreateCardClick(ActionEvent actionEvent) {
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
        } catch (NumberFormatException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            lblSuccess.setVisible(false);
            lblError.setText("Fehler!");
            lblError.setVisible(true);
        }
    }

    @FXML
    private void onCreateUserClick(ActionEvent actionEvent) {
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
    private void paneClicked(MouseEvent mouseEvent) {
        if(success) {
            lblSuccess.setVisible(false);
            lblSuccess1.setVisible(false);
            success = false;
        }
    }

    @FXML
    private void onBackToLogin(ActionEvent actionEvent) throws IOException {
        BorderPane pane = FXMLLoader.load(Main.class.getResource("Views/Login.fxml"));
        Main.primaryStage.setScene(new Scene(pane));
        Main.primaryStage.show();
    }

    @FXML
    private void onCreateAccountClick(ActionEvent actionEvent) {

    }
}
