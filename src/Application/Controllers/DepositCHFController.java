package Application.Controllers;

import Application.Data.DepositInfo;
import Application.Utility.Navigation;
import Application.Utility.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DepositCHFController {

    @FXML
    private TextField tfThousand, tfTwoHundred, tfHundred, tfFifty, tfTwenty, tfTen;

    @FXML
    private Label lblError;

    @FXML
    private void confirm() throws IOException {
        lblError.setVisible(false);
        ArrayList<TextField> fields = new ArrayList<>(Arrays.asList(tfThousand, tfTwoHundred, tfHundred, tfFifty, tfTwenty, tfTen));
        // Check if at least one is not 0
        boolean isZero = true;
        boolean isNotNumeric = false;
        for (TextField tf : fields) {
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
            };
            int sum = notes[0] * 1000 + notes[1] * 200 + notes[2] * 100 + notes[3] * 50 + notes[4] * 20 + notes[5] * 10;
            DepositInfo.getInstance().setBanknotes(notes);
            DepositInfo.getInstance().setAmount(sum);
            Navigation.switchToView("");
        }


//    lblError.setText("Nur Zahlen eingeben");
//    DepositInfo.getInstance().setAmount();
    }

    @FXML
    private void toMaster() throws IOException {
        DepositInfo.getInstance().setAmount(0);
        Navigation.switchToView("Master");
    }

}