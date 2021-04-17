package Application.Controllers;

import Application.Utility.Navigation;
import javafx.fxml.FXML;

import java.io.IOException;

public class StandByController {

    @FXML
    void openLoginView() throws IOException {
        Navigation.switchToView("Login");
    }
}
