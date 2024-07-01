package lk.ijse.dpcommunication.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.model.PayLaterFinancialDTO;
import lk.ijse.dpcommunication.repository.FinancialRepo;
import lk.ijse.dpcommunication.repository.profitsRepo;
import lk.ijse.dpcommunication.repository.profitsRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MonthReportFormController {

    public Label lblNetProfit;
    public ComboBox cmbYear;
    public Label lblThisMonth;
    public Label lblSelectedMonth;
    public Label lblSelectedMonth1;
    @FXML
    private Button btnBack;

    @FXML
    private Button btnReport;

    @FXML
    private ComboBox<?> cmbMonth;

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
    private Label lblAmountsReceivable;

    @FXML
    private Label lblLaterArrears;

    @FXML
    private Label lblNetTot;

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
    void getReportOnAction(ActionEvent event) {

    }

    @FXML
    void selectMonthOnAction(ActionEvent event) throws SQLException {
        String year = String.valueOf(cmbYear.getValue());
        String month = String.valueOf(cmbMonth.getValue());
        setTable(year,month);
        setLabels();
        choose();


    }
    private String[] years = new String[]{"2024", "2025","2026","2027","2028","2029","2030"};
    private String[] months = new String[]{"01", "02","03","04","05", "06","07","08","09", "10","11","12"};


    public void initialize() throws SQLException {
        setCellValueFactory();
        setStatus1();
        showStatus2();
        thisMonth();

    }
    public void setStatus1() {
        List<String> listS = new ArrayList();
        String[] var2 = this.years;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String data = var2[var4];
            listS.add(data);
        }

        ObservableList listStatus = FXCollections.observableArrayList(listS);
        this.cmbYear.setItems(listStatus);

    }
    public void showStatus2() {
        List<String> listS = new ArrayList();
        String[] var2 = this.months;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String data = var2[var4];
            listS.add(data);
        }

        ObservableList listStatus = FXCollections.observableArrayList(listS);
        this.cmbMonth.setItems(listStatus);

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
    private void setTable(String year , String month) throws SQLException {
        List<PayLaterFinancialDTO> o1 = FinancialRepo.getProfitMonthly(year,month);
        System.out.println(o1);

        tblInstancePay.getItems().clear();

        if (o1 != null) {
            tblInstancePay.getItems().addAll(o1);
        }
    }


    public void selectYearOnAction(ActionEvent actionEvent) {
    }

    private void setLabels() throws SQLException {
        String year = String.valueOf(cmbYear.getValue());
        String month = String.valueOf(cmbMonth.getValue());

        double todayIncome = profitsRepo.getMonthlyTotal(year,month);
        lblNetTot.setText(String.valueOf(todayIncome));

        double todayReceivedInstance = profitsRepo.getMonthlyReceivedInstance(year,month);
        double todayReceivedLater = profitsRepo.getMonthlyReceivedLater(year,month);
        double TottodayReceived = todayReceivedLater+todayReceivedInstance;

        lbltotalReceived.setText(String.valueOf(TottodayReceived));

        double instanceArrears = profitsRepo.getMonthlyInstanceArrears(year,month);
        lblinstanceArrears.setText(String.valueOf(instanceArrears));

        double laterArrears = profitsRepo.getMonthlyLaterArrears(year,month);
        lblLaterArrears.setText(String.valueOf(laterArrears));

        double amountReceivable = profitsRepo.getMonthlyReceivableAmount(year,month);
        lblAmountsReceivable.setText(String.valueOf(amountReceivable));

        double cost = profitsRepo.getMonthlyCosts(year,month);
        double netBalance = todayIncome - cost;
        lblNetProfit.setText(String.valueOf(netBalance));
    }
    private void thisMonth(){
        LocalDate now = LocalDate.now();

        // Define the formatter to format the date as "YYYY MMMM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM");

        // Format the current date
        String formattedDate = now.format(formatter);

        // Set the formatted date to the JLabel
        lblThisMonth.setText(formattedDate);
    }
    private void choose(){
        String year = cmbYear.getValue().toString();
        lblSelectedMonth.setText(year);

        String monthString = String.valueOf(cmbMonth.getValue()); // Assuming cmbMonth.getValue() returns an integer
        int month;
        try {
            month = Integer.parseInt(monthString);
        } catch (NumberFormatException e) {
            month = -1; // Set to an invalid month to handle parsing errors
        }

        String monthName;
        switch (month) {
            case 1:
                monthName = "January";
                break;
            case 2:
                monthName = "February";
                break;
            case 3:
                monthName = "March";
                break;
            case 4:
                monthName = "April";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "June";
                break;
            case 7:
                monthName = "July";
                break;
            case 8:
                monthName = "August";
                break;
            case 9:
                monthName = "September";
                break;
            case 10:
                monthName = "October";
                break;
            case 11:
                monthName = "November";
                break;
            case 12:
                monthName = "December";
                break;
            default:
                monthName = "Invalid month";
                break;
        }

        lblSelectedMonth1.setText(monthName);

    }

}
