package Application.Controllers;

import Application.Data.Database;
import Application.Data.DepositInfo;
import Application.Data.Info;
import Application.Utility.Navigation;
import Application.Utility.Security;
import Application.Utility.Utils;
import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;


public class LoginController {

    @FXML
    private  Label lblAdminError, lblError;
    @FXML
    private TextField pfPassword, tfCardNr, tfPIN;
    @FXML
    private ImageView btnSettings;
    @FXML
    private HBox cover;
    @FXML
    private Rectangle btnLogin;
    @FXML
    private BorderPane root;

    @FXML
    private void initialize() {
        btnSettings.toFront();
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        pfPassword.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                root.requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    @FXML
    void onAdminClick() {
        if (settingsActive.equals("true")) {
            if(pfPassword.getText().isEmpty()) {
                lblAdminError.setText("Bitte Passwort eingeben!");
                lblAdminError.setVisible(true);
            } else {
                try {
                    byte[] salt = Database.getAdminSalt();
                    byte[] hash = Security.hash(pfPassword.getText(), salt);
                    byte[] expHash = Database.getAdminHash();
                    if (Arrays.equals(hash, expHash)) { // Korrektes Passwort
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
    }

    @FXML
    private void keyPressed(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            onLoginClick();
        }
    }

    @FXML
    private void textKeyPressed(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            onAdminClick();
        }
    }

    public void onLoginClick() {
        DepositInfo.getInstance().setAdmin(false);
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

    // Animations & Design

    private String settingsActive = "false";
    @FXML
    void settingsHover() {
        if (settingsActive.equals("false")) {
            settingsActive = "running";
            adminTransition(+300, "true");
        }
    }

    @FXML
    void onLoginEntered() {
        if (settingsActive.equals("true")) {
            settingsActive = "running";
            adminTransition(-300, "false");
        }
    }

    TranslateTransition transition;
    TranslateTransition transition1;
    ParallelTransition pt;
    private void adminTransition(int pixels, String status) {
        lblAdminError.setVisible(false);
        transition = new TranslateTransition(Duration.millis(800), btnSettings);
        transition.setByX(pixels);
        transition.play();
        transition1 = new TranslateTransition(Duration.millis(800), cover);
        transition1.setByX(pixels);
        transition.play();
        pt = new ParallelTransition(transition, transition1);
        pt.play();

        pt.setOnFinished((e) -> {
            settingsActive = status;
        });
    }

    RadialGradient gradient1;
    @FXML
    void onLoginHover(MouseEvent e) {
        gradient1 = new RadialGradient(0, .1, e.getX(),e.getY(), 80, false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(250,166,0)),
                new Stop(1, Color.rgb(230,0,60))); //rgb(170,25,55)
        btnLogin.setFill(gradient1);
    }

    @FXML
    void onLoginExited() {
        btnLogin.setFill(Color.rgb(230,0,60));
    }
}
