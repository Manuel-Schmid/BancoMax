package Application.Controllers;

import Application.Data.Database;
import Application.Data.Info;
import Application.Data.Transaction;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;

import java.util.ArrayList;

public class TransactionHistoryController {

    @FXML
    private JFXTreeTableView tableView;

    ObservableList<Transaction> data;

    @FXML
    private void initialize() { // Datum, Aktion, Betrag
        TreeTableColumn dateCol = new TreeTableColumn("Datum");
        TreeTableColumn actionCol = new TreeTableColumn("Transaktionsart");
        TreeTableColumn amountCol = new TreeTableColumn("Betrag in CHF");

        tableView.getColumns().addAll(dateCol, actionCol, amountCol);

        data = FXCollections.observableArrayList(Database.getTransactions(Info.getCardID()));

        dateCol.setCellValueFactory(
                new TreeItemPropertyValueFactory<Transaction, String>("date")
        );
        actionCol.setCellValueFactory(
                new TreeItemPropertyValueFactory<Transaction, String>("action")
        );
        amountCol.setCellValueFactory(
                new TreeItemPropertyValueFactory<Transaction, String>("amount")
        );

        dateCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        actionCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        amountCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));

        // build Tree
        TreeItem<Transaction> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);

        tableView.setRoot(root);
        tableView.setShowRoot(false);

        dateCol.setComparator(dateCol.getComparator().reversed());
        tableView.getSortOrder().add(dateCol);

        applyColumnSettings(dateCol);
        applyColumnSettings(actionCol);
        applyColumnSettings(amountCol);
    }

    private void applyColumnSettings(TreeTableColumn ttc) {
        ttc.setSortable(false);
        ttc.setResizable(false);
        ttc.setReorderable(false);
    }

}
