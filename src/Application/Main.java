package Application;

import Application.Data.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;

    // GUI
    @Override
    public void start(Stage stage) throws Exception{
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("Views/StandBy.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("BancoMax");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Main
    public static void main(String[] args) throws Exception {
        // Database.connectToDatabase();
        // Database.viewTableUser();
        launch(args);
    }

}
