package Application.Controllers;

import Application.Data.CurrencyAPI;
import Application.Data.Database;
import Application.Data.Info;
import Application.Data.WithdrawalInfo;
import Application.PDF_Maker.OpenPDF;
import Application.PDF_Maker.PDFFile;
import Application.Utility.Currency;
import Application.Utility.Navigation;
import Application.Utility.Operation;
import Application.Utility.Utils;
import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;

public class WithdrawalConfirmController {

    @FXML
    private Label lblCurrAmount, lblError;
    @FXML
    private Button btnBack;
    @FXML
    private BorderPane root;
    @FXML
    private JFXSlider slider;

    private String formattedAmount;

    @FXML
    private void initialize() {
        formattedAmount = WithdrawalInfo.getInstance().getCurrency() + " " + Utils.formatMoney(WithdrawalInfo.getInstance().getAmount());
        lblCurrAmount.setText(formattedAmount);
        slider.setLabelFormatter(Utils.getStringConverter());

        slider.valueProperty().addListener(e -> {
            Color imageColor = Color.rgb(250, 166, 0).interpolate(Color.rgb(230, 0, 60),
                    slider.getValue() / 100);
            slider.setStyle("-fx-custom-color : " + Utils.colorToHex(imageColor) + ";");
        });
        Utils.moveFocus(btnBack, root);
    }

    @FXML
    private void onBack() throws IOException {
        Navigation.switchToView("Withdrawal");
    }

    @FXML
    private void onCancel() throws IOException {
        WithdrawalInfo.getInstance().setAmount(0);
        Navigation.switchToView("Master");
    }

    @FXML
    private void confirm() throws Exception {
        withdraw();
    }

    @FXML
    private void confirmReceipt() throws Exception {
        if (withdraw()) {
            PDFFile f1 = new PDFFile();
            f1.createWithdrawInfoDeposit("Bezugsbeleg", "withdraw");
            OpenPDF oPdf = new OpenPDF(f1);
        }
    }

    private boolean withdraw() throws IOException {
        switch ((int)slider.getValue()) {
            case 0 -> setNoteSize(1);
            case 33 -> setNoteSize(2);
            case 66 -> setNoteSize(3);
            default -> setNoteSize(4);
        }
        lblError.setVisible(false);
        int[] banknotes = payout((int) WithdrawalInfo.getInstance().getAmount(), WithdrawalInfo.getInstance().getNoteSize());
        WithdrawalInfo.getInstance().setBanknotes(banknotes);
        int noteCount = 0;
        for(int i : banknotes) {
            noteCount += i;
        }
        boolean enoughInStock = Database.checkMoneystock(banknotes, WithdrawalInfo.getInstance().getCurrency().toString()); // error handling if moneyStock allows the withdrawal
        double amount = WithdrawalInfo.getInstance().getAmount();
        double amountInCHF;
        if (WithdrawalInfo.getInstance().getCurrency() == Currency.Euro) { // Euro -> CHF
            Double exRate = CurrencyAPI.getExRate();
            amountInCHF = amount * exRate;
        } else {
            amountInCHF = amount;
        }
        boolean enoughBalance = (Database.getBalance(Info.getAccountID()) >= amountInCHF);
        if (noteCount > 20) {
            return setError("Sie können sich nicht mehr als 20 Banknoten auf einmal auszahlen lassen.");
        } else if (!enoughInStock) {
            return setError("Der Automat hat nicht mehr genügend Noten für Ihre Anfrage, probieren Sie es später erneut.         Wir entschuldigen uns für die Unannehmlichkeiten.");
        } else if (!enoughBalance) {
            return setError("Ihr Kontostand ist zu niedrig für diesen Bezug!");
        } else {
            Database.updateBalance(Operation.withdraw, amountInCHF, Info.getAccountID()); // Abzug vom Konto
            Database.updateMoneyStock(Operation.withdraw, banknotes, WithdrawalInfo.getInstance().getCurrency()); // Abzug vom MoneyStock
            Database.insertTransaction(Operation.withdraw, WithdrawalInfo.getInstance().getCurrency(), amount, Info.getCardID());
            // Auszahlung
            printWithdrawal(banknotes);
            Navigation.switchToPayout();
            return true;
        }
    }

    private boolean setError(String msg) {
        root.requestFocus();
        lblError.setText(msg);
        lblError.setVisible(true);
        return false;
    }

    private int[] payout(int restAmount, int noteSize) {
        switch (noteSize) {
            case 1 -> {
                return new int[] { 0, 0, 0, 0, 0, restAmount / 10 };
            }
            case 2 -> {
                int twenty = restAmount / 20; restAmount = restAmount % 20;
                int ten = restAmount / 10;
                return new int[] { 0, 0, 0, 0, twenty, ten };
            }
            case 3 -> {
                int fifty = restAmount / 50; restAmount = restAmount % 50;
                int twenty = restAmount / 20; restAmount = restAmount % 20;
                int ten = restAmount / 10;
                return new int[] { 0, 0, 0, fifty, twenty, ten };
            }
            default ->  { // (case 4 & default)
                int thousand = 0;
                if (WithdrawalInfo.getInstance().getCurrency() == Currency.CHF) { // thousand bank note only if CHF
                    thousand = restAmount / 1000; restAmount = restAmount % 1000;
                }
                int twoHundred = restAmount / 200; restAmount = restAmount % 200;
                int hundred = restAmount / 100; restAmount = restAmount % 100;
                int fifty = restAmount / 50; restAmount = restAmount % 50;
                int twenty = restAmount / 20; restAmount = restAmount % 20;
                int ten = restAmount / 10;

                return new int[] { thousand, twoHundred, hundred, fifty, twenty, ten };
            }
        }
    }

    private void setNoteSize(int size) {
        WithdrawalInfo.getInstance().setNoteSize(size);
    }

    private void printWithdrawal(int[] notePayout) {
        System.out.println("Sie heben einen Betrag von " + formattedAmount + " " + WithdrawalInfo.getInstance().getCurrency() + " ab.");
        System.out.println("Notenausgabe: ");
        if (notePayout[0] > 0){
            System.out.println("1000er: " + notePayout[0]);
        }
        if (notePayout[1] > 0){
            System.out.println("200er: " + notePayout[1]);
        }
        if (notePayout[2] > 0){
            System.out.println("100er: " + notePayout[2]);
        }
        if (notePayout[3] > 0){
            System.out.println("50er: " + notePayout[3]);
        }
        if (notePayout[4] > 0){
            System.out.println("20er: " + notePayout[4]);
        }
        if (notePayout[5] > 0){
            System.out.println("10er: " + notePayout[5]);
        }
    }
}
