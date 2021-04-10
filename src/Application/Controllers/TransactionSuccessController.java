package Application.Controllers;

import Application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TransactionSuccessController {
    @FXML
    private void onClick() throws IOException {
        AnchorPane pane = FXMLLoader.load(Main.class.getResource("Views/StandBy.fxml"));
        Main.primaryStage.setScene(new Scene(pane));
        Main.primaryStage.show();
    }

}
