package Application.Controllers;

import Application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;


public class LoginController {

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
        BorderPane pane = FXMLLoader.load(Main.class.getResource("Views/Admin.fxml"));
        Main.primaryStage.setScene(new Scene(pane));
        Main.primaryStage.show();
    }

    @FXML
    void onSettingsClick(MouseEvent mouseEvent) {
        btnSettings.setVisible(false);
        lblPassword.setVisible(true);
        btnAdmin.setVisible(true);
        pfPassword.setVisible(true);
    }
}
