package Application;

import Application.Data.Database;
import Application.Utility.Salutation;
import Application.Utility.Security;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

//        // INSERT admin
//        byte[] salt = Security.createSalt();
//        byte[] hash = Security.hash("xxxxx", salt);
//
//         Database.insertAdmin(hash, salt);

    }

}
