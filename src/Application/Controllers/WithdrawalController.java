package Application.Controllers;

import Application.Data.WithdrawalInfo;
import Application.Utility.Navigation;
import Application.Utility.Utils;
import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.io.IOException;

public class WithdrawalController {

    @FXML
    private Button btnConfirm, btnBack, btnCustomAmount;
    @FXML
    private Label lblSuccess, lblError, lblCurrency;
    @FXML
    private TextField tfAmount;
    @FXML
    private BorderPane root;
    @FXML
    private JFXSlider slider;

    private boolean success = false;

    @FXML
    private void initialize() {
        lblCurrency.setText("Währung: " + WithdrawalInfo.getInstance().getCurrency().toString());
        slider.setLabelFormatter(Utils.getStringConverter());

        slider.valueProperty().addListener(e -> {
            Color imageColor = Color.rgb(250, 166, 0).interpolate(Color.rgb(230, 0, 60),
                    slider.getValue() / 100);
            slider.setStyle("-fx-custom-color : " + Utils.colorToHex(imageColor) + ";");
        });

        Utils.moveFocus(btnBack, root);
    }

    @FXML
    private void keyPressed(KeyEvent ke) throws IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            if (!btnConfirm.isDisabled()){
                onConfirm();
            }
        }
    }

    @FXML
    private void textKeyPressed(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            btnCustomAmount.requestFocus();
            setCustomAmount();
        }
    }

    @FXML
    private void toMaster() throws IOException {
        WithdrawalInfo.getInstance().setAmount(0);
        Navigation.switchToView("Master");
    }

    @FXML
    private void onConfirm() throws IOException {
        switch ((int)slider.getValue()) {
            case 0 -> setNoteSize(1);
            case 33 -> setNoteSize(2);
            case 66 -> setNoteSize(3);
            default -> setNoteSize(4);
        }
        Navigation.switchToView("WithdrawalConfirm");
    }

    private void setNoteSize(int size) {
        WithdrawalInfo.getInstance().setNoteSize(size);
    }

    @FXML
    private void setAmount(ActionEvent event) {
        hideSuccess();
        String s = ((Labeled)event.getTarget()).getText();
        s = s.replace("'", "");
        int i = Integer.parseInt(s);
        WithdrawalInfo.getInstance().setAmount(i);
        btnConfirm.setDisable(false);
    }

    private void hideSuccess() {
        if(success) {
            lblSuccess.setVisible(false);
            success = false;
        }
    }

    private void setError(String msg) {
        lblError.setText(msg);
        btnConfirm.setDisable(true);
        lblError.setVisible(true);
    }

    @FXML
    private void setCustomAmount() {
        hideSuccess();
        tfAmount.setText(Utils.zeroHandling(tfAmount.getText().toCharArray()));
        if (tfAmount.getText().isEmpty()) {
            setError("Betrag eingeben!");
        } else if (!Utils.isNumeric(tfAmount.getText())) {
            setError("Falsches Format!");
        } else {
            boolean isInt = false;
            int cusAmount = 0;
            try {
                cusAmount = Integer.parseInt(tfAmount.getText());
                isInt = true;
            } catch (NumberFormatException e) {
                setError("Nur Ganzzahlen verwenden!");
            }
            if (isInt) {
                if (cusAmount > 2000) { // Amount greater than 2000
                    setError("Höchstbetrag: 2000");
                } else if (cusAmount % 10 != 0) { // Amount is not divisible by 10
                    setError("Betrag nicht in Noten auszahlbar!");
                } else { // Success
                    WithdrawalInfo.getInstance().setAmount(cusAmount);
                    btnConfirm.setDisable(false);
                    lblError.setVisible(false);
                    lblSuccess.setVisible(true);
                    success = true;
                }
            }
        }
    }
}
