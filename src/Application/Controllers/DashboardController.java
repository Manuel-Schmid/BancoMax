package Application.Controllers;

import Application.Utility.Navigation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;

import java.io.IOException;

public class DashboardController {

    @FXML
    private void initialize() {

    }

    @FXML
    private void toAdmin() throws IOException {
        Navigation.switchToView("Admin");
    }

}
