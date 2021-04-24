package Application.Controllers;

import Application.Utility.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DepositCHFController {

    @FXML
    private TextField tfThousand, tfTwoHundred, tfHundred, tfFifty, tfTwenty, tfTen;
    @FXML
    private Button btnConfirm;
    @FXML
    private BorderPane root;
    @FXML
    private Label lblError;

    @FXML
    private void initialize() {
        Utils.moveFocus(btnConfirm, root);
    }

    @FXML
    private void confirm() throws IOException {
        DepositEuroController.depositConfirm(lblError, tfThousand, tfTwoHundred, tfHundred, tfFifty, tfTwenty, tfTen);
    }

    @FXML
    private void addNote(MouseEvent event) {
        int note = Integer.parseInt(event.getPickResult().getIntersectedNode().getId()); // Id von ImageView auslesen
        int count;
        switch(note) {
            case 1000:
                try { count = Integer.parseInt(tfThousand.getText()); }
                catch (Exception e) { count = 0; }
                tfThousand.setText(String.valueOf(count + 1));
                break;
            case 200:
                try { count = Integer.parseInt(tfTwoHundred.getText()); }
                catch (Exception e) { count = 0; }
                tfTwoHundred.setText(String.valueOf(count + 1));
                break;
            case 100:
                try { count = Integer.parseInt(tfHundred.getText()); }
                catch (Exception e) { count = 0; }
                tfHundred.setText(String.valueOf(count + 1));
                break;
            case 50:
                try { count = Integer.parseInt(tfFifty.getText()); }
                catch (Exception e) { count = 0; }
                tfFifty.setText(String.valueOf(count + 1));
                break;
            case 20:
                try { count = Integer.parseInt(tfTwenty.getText()); }
                catch (Exception e) { count = 0; }
                tfTwenty.setText(String.valueOf(count + 1));
                break;
            case 10:
                try { count = Integer.parseInt(tfTen.getText()); }
                catch (Exception e) { count = 0; }
                tfTen.setText(String.valueOf(count + 1));
                break;
        }
    }

    @FXML
    private void back() throws IOException {
        DepositEuroController.goBack();
    }
}
