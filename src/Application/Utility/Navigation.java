package Application.Utility;

import Application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Navigation {
    public static void switchToView(String view) throws IOException {
        BorderPane pane = FXMLLoader.load(Main.class.getResource("Views/"+view+".fxml"));
        Main.primaryStage.setScene(new Scene(pane));
        Main.primaryStage.show();
    }
}
