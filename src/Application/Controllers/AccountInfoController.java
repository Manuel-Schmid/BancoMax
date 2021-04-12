package Application.Controllers;

import Application.Data.Database;
import Application.Data.Info;
import Application.Main;
import Application.Utility.Navigation;
import Application.Utility.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AccountInfoController {

    @FXML
    private Label lblAccount, lblBank, lblBalanceDate, lblBalance;

    @FXML
    private void initialize() {
        lblAccount.setText("Konto: " + Info.getIBAN());
        lblBank.setText("Bank: " + Info.getBank());
        lblBalanceDate.setText("Aktueller Saldo per " + Utils.getCurrentTimeStamp("dd.MM.yyyy")); // !!!
        lblBalance.setText("CHF " + Utils.formatMoney(Database.getBalance(Info.getAccountID()))); // !!!
    }

    @FXML
    private void onPrintReceipt() {
        // print receipt
    }

    @FXML
    private void onLastTransactions() throws IOException {
        // show last transactions
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Main.primaryStage);
        BorderPane pane = FXMLLoader.load(Main.class.getResource("Views/transactionHistory.fxml"));
        dialog.setTitle("BancoMax - Last Transactions");
        dialog.getIcons().add(new Image("Application/Media/logo256.png"));
        dialog.setScene(new Scene(pane));
        dialog.show();
    }

    @FXML
    private void toMaster() throws IOException {
        Navigation.switchToView("Master");
    }

}
