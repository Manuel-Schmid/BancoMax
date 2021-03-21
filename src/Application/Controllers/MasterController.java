package Application.Controllers;

import Application.Data.UserInfo;
import Application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MasterController {

    @FXML
    private Label lblWelcome;

    String welcome = "Welcome " + UserInfo.getSalutation() + " " + UserInfo.getLastName();

    public void onBackToLogin(ActionEvent actionEvent) throws IOException {
        BorderPane pane = FXMLLoader.load(Main.class.getResource("Views/Login.fxml"));
        Main.primaryStage.setScene(new Scene(pane));
        Main.primaryStage.show();
    }

    @FXML
    public void initialize() {
        lblWelcome.setText("Willkommen " + UserInfo.getSalutation() + " " + UserInfo.getLastName());
    }
}
