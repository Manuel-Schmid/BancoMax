package Application.Controllers;

import Application.Main;
import Application.Utility.Navigation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class StandByController {

    @FXML
    void openLoginView(MouseEvent mouseEvent) throws IOException {
        Navigation.switchToView("Login");
    }
}
