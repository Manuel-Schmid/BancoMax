package Application.Controllers;

import Application.Data.CurrencyAPI;
import Application.Data.Database;
import Application.Data.Info;
import Application.Data.Transaction;
import Application.Utility.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private PieChart currenciesChart, euroChart, chfChart;
    @FXML
    private Label valueTotalCHF, valueCHF, valueEuro, lblTitleLineChart;
    @FXML
    private StackPane stackpane;
    @FXML
    private Button btnPWChange, btnBack;
    @FXML
    private BorderPane root;
    @FXML
    private LineChart<String, Integer> lineChart;
    @FXML
    private NumberAxis yAxis;

    @FXML
    private void initialize() {

        ObservableList<PieChart.Data> currenciesData
                = FXCollections.observableArrayList(
                        new PieChart.Data("CHF  ", Database.getTransactionCurrencyCount(Currency.CHF)),
                        new PieChart.Data("Euro  ", Database.getTransactionCurrencyCount(Currency.Euro))
        );
        currenciesChart.setData(currenciesData);

        ObservableList<PieChart.Data> chfData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Bezüge", Database.getTransactionOperationsCount(Currency.CHF, Operation.withdraw)),
                        new PieChart.Data("Einzahlungen", Database.getTransactionOperationsCount(Currency.CHF, Operation.deposit))
        );
        chfChart.setData(chfData);

        ObservableList<PieChart.Data> euroData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Bezüge", Database.getTransactionOperationsCount(Currency.Euro, Operation.withdraw)),
                        new PieChart.Data("Einzahlungen", Database.getTransactionOperationsCount(Currency.Euro, Operation.deposit))
        );
        euroChart.setData(euroData);

        int worthCHF = calcValue(Database.getMoneystock(Currency.CHF));
        int worthEuro = calcValue(Database.getMoneystock(Currency.Euro));

        valueCHF.setText(Utils.formatMoney(worthCHF) + " CHF");
        valueEuro.setText(Utils.formatMoney(worthEuro) + " Euro");
        Double exRate = CurrencyAPI.getExRate();
        valueTotalCHF.setText(Utils.formatMoney((worthEuro * exRate) + worthCHF) + " CHF");

        // Line chart
        lblTitleLineChart.setText("Transaktionenübersicht " + Utils.getFormattedMonth());

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        int currentMonth = Utils.getMonthInt(new Date());
        int daysOfMonth = Utils.getMonthDayCount();
        int highestTransCount = 0;
        for (int day = 1; day <= daysOfMonth; day++) {
            int transactionCount = Database.getTransactionCount(day, currentMonth);
            if (transactionCount > highestTransCount) { highestTransCount = transactionCount; }
            series.getData().add(new XYChart.Data(day + "." , transactionCount));
        }
        lineChart.getData().add(series);
        yAxis.setAutoRanging(false);
        yAxis.setTickUnit(2);
        yAxis.setUpperBound(highestTransCount + 2);

        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        btnPWChange.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                root.requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    private int calcValue(int[] banknotes) {
        return banknotes[0] * 1000 + banknotes[1] * 200 + banknotes[2] * 100 + banknotes[3] * 50 + banknotes[4] * 20 + banknotes[5] * 10;
    }

    private static JFXDialog dialog;
    JFXTextField tf;
    @FXML
    private void resetAdminPW() {
        disableButtons(true);
        tf = new JFXTextField();
        tf.setPromptText("Neues Passwort");
        tf.getStyleClass().add("dialog-tf");
        tf.setFocusColor(Color.rgb(255, 206, 0));
        JFXDialogLayout content = new JFXDialogLayout();
        content.setBody(tf);
        content.getStyleClass().add("dialog-content");
        dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton buttonConf = new JFXButton("Bestätigen");
        JFXButton buttonCancel = new JFXButton("Abbrechen");
        buttonConf.getStyleClass().add("dialog-button");
        buttonCancel.getStyleClass().add("dialog-button");

        buttonConf.setOnAction(actionEvent -> {
            if(tf.getText().isEmpty()) {
                invalid("Ungültig");
            } else if(tf.getText().length() < 6) {
                invalid("Zu kurz (min. 6)");
            } else {
                byte[] salt = Security.createSalt();
                byte[] hash;
                try {
                    hash = Security.hash(tf.getText(), salt);
                    Database.changeAdminPW(hash, salt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tf.setStyle("-fx-prompt-text-fill: #00ff2c");
                tf.setText("Erfolgreich");
                dialog.close();
                disableButtons(false);
                tf.setStyle("-fx-prompt-text-fill: white");
            }
        });
        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tf.setText("");
                tf.setStyle("-fx-prompt-text-fill: white");
                dialog.close();
                disableButtons(false);
            }
        });

        content.setActions(buttonCancel, buttonConf);
        dialog.show();
        dialog.toFront();
    }

    @FXML
    private void toAdmin() throws IOException {
        Navigation.switchToView("Admin");
    }

    private void invalid(String msg) {
        tf.setText("");
        tf.setStyle("-fx-prompt-text-fill: red");
        tf.setPromptText(msg);
    }

    private void disableButtons(boolean b) {
        btnBack.setDisable(b);
        btnPWChange.setDisable(b);
    }
}
