package Application.Controllers;

import Application.Data.Database;
import Application.Main;
import Application.Utility.Security;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AdminController {

    @FXML
    private Label lblError;
    @FXML
    private Button btnCreateCard;
    @FXML
    private TextField tfCardNr;
    @FXML
    private  TextField tfCardtype;
    @FXML
    private TextField tfPIN;
    @FXML
    private  TextField tfAccountID;

    @FXML
    private void onCreateCardClick(ActionEvent actionEvent) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if(tfCardNr.getText().isEmpty() || tfCardtype.getText().isEmpty() || tfPIN.getText().isEmpty() || tfAccountID.getText().isEmpty()) {
            lblError.setVisible(true);
        } else {
            byte[] PINsalt = Security.createSalt();
            byte[] PINhash = Security.hash(tfPIN.getText(), PINsalt);

            Database.insertCard(tfCardNr.getText(), tfCardtype.getText(), PINhash, PINsalt, Integer.parseInt(tfAccountID.getText()));
        }
    }

    @FXML
    private void onBackToLogin(ActionEvent actionEvent) throws IOException {
        BorderPane pane = FXMLLoader.load(Main.class.getResource("Views/Login.fxml"));
        Main.primaryStage.setScene(new Scene(pane));
        Main.primaryStage.show();
    }
}
