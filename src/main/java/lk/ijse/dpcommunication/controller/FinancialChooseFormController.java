package lk.ijse.dpcommunication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FinancialChooseFormController {

    public Button btnCosts;
    @FXML
    private Button btnPayLater;

    @FXML
    private Button btnPayNow;

    @FXML
    private AnchorPane financialChoosePane;

    @FXML
    void payLaterOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/payLater_form.fxml"));
        financialChoosePane.getChildren().clear();
        financialChoosePane.getChildren().add(Form);

    }

    @FXML
    void payNowOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/payInstance.fxml"));
        financialChoosePane.getChildren().clear();
        financialChoosePane.getChildren().add(Form);

    }

    public void getCostOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/costs_form.fxml"));
        financialChoosePane.getChildren().clear();
        financialChoosePane.getChildren().add(Form);

    }
}
