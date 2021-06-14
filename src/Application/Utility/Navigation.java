package Application.Utility;

import Application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.util.Objects;

public class Navigation {
    public static void switchToView(String view) throws IOException {
        BorderPane pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Views/" + view + ".fxml")));
        Main.primaryStage.setScene(new Scene(pane));
        Main.primaryStage.show();
    }

    public static void switchToPayout() throws IOException {
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Views/Payout.fxml")));
        Main.primaryStage.setScene(new Scene(pane));
        Main.primaryStage.show();
    }
}
