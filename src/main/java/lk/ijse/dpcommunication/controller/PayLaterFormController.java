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
import lk.ijse.dpcommunication.model.PayLaterFinancialDTO;
import lk.ijse.dpcommunication.repository.FinancialRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PayLaterFormController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnIncomplete;

    @FXML
    private TableColumn<?, ?> colCustomer;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colItem;

    @FXML
    private TableColumn<?, ?> colOrder;

    @FXML
    private TableColumn<?, ?> colPayment;

    @FXML
    private TableColumn<PayLaterFinancialDTO, Double> colQty;

    @FXML
    private TableColumn<PayLaterFinancialDTO, Double> colTotal;

    @FXML
    private TableColumn<PayLaterFinancialDTO, Double> colUnitPrice;

    @FXML
    private TableColumn<PayLaterFinancialDTO, Double> colUpdatedTot;

    @FXML
    private AnchorPane financialPane;

    @FXML
    private TableView<PayLaterFinancialDTO> tblInstancePay;

    @FXML
    void backOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/financialChoose_form.fxml"));
        financialPane.getChildren().clear();
        financialPane.getChildren().add(Form);


    }

    @FXML
    void getCustomerViceOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/payLaterCustomer_form.fxml"));
        financialPane.getChildren().clear();
        financialPane.getChildren().add(Form);


    }
    public void initialize() throws SQLException {
        setCellValueFactory();
        setTable();
    }
    private void setCellValueFactory(){
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOrder.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPayment.setCellValueFactory(new PropertyValueFactory<>("payment"));
        colUpdatedTot.setCellValueFactory(new PropertyValueFactory<>("updated_total"));
    }
    private void setTable() throws SQLException {
        List<PayLaterFinancialDTO> o1 = FinancialRepo.getPayLater();
        System.out.println(o1);

        tblInstancePay.getItems().clear();

        if (o1 != null) {
            tblInstancePay.getItems().addAll(o1);
        }
    }


}
