package Application.Controllers;

import Application.Data.Database;
import Application.Data.DepositInfo;
import Application.Data.Info;
import Application.Main;
import Application.Utility.Navigation;
import Application.Utility.Security;
import Application.Utility.Utils;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Arrays;


public class LoginController {

    @FXML
    private Label lblAdminError, lblError, lblErrorFile;
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
        Utils.moveFocus(pfPassword, root);
    }

    @FXML
    void onAdminClick() {
        if (settingsActive.equals("true")) {
            if(pfPassword.getText().isEmpty()) {
                setError(lblAdminError, "Bitte Passwort eingeben!");
            } else {
                try {
                    byte[] salt = Database.getAdminSalt();
                    byte[] hash = Security.hash(pfPassword.getText(), salt);
                    byte[] expHash = Database.getAdminHash();
                    if (Arrays.equals(hash, expHash)) { // Korrektes Passwort
                        Navigation.switchToView("Admin");
                    } else { // Falsches Passwort
                        setError(lblAdminError, "Falsches Passwort!");
                    }
                } catch (Exception e) {
                    setError(lblAdminError, "Fehler!");
                }
            }
        }
    }

    @FXML
    private void chooseFile() {
        root.setDisable(true);
        lblErrorFile.setVisible(false);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(Utils.extFilter);
        File file = fileChooser.showOpenDialog(Main.primaryStage);
        root.setDisable(false);
        if (file == null || !file.getName().contains(".txt")) {
            lblErrorFile.setVisible(true);
        } else {
            String cardNr = Security.decrypt(Utils.read(file));
            if (cardNr.isEmpty() || cardNr.isBlank() || !Utils.isNumeric(cardNr)) {
                lblErrorFile.setVisible(true);
            } else {
                tfCardNr.setText(cardNr);
            }
        }
    }

    @FXML
    private void keyPressed(KeyEvent ke) {
        lblErrorFile.setVisible(false);
        if (ke.getCode().equals(KeyCode.ENTER)) {
            onLoginClick();
        }
    }

    @FXML
    private void textKeyPressed(KeyEvent ke) {
        lblErrorFile.setVisible(false);
        if (ke.getCode().equals(KeyCode.ENTER)) {
            onAdminClick();
        }
    }

    public void onLoginClick() {
        DepositInfo.getInstance().setAdmin(false);
        if(tfCardNr.getText().isEmpty() || tfPIN.getText().isEmpty()) {
            setError(lblError, "Bitte alle Felder ausfüllen!");
        } else if (!Utils.isNumeric(tfCardNr.getText()) || !Utils.isNumeric(tfPIN.getText()) || tfCardNr.getText().length() > 16) {
            setError(lblError, "Falsches Format!");
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
                    setError(lblError, "Falsche Kombination!");
                }
            } catch (Exception e) {
                setError(lblError, "Fehler!");
            }
        }
    }

    private void setError(Label label, String msg) {
        label.setText(msg);
        label.setVisible(true);
    }

    // Animations & Design

    private String settingsActive = "false";
    @FXML
    void settingsHover() {
        if (settingsActive.equals("false")) {
            settingsActive = "running";
            adminTransition(+310, "true");
        }
    }

    @FXML
    void onLoginEntered() {
        if (settingsActive.equals("true")) {
            settingsActive = "running";
            adminTransition(-310, "false");
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
