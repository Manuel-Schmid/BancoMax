package Application.Controllers;

import Application.Data.DepositInfo;
import Application.Data.WithdrawalInfo;
import Application.Utility.Navigation;
import Application.Utility.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        btnConfirm.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                root.requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    @FXML
    private void confirm() throws IOException {
        lblError.setVisible(false);
        ArrayList<TextField> fields = new ArrayList<>(Arrays.asList(tfThousand, tfTwoHundred, tfHundred, tfFifty, tfTwenty, tfTen));
        boolean isZero = true;
        boolean isNotNumeric = false;
        for (TextField tf : fields) {
            tf.setText(Utils.zeroHandling(tf.getText().toCharArray()));
            if (tf.getText().isEmpty()) {
                tf.setText("0");
            } else {
                if (!Utils.isNumeric(tf.getText())) {
                    isNotNumeric = true;
                }
                isZero = false;
            }
        }
        if (isZero) {
            lblError.setText("Keine Note eingezahlt");
            lblError.setVisible(true);
            fields.forEach(tf -> {tf.setText("");});
        } else if (isNotNumeric) {
            lblError.setText("Nur Zahlen eingeben");
            lblError.setVisible(true);
            fields.forEach(tf -> {tf.setText("");});
        } else { // Success
            int[] notes = new int[6];
            int counter = 0;
            for (TextField tf : fields) {
                notes[counter] = Integer.parseInt(tf.getText());
                counter++;
            }
            int sum = notes[0] * 1000 + notes[1] * 200 + notes[2] * 100 + notes[3] * 50 + notes[4] * 20 + notes[5] * 10;
            DepositInfo.getInstance().setBanknotes(notes);
            DepositInfo.getInstance().setAmount(sum);
            Navigation.switchToView("DepositConfirm");
        }
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
        if (DepositInfo.getInstance().isAdmin()) {
            DepositInfo.getInstance().setAdmin(false);
            Navigation.switchToView("Admin");
        } else {
            Navigation.switchToView("Master");
        }
        DepositInfo.getInstance().setAmount(0);
    }
}
