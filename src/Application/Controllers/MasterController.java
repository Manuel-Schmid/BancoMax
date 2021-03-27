package Application.Controllers;

import Application.Data.Info;
import Application.Utility.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class MasterController {

    @FXML
    private Label lblWelcome;

    String welcome = "Welcome " + Info.getSalutation() + " " + Info.getLastName();

    public void onBackToLogin(ActionEvent actionEvent) throws IOException {
        Navigation.switchToView("Login");
    }

    @FXML
    private void initialize() {
        lblWelcome.setText("Willkommen " + Info.getSalutation() + " " + Info.getLastName());
    }

    @FXML
    private void onShowAccountInfo() throws IOException {
        Navigation.switchToView("AccountInfo");
    }

    @FXML
    private void onPinChange() throws IOException {
        Navigation.switchToView("PinChange");
    }
}
