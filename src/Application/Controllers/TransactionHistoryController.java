package Application.Controllers;

import Application.Data.Database;
import Application.Data.Info;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class TransactionHistoryController {

    @FXML
    private ListView timestampList, actionList, amountList;

    @FXML
    private void initialize() {
        ArrayList<String> transactions = Database.getTransactions(Info.getCardID());
        Collections.reverse(transactions);
        int counter = 1;
        for (String transaction : transactions) {
            if (counter <= 10) {
                String[] arr = transaction.split(";");
                timestampList.getItems().add(arr[0]);
                actionList.getItems().add(arr[1]);
                amountList.getItems().add(arr[2] + " CHF");
                counter++;
            } else { break; }
        }
    }

}
