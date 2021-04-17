package Application.Controllers;

import Application.Data.Database;
import Application.Data.DepositInfo;
import Application.Data.Info;
import Application.Data.WithdrawalInfo;
import Application.Utility.Currency;
import Application.Utility.Navigation;
import Application.Utility.Operation;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;

public class MasterController {

    @FXML
    private Label lblWelcome, lblError;

    public void onBackToLogin() throws IOException {
        Info.logout();
        Navigation.switchToView("Login");
    }

    @FXML
    private void initialize() {
        lblWelcome.setText("Willkommen " + Info.getSalutation() + " " + Info.getLastName());
    }

    @FXML
    private void onShowAccountInfo() throws IOException {
        Navigation.switchToView("AccountInfo");
    }

    @FXML
    private void onPinChange() throws IOException {
        System.err.close();
        System.setErr(System.out);
        Navigation.switchToView("PinChange");
    }

    @FXML
    private void onDepositCHF() throws IOException {
        DepositInfo.getInstance().setCurrency(Currency.CHF);
        Navigation.switchToView("DepositCHF");
    }

    @FXML
    private void onDepositEuro() throws IOException {
        DepositInfo.getInstance().setCurrency(Currency.Euro);
        Navigation.switchToView("DepositEuro");
    }

    @FXML
    private void onWithdrawCHF() throws IOException {
        WithdrawalInfo.getInstance().setCurrency(Currency.CHF);
        Navigation.switchToView("Withdrawal");
    }

    @FXML
    private void onWithdrawEuro() throws IOException {
        WithdrawalInfo.getInstance().setCurrency(Currency.Euro);
        Navigation.switchToView("Withdrawal");
    }

    @FXML
    private void onWithdrawExpress() throws IOException {
        WithdrawalInfo.getInstance().setCurrency(Currency.CHF);
        WithdrawalInfo.getInstance().setAmount(50);
        int[] banknotes = {0, 0, 0, 1, 0, 0};
        boolean enoughInStock = Database.checkMoneystock(banknotes, WithdrawalInfo.getInstance().getCurrency().toString()); // error handling if moneyStock allows the withdrawal
        double amountInCHF = WithdrawalInfo.getInstance().getAmount();
        boolean enoughBalance = (Database.getBalance(Info.getAccountID()) >= amountInCHF);
        if (!enoughInStock) {
            lblError.setText("Der Automat hat nicht mehr genügend Noten für Ihre Anfrage! Probieren Sie es später erneut.");
            lblError.setVisible(true);
        } else if (!enoughBalance) {
            lblError.setText("Ihr Kontostand ist zu niedrig für diesen Bezug!");
            lblError.setVisible(true);
        } else {
            Database.updateBalance(Operation.withdraw, amountInCHF, Info.getAccountID()); // Abzug vom Konto
            Database.updateMoneyStock(Operation.withdraw, banknotes, Currency.CHF); // Abzug vom MoneyStock
            Database.insertTransaction(Operation.withdraw, WithdrawalInfo.getInstance().getAmount(), Info.getCardID());

            // Auszahlung & Transaktion erfolgreich
            Navigation.switchToView("TransactionSuccess");
        }
    }

    // Animations & Design

    @FXML
    private Rectangle rec1, rec2, rec3;
    @FXML
    private Label title1, title2, title3;
    @FXML
    private AnchorPane depositBtns, manageBtns, withdrawBtns;

    private String active = "";

    @FXML
    private void manageHover() {
        if(active.equals("") || active.equals("2") || active.equals("3")) {
            reverse(false);
            animation(title1, manageBtns, rec1, "1");
        }
    }
    @FXML
    private void depositHover() {
        if(active.equals("") || active.equals("1") || active.equals("3")) {
            reverse(false);
            animation(title2, depositBtns, rec2, "2");
        }
    }
    @FXML
    private void withdrawHover() {
        if (active.equals("") || active.equals("1") || active.equals("2")) {
            reverse(false);
            animation(title3, withdrawBtns, rec3, "3");
        }
    }
    @FXML
    private void reverse(boolean menuExited) {
        if (active.equals("1")) {
            reverseAnimation(title1, manageBtns, rec1, menuExited);
        } else if (active.equals("2")) {
            reverseAnimation(title2, depositBtns, rec2, menuExited);
        } else if (active.equals("3")) {
            reverseAnimation(title3, withdrawBtns, rec3, menuExited);
        }
    }
    @FXML
    private void menuExited() {
        reverse(true);
    }

    private void animation(Label title, AnchorPane buttons, Rectangle rec, String status) {
        active = "running";
        buttons.toFront();
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), title);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), buttons);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        Timeline recTransition = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rec.heightProperty(), rec.getHeight())),
                new KeyFrame(Duration.millis(500), new KeyValue(rec.heightProperty(), 2.8 * rec.getHeight())));
        recTransition.play();
        Timeline recTransition2 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rec.yProperty(), rec.getY())),
                new KeyFrame(Duration.millis(500), new KeyValue(rec.yProperty(), rec.getY() - 41)));
        recTransition2.play();
        ParallelTransition pt = new ParallelTransition(fadeOut, fadeIn, recTransition, recTransition2);
        pt.play();

        pt.setOnFinished((e) -> {
            buttons.setDisable(false);
            active = status;
        });
    }

    private void reverseAnimation(Label title, AnchorPane buttons, Rectangle rec, boolean menuExited) {
        active = "running";
        buttons.toBack();
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), title);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), buttons);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();
        Timeline recTransition = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rec.heightProperty(), rec.getHeight())),
                new KeyFrame(Duration.millis(500), new KeyValue(rec.heightProperty(), rec.getHeight() / 2.8)));
        recTransition.play();
        Timeline recTransition2 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rec.yProperty(), rec.getY())),
                new KeyFrame(Duration.millis(500), new KeyValue(rec.yProperty(), rec.getY() + 41)));
        recTransition2.play();
        ParallelTransition pt = new ParallelTransition(fadeIn, fadeOut, recTransition, recTransition2);
        pt.play();

        pt.setOnFinished((e) -> {
            buttons.setDisable(true);
            if (menuExited) {
                active = "";
            }
        });
    }

}
