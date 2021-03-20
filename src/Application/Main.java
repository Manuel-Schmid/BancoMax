package Application;

import Application.Data.Database;
import Application.Utility.Security;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;

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

    // 127.0.0.1:3306
    // http://83.77.103.210:3306/bancomax
    // http://83.77.103.210/phpmyadmin:3306/bancomax
    // db_ip:3306/dbName

    // Main
    public static void main(String[] args) throws Exception {
        // Database.connectToDatabase();
        // Database.viewTableUser();
        launch(args);

//        byte[] salt = Security.createSalt();
//        System.out.println("Salt: " + Arrays.toString(salt));
//
//        System.out.println(Arrays.toString(Security.hash("xasdf", salt)));
//        System.out.println(Arrays.toString(Security.hash("xasdf", salt)));
//        System.out.println(Arrays.toString(Security.hash("Many", salt)));
//        System.out.println(Arrays.toString(Security.hash("Many", salt)));

    }

}
