package Application.Controllers;

import Application.Data.WithdrawalInfo;
import Application.Utility.Currency;
import Application.Utility.Navigation;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class PayoutController {

    @FXML
    private VBox rootVBox;
    private int noteCount = 0;
    private int sizeLimit = 0;
    int[] banknotes;
    private final ArrayList<HBox> listHBoxes = new ArrayList<>();

    @FXML
    private void initialize() {
        banknotes = WithdrawalInfo.getInstance().getBanknotes();
        for(int i : banknotes) {
            noteCount += i;
        }

        if (WithdrawalInfo.getInstance().getCurrency() == Currency.CHF) { // CHF
            sizeLimit = 8;

            int hBoxCount;
            if (noteCount % 8 == 0) hBoxCount = noteCount / 8;
            else hBoxCount = (noteCount / 8) + 1;
            fillHBoxList(hBoxCount);

            int imgWidth;
            if (noteCount <= 3) imgWidth = 160;
            else if (noteCount <= 5)  imgWidth = (832-(5*noteCount))/noteCount - 30;
            else if (noteCount <= 8)  imgWidth = (832-(5*noteCount))/noteCount;
            else if (noteCount <= 16) imgWidth = 99;
            else {
                rootVBox.setLayoutY(95);
                sizeLimit = 10;
                imgWidth = 78;
            }
            paintNotes(imgWidth);

        } else { // Euro
            sizeLimit = 4;

            int hBoxCount;
            if (noteCount % 4 == 0) hBoxCount = noteCount / 4;
            else hBoxCount = (noteCount / 4) + 1;
            fillHBoxList(hBoxCount);

            int imgWidth;
            if (noteCount <= 3) imgWidth = (832-(5*3))/3;
            else if (noteCount <= 16)  imgWidth = (832-(5*sizeLimit))/sizeLimit;
            else {
                sizeLimit = 5;
                imgWidth = (832 - (5 * sizeLimit)) / sizeLimit;
            }
            paintNotes(imgWidth);
        }
    }

    private void fillHBoxList(int hBoxCount) {
        for (int i = 0; i < hBoxCount; i++) {
            HBox h = new HBox();
            h.setSpacing(5);
            rootVBox.getChildren().add(h);
            listHBoxes.add(h);
        }
    }

    private void paintNotes(int imgWidth) {
        String imageCompressor = "png";
        if (WithdrawalInfo.getInstance().getCurrency() == Currency.CHF) {
            imageCompressor = "jpg";
            for(int i = 0; i < banknotes[0]; i++) {
                ImageView img = new ImageView(new Image("Application/Media/CHF/thousand.jpg", imgWidth, 0, true, false));
                addNote(img);
            }
        }
        for(int i = 0; i < banknotes[1]; i++) {
            ImageView img = new ImageView(new Image("Application/Media/" + WithdrawalInfo.getInstance().getCurrency() +"/twoHundred."+imageCompressor, imgWidth, 0, true, false));
            addNote(img);
        }
        for(int i = 0; i < banknotes[2]; i++) {
            ImageView img = new ImageView(new Image("Application/Media/" + WithdrawalInfo.getInstance().getCurrency() +"/hundred."+imageCompressor, imgWidth, 0, true, false));
            addNote(img);
        }
        for(int i = 0; i < banknotes[3]; i++) {
            ImageView img = new ImageView(new Image("Application/Media/" + WithdrawalInfo.getInstance().getCurrency() +"/fifty."+imageCompressor, imgWidth, 0, true, false));
            addNote(img);
        }
        for(int i = 0; i < banknotes[4]; i++) {
            ImageView img = new ImageView(new Image("Application/Media/" + WithdrawalInfo.getInstance().getCurrency() +"/twenty."+imageCompressor, imgWidth, 0, true, false));
            addNote(img);
        }
        for(int i = 0; i < banknotes[5]; i++) {
            ImageView img = new ImageView(new Image("Application/Media/" + WithdrawalInfo.getInstance().getCurrency() +"/ten."+imageCompressor, imgWidth, 0, true, false));
            addNote(img);
        }
    }

    private void addNote(ImageView image) {
        for (HBox hBox : listHBoxes) {
            if (hBox.getChildren().size() < sizeLimit) {
                hBox.getChildren().add(image);
                break;
            }
        }
    }

    @FXML
    private void onClick() throws IOException {
        Navigation.switchToView("TransactionSuccess");
    }
}
