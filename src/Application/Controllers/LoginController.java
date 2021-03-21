package Application.Controllers;

import Application.Data.AccountInfo;
import Application.Data.Database;
import Application.Data.UserInfo;
import Application.Main;
import Application.Utility.Security;
import Application.Utility.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;


public class LoginController {
    @FXML
    private  Label lblAdminError;
    @FXML
    private TextField tfPIN;
    @FXML
    private TextField tfCardNr;
    @FXML
    private Label lblError;
    @FXML
    private Label lblPassword;
    @FXML
    private ImageView btnSettings;
    @FXML
    private Button btnAdmin;
    @FXML
    private TextField pfPassword;

    @FXML
    void onAdminClick(ActionEvent actionEvent) throws IOException {
        if(pfPassword.getText().isEmpty()) {
            lblAdminError.setText("Bitte alle Felder ausfüllen!");
            lblAdminError.setVisible(true);
        } else {
            try {
                byte[] salt = Database.getAdminSalt();
                byte[] hash = Security.hash(pfPassword.getText(), salt);
                byte[] expHash = Database.getAdminHash();
                if (Arrays.equals(hash, expHash)) { // Richtige Kombination
                    BorderPane pane = FXMLLoader.load(Main.class.getResource("Views/Admin.fxml"));
                    Main.primaryStage.setScene(new Scene(pane));
                    Main.primaryStage.show();
                } else { // Falsches Passwort
                    lblAdminError.setText("Falsches Passwort!");
                    lblAdminError.setVisible(true);
                }
            } catch (Exception e) {
                lblAdminError.setText("Fehler");
                lblAdminError.setVisible(true);
            }
        }
    }

    @FXML
    void onSettingsClick(MouseEvent mouseEvent) {
        btnSettings.setVisible(false);
        lblPassword.setVisible(true);
        btnAdmin.setVisible(true);
        pfPassword.setVisible(true);
    }

    public void onLoginClick(ActionEvent actionEvent) throws IOException {
        if(tfCardNr.getText().isEmpty() || tfPIN.getText().isEmpty()) {
            lblError.setText("Bitte alle Felder ausfüllen!");
            lblError.setVisible(true);
        } else if (!Utils.isNumeric(tfCardNr.getText()) || !Utils.isNumeric(tfPIN.getText()) || tfCardNr.getText().length() > 16) {
            lblError.setText("Falsches Format!");
            lblError.setVisible(true);
        } else {
            try {
                String cardNr = tfCardNr.getText();
                byte[] salt = Database.getSalt(cardNr);
                byte[] hash = Security.hash(tfPIN.getText(), salt);
                byte[] expHash = Database.getHash(cardNr);
                if (Arrays.equals(hash, expHash)) { // Richtige Kombination
                    UserInfo.setUser(cardNr);
                    AccountInfo.setAccount(cardNr);
                    BorderPane pane = FXMLLoader.load(Main.class.getResource("Views/Master.fxml"));
                    Main.primaryStage.setScene(new Scene(pane));
                    Main.primaryStage.show();
                } else { // Falsche Kombination
                    lblError.setText("Falsche Kombination!");
                    lblError.setVisible(true);
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                lblError.setText("Fehler!");
                lblError.setVisible(true);
            }
        }
    }
}
