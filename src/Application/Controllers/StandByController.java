package Application.Controllers;

import Application.Utility.Navigation;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class StandByController {

    @FXML
    void openLoginView(MouseEvent mouseEvent) throws IOException {
        Navigation.switchToView("Login");
    }
}
