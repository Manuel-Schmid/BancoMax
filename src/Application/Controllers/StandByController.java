package Application.Controllers;

import Application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class StandByController {

    @FXML
    void openLoginView(MouseEvent mouseEvent) throws IOException {
        BorderPane pane = FXMLLoader.load(Main.class.getResource("Views/Login.fxml"));
        Main.primaryStage.setScene(new Scene(pane));
        Main.primaryStage.show();
    }
}
