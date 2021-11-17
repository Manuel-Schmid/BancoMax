package Application;

import Application.Data.Database;
import Application.Utility.Currency;
import Application.Utility.Operation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;

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

    private static void bulkTransactionInsert() { // edit inside this function
        try {
            Database.connectToDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        Random r = new Random();

        for (int i = 1; i <= 30; i++) { // number of days in month
            int dailyTransactions = r.nextInt(14) + 4;
            System.out.println(i); // function is finished as soon as this hits the number of days in the month
            for (int j = 1; j <= dailyTransactions; j++) {
                int op = r.nextInt(2);
                int cur = r.nextInt(2);
                int amt = r.nextInt(2000) + 1;
                int cardID = r.nextInt(8) + 1;
                int year = 2021;
                int month = 10; // zero based !
                int day = i;
                int hour = r.nextInt(24);
                int minute = r.nextInt(60);
                Database.insertModifiedTransaction(
                        year,
                        month,
                        day,
                        hour,
                        minute,
                        op == 0 ? Operation.withdraw : Operation.deposit,
                        cur == 0 ? Currency.CHF : Currency.Euro,
                        amt,
                        cardID
                );
            }
        }
    }
}
