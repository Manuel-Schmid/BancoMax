package Application;

import Application.Data.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception{
        primaryStage = stage;
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Views/StandBy.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setTitle("BancoMax");
        primaryStage.getIcons().add(new Image("Application/Media/logo256.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2); // center
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2); // center
    }

    public static void main(String[] args) {
        launch(args);
        Database.close();
    }

}
