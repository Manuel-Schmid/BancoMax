package Application.Controllers;

import Application.Data.Database;
import Application.Data.Info;
import Application.Utility.Navigation;
import Application.Utility.Security;
import Application.Utility.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.Arrays;


public class LoginController {
    @FXML
    private  Label lblAdminError, lblError, lblPassword;
    @FXML
    private TextField tfPIN, tfCardNr, pfPassword;
    @FXML
    private ImageView btnSettings;
    @FXML
    private Button btnAdmin;

    @FXML
    void onAdminClick() {
        if(pfPassword.getText().isEmpty()) {
            lblAdminError.setText("Bitte alle Felder ausfüllen!");
            lblAdminError.setVisible(true);
        } else {
            try {
                byte[] salt = Database.getAdminSalt();
                byte[] hash = Security.hash(pfPassword.getText(), salt);
                byte[] expHash = Database.getAdminHash();
                if (Arrays.equals(hash, expHash)) { // Richtige Kombination
                    Navigation.switchToView("Admin");
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
    void onSettingsClick() {
        btnSettings.setVisible(false);
        lblPassword.setVisible(true);
        btnAdmin.setVisible(true);
        pfPassword.setVisible(true);
    }

    public void onLoginClick() {
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
                    Info.setInfo(cardNr);
                    Navigation.switchToView("Master");
                } else { // Falsche Kombination
                    lblError.setText("Falsche Kombination!");
                    lblError.setVisible(true);
                }
            } catch (Exception e) {
                lblError.setText("Fehler!");
                lblError.setVisible(true);
            }
        }
    }
}
