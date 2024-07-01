package lk.ijse.dpcommunication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.model.InstanceFinancialDTO;
import lk.ijse.dpcommunication.model.ItemDTO;
import lk.ijse.dpcommunication.repository.FinancialRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class InstancePayFormController {

    public Button btnIncomplete;
    @FXML
    private Button btnBack;

    @FXML
    private TableColumn<?, ?> colArrears;

    @FXML
    private TableColumn<?, ?> colCost;

    @FXML
    private TableColumn<?, ?> colCustomer;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colItem;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colReceivedMoney;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private AnchorPane financialPane;

    @FXML
    private TableView<InstanceFinancialDTO> tblInstancePay;

    @FXML
    void backOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/financialChoose_form.fxml"));
        financialPane.getChildren().clear();
        financialPane.getChildren().add(Form);

    }
    public void initialize() throws SQLException {
        setCellValueFactory();
        setTable();
    }
    private void setCellValueFactory(){
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("order_date"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colReceivedMoney.setCellValueFactory(new PropertyValueFactory<>("received_money"));
        colArrears.setCellValueFactory(new PropertyValueFactory<>("arrears"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    private void setTable() throws SQLException {
        List<InstanceFinancialDTO> o1 = FinancialRepo.getInstancePayments();
        //System.out.println(o1);

        tblInstancePay.getItems().clear();

        if (o1 != null) {
            tblInstancePay.getItems().addAll(o1);
        }
    }

    public void getIncompleteOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/IncompletepayInstance_form.fxml"));
        financialPane.getChildren().clear();
        financialPane.getChildren().add(Form);
    }
}
