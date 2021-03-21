package Application.Controllers;

import Application.Data.Database;
import Application.Main;
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

public class AdminController {

    @FXML
    private Label lblSuccess;
    @FXML
    private Label lblError;
    @FXML
    private TextField tfCardNr;
    @FXML
    private  TextField tfCardtype;
    @FXML
    private TextField tfPIN;
    @FXML
    private  TextField tfAccountID;

    private boolean success = false;

    @FXML
    private void onCreateCardClick(ActionEvent actionEvent) {
        try {
            if(tfCardNr.getText().isEmpty() || tfCardtype.getText().isEmpty() || tfPIN.getText().isEmpty() || tfAccountID.getText().isEmpty()) {
                lblError.setVisible(true);
            } else if (!Utils.isNumeric(tfCardNr.getText()) || !Utils.isNumeric(tfPIN.getText()) || !Utils.isNumeric(tfAccountID.getText())  || tfCardNr.getText().length() > 16) {
                lblError.setText("Falsches Format!");
                lblError.setVisible(true);
            } else if (Database.getAllCardNrs().contains(tfCardNr.getText())){
                lblError.setText("Kartennummer vergeben!");
                lblError.setVisible(true);
            }  else if (!Database.getAllAccountIDs().contains(Integer.parseInt(tfAccountID.getText()))) {
                lblError.setText("AccountID existiert nicht!");
                lblError.setVisible(true);
            } else { // Success
                byte[] PINsalt = Security.createSalt();
                byte[] PINhash = Security.hash(tfPIN.getText(), PINsalt);
                Database.insertCard(tfCardNr.getText(), tfCardtype.getText(), PINhash, PINsalt, Integer.parseInt(tfAccountID.getText()));
                lblError.setVisible(false);
                lblSuccess.setText("Karte erfolgreich erstellt!");
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
    private void paneClicked(MouseEvent mouseEvent) {
        if(success) {
            lblSuccess.setVisible(false);
            success = false;
        }
    }

    @FXML
    private void onBackToLogin(ActionEvent actionEvent) throws IOException {
        BorderPane pane = FXMLLoader.load(Main.class.getResource("Views/Login.fxml"));
        Main.primaryStage.setScene(new Scene(pane));
        Main.primaryStage.show();
    }

}
