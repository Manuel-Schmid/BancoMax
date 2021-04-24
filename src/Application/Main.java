package Application;

import Application.Data.CurrencyAPI;
import Application.Data.Database;
import Application.Data.Transaction;
import Application.Utility.Currency;
import Application.Utility.Security;
import Application.Utility.Utils;
import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;


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

//        System.out.println(CurrencyAPI.getExRate());

//        // INSERT admin
//        byte[] salt = Security.createSalt();
//        byte[] hash = Security.hash("xxx", salt);
//
//         Database.insertAdmin(hash, salt);

    }

}
