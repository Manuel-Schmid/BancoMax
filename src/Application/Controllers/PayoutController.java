package Application.Controllers;

import Application.Data.WithdrawalInfo;
import Application.Utility.Currency;
import Application.Utility.Navigation;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class PayoutController {

    @FXML
    private JFXMasonryPane notePane;
    private int noteCount = 0;

    @FXML
    private void initialize() {
        int[] banknotes = WithdrawalInfo.getInstance().getBanknotes();
        for(int i: banknotes) {
            noteCount += i;
        }
        System.out.println(noteCount);
        if (WithdrawalInfo.getInstance().getCurrency() == Currency.CHF) {
            for(int i = 0; i < banknotes[0]; i++) {
                ImageView img = new ImageView(new Image("Application/Media/CHF/thousand.jpg"));
                addNote(img);
            }
        }
        for(int i = 0; i < banknotes[1]; i++) {
            ImageView img = new ImageView(new Image("Application/Media/" + WithdrawalInfo.getInstance().getCurrency() +"/twoHundred.jpg"));
            addNote(img);
        }
        for(int i = 0; i < banknotes[2]; i++) {
            ImageView img = new ImageView(new Image("Application/Media/" + WithdrawalInfo.getInstance().getCurrency() +"/hundred.jpg"));
            addNote(img);
        }
        for(int i = 0; i < banknotes[3]; i++) {
            ImageView img = new ImageView(new Image("Application/Media/" + WithdrawalInfo.getInstance().getCurrency() +"/fifty.jpg"));
            addNote(img);
        }
        for(int i = 0; i < banknotes[4]; i++) {
            ImageView img = new ImageView(new Image("Application/Media/" + WithdrawalInfo.getInstance().getCurrency() +"/twenty.jpg"));
            addNote(img);
        }
        for(int i = 0; i < banknotes[5]; i++) {
            ImageView img = new ImageView(new Image("Application/Media/" + WithdrawalInfo.getInstance().getCurrency() +"/ten.jpg"));
            addNote(img);
        }
    }

    @FXML
    private void onClick() throws IOException {
        Navigation.switchToView("TransactionSuccess");
    }

    private void addNote(ImageView image) {
        image.setFitHeight(470/noteCount);
        image.setFitWidth(832/noteCount);
        notePane.getChildren().add(image);
    }
}
