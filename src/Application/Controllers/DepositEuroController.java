package Application.Controllers;

import Application.Data.DepositInfo;
import Application.Data.WithdrawalInfo;
import Application.Utility.Navigation;
import javafx.fxml.FXML;

import java.io.IOException;

public class DepositEuroController {

    @FXML
    private void confirm() {

    }

    @FXML
    private void toMaster() throws IOException {
        DepositInfo.getInstance().setAmount(0);
        Navigation.switchToView("Master");
    }


}
