package Application.Controllers;

import Application.Data.Database;
import Application.Main;
import Application.Utility.Navigation;
import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class StandByController {

    @FXML
    private StackPane stackpane;
    @FXML
    private BorderPane root;
    private static JFXDialog dialog;

    @FXML
    private void initialize() {
        Database.close();
        stackpane.setVisible(false);
    }

    @FXML
    void openLoginView() {
        stackpane.setVisible(true);
        if (dialog != null) dialog.close();
        try {
            Database.connectToDatabase();

            System.err.close();
            System.setErr(System.out);
            Navigation.switchToView("Login");

        } catch (ClassNotFoundException | SQLException e) {
            openDialog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openDialog() {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Datenbankverbindung fehlgeschlagen"));
        content.setBody(new Text("Die Verbindung zur BancoMax-Datenbank ist leider fehlgeschlagen. \nBitte kontaktieren Sie einen Systemadministrator oder versuchen Sie es später erneut. \nDanke für Ihr Verständnis."));
        content.getStyleClass().add("dialog-content");
        dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btnAgain = new JFXButton("Erneut versuchen");
        JFXButton btnCancel = new JFXButton("Programm schließen");
        btnAgain.getStyleClass().add("dialog-button");
        btnCancel.getStyleClass().add("dialog-button");

        btnAgain.setOnAction(actionEvent -> {
            openLoginView();
        });
        btnCancel.setOnAction(actionEvent -> {
            Main.primaryStage.close();
        });
        content.setActions(btnCancel, btnAgain);
        root.setDisable(true);
        dialog.show();
        dialog.toFront();
        stackpane.requestFocus();
    }
}
