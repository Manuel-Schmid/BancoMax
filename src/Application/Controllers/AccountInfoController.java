package Application.Controllers;

import Application.Data.Database;
import Application.Data.Info;
import Application.Main;
import Application.PDF_Maker.OpenPDF;
import Application.PDF_Maker.PDFFile;
import Application.Utility.Navigation;
import Application.Utility.Utils;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AccountInfoController {

    @FXML
    private Label lblAccount, lblBank, lblBalanceDate, lblBalance;
    @FXML
    private BorderPane root;
    @FXML
    private JFXButton btnLastTrans;

    @FXML
    private void initialize() {
        lblAccount.setText("Konto: " + Info.getIBAN());
        lblBank.setText("Bank: " + Info.getBank());
        lblBalanceDate.setText("Aktueller Saldo per " + Utils.getCurrentTimeStamp("dd.MM.yyyy"));
        double balance = Database.getBalance(Info.getAccountID());
        lblBalance.setText("CHF " + Utils.formatMoney(balance));
        if (balance <= 0) {
            lblBalance.setTextFill(Color.rgb(247,0,62));
        }
        Utils.moveFocus(btnLastTrans, root);
    }

    @FXML
    private void onPrintReceipt() {
        PDFFile f1 = new PDFFile();
        f1.createWithdrawInfoDeposit("Informationsbeleg", "information");

        OpenPDF oPdf = new OpenPDF(f1);
    }

    @FXML
    private void onLastTransactions() throws IOException {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Main.primaryStage);
        BorderPane pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Views/transactionHistory.fxml")));
        dialog.setTitle("BancoMax - Transaktionen");
        dialog.getIcons().add(new Image("Application/Media/logo256.png"));
        dialog.setScene(new Scene(pane));
        dialog.setResizable(false);
        dialog.show(); // show lastTransactions-List
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        dialog.setX((primScreenBounds.getWidth() - dialog.getWidth()) / 2); // center
        dialog.setY((primScreenBounds.getHeight() - dialog.getHeight()) / 2); // center
    }

    @FXML
    private void toMaster() throws IOException {
        Navigation.switchToView("Master");
    }

}
