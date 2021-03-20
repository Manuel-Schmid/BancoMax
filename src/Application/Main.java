package Application;

import Application.Data.Database;
import Application.Utility.Security;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Arrays;

public class Main extends Application {

    public static Stage primaryStage;

    // GUI
    @Override
    public void start(Stage stage) throws Exception{
        primaryStage = stage;
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("Views/StandBy.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("BancoMax");
        primaryStage.getIcons().add(new Image("Application/Media/logo256.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Main
    public static void main(String[] args) throws Exception {
        Database.connectToDatabase();
        launch(args);

        // INSERT card
//        byte[] PINsalt = Security.createSalt();
//        byte[] PINhash = Security.hash("1234", PINsalt);
//        Database.insertCard("5678", "DebitCard", PINhash, PINsalt, 2);

//        // INSERT admin
//        byte[] salt = Security.createSalt();
//        byte[] hash = Security.hash("xxxxx", salt);
//
//         Database.insertAdmin(hash, salt);

    }

}
