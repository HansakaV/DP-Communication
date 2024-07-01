package lk.ijse.dpcommunication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.model.PayLaterFinancialDTO;
import lk.ijse.dpcommunication.repository.FinancialRepo;
import lk.ijse.dpcommunication.repository.dashboardRepo;
import lk.ijse.dpcommunication.repository.profitsRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ProfitsFormController {

    public Label lblTotal;
    public Label lblDate;
    public Label lblNetProfit;
    public Label lblAmountsReceivable;
    @FXML
    private Button btnBack;

    @FXML
    private Button btnReport;

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
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colUpdatedTot;

    @FXML
    private AnchorPane financialPane;

    @FXML
    private Label lblLaterArrears;

    @FXML
    private Label lblinstanceArrears;

    @FXML
    private Label lbltotalReceived;

    @FXML
    private TableView<PayLaterFinancialDTO> tblInstancePay;

    @FXML
    void backOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/adminDashboard_form.fxml"));
        financialPane.getChildren().clear();
        financialPane.getChildren().add(Form);

    }

    @FXML
    void getReportOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/monthReport_form.fxml"));
        financialPane.getChildren().clear();
        financialPane.getChildren().add(Form);


    }
    public void initialize() throws SQLException {
        setDate();
        setLabels();
        setCellValueFactory();
        setTable();
    }
    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));

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
        LocalDate now = LocalDate.now();
        List<PayLaterFinancialDTO> o1 = FinancialRepo.getProfit(String.valueOf(now));
        System.out.println(o1);

        tblInstancePay.getItems().clear();

        if (o1 != null) {
            tblInstancePay.getItems().addAll(o1);
        }
    }

    private void setLabels() throws SQLException {
        String date = lblDate.getText();

        double todayIncome = dashboardRepo.getTodayTotal(date);
        lblTotal.setText(String.valueOf(todayIncome));

        double todayReceivedInstance = dashboardRepo.getTodayReceivedInstance(date);
        double todayReceivedLater = dashboardRepo.getTodayReceivedLater(date);
        double TottodayReceived = todayReceivedLater+todayReceivedInstance;

        lbltotalReceived.setText(String.valueOf(TottodayReceived));

        double instanceArrears = dashboardRepo.getTodayInstanceArrears(date);
        lblinstanceArrears.setText(String.valueOf(instanceArrears));

        double laterArrears = dashboardRepo.getTodayLaterArrears(date);
        lblLaterArrears.setText(String.valueOf(laterArrears));

        double amountReceivable = dashboardRepo.getTodayReceivableAmount(date);
        lblAmountsReceivable.setText(String.valueOf(amountReceivable));

        double cost = profitsRepo.getTodayCosts(date);
        double netBalance = todayIncome - cost;
        lblNetProfit.setText(String.valueOf(netBalance));



    }

}
