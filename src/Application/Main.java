package Application;

import Application.Data.CurrencyAPI;
import Application.Utility.Security;
import Application.Data.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.Test;

import java.io.File;
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

    public static void main(String[] args) throws Exception {
        launch(args);
        Database.close();

//        System.out.println(CurrencyAPI.getExRate());

//        byte[] salt = Security.createSalt();
//        byte[] hash;
//        hash = Security.hash("xxx", salt);
//        Database.changeAdminPW(hash, salt);

    }

}
