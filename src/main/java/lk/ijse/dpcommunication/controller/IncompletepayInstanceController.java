package lk.ijse.dpcommunication.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.model.InstanceFinancialDTO;
import lk.ijse.dpcommunication.repository.FinancialRepo;
import lk.ijse.dpcommunication.repository.customerRepo;
import lk.ijse.dpcommunication.repository.profitsRepo;
import net.adeonatech.dto.SendTextBody;
import net.adeonatech.dto.TokenBody;
import net.adeonatech.dto.TransactionBody;
import net.adeonatech.service.SendSMSImpl;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class IncompletepayInstanceController {

    public Label lblDate;
    public Label lblArrears;
    public Button btnCollected;
    public ComboBox cmbCustomer;
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
    private AnchorPane financiaIncompletelPane;

    @FXML
    private TableView<InstanceFinancialDTO> tblInstancePay;

    @FXML
    void backOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/financialChoose_form.fxml"));
        financiaIncompletelPane.getChildren().clear();
        financiaIncompletelPane.getChildren().add(Form);

    }
    String orderDate;
    double arrears;
    double cost;
    String customerName;

    public void initialize() throws SQLException {
        setCellValueFactory();
        setTable();
        getNames();
        igGet();
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
        List<InstanceFinancialDTO> o1 = FinancialRepo.getInstancePaymentsViaCustomer();
        System.out.println(o1);
        for (InstanceFinancialDTO dto : o1) {
            // Extract values
             customerName = dto.getCustomer_name();
             orderDate = dto.getOrder_date();
            String itemName = dto.getItem_name();
            double unitPrice = dto.getUnit_price();
            double qty = dto.getQty();
             cost = dto.getCost();
            double receivedMoney = dto.getReceived_money();
             arrears = dto.getArrears();
            String status = dto.getStatus();
            }

        tblInstancePay.getItems().clear();

        if (o1 != null) {
            tblInstancePay.getItems().addAll(o1);
        }
    }
    String oID;
    private void igGet() throws SQLException {
           oID = profitsRepo.getId(customerName,cost);
    }

    public void collectOnAction(ActionEvent actionEvent) throws IOException {
       String customer = String.valueOf(cmbCustomer.getValue());
        try {
            boolean isSaved = profitsRepo.updateGetArrears(customer,true,arrears,cost);
            if (isSaved) {
                showNotification("Confirmation", "Added To System !!", Pos.BOTTOM_RIGHT, "confirmation");
                sendMessage();
            } else {
                showNotification("Warning", "Something Went Wrong !!", Pos.BOTTOM_RIGHT, "warning");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void getNames(){
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> cusList = profitsRepo.getInstanceName();

            for (String code : cusList) {
                obList.add(code);
            }
            cmbCustomer.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getCustomerOnAction(ActionEvent actionEvent) {
        lblArrears.setText(String.valueOf(arrears));
        lblDate.setText(orderDate);
    }
    private void sendMessage() throws IOException, SQLException {
        TokenBody tokenBody = new TokenBody();

        tokenBody.setUsername("gayashanb");
        tokenBody.setPassword("2001@Dhananka");

        SendSMSImpl SMS = new SendSMSImpl();
        String token = SMS.getToken(tokenBody).getToken();
        System.out.println(token);

        SendSMSImpl sendSMS = new SendSMSImpl();
        SendTextBody sendTextBody = new SendTextBody();
        getCustomerMobile();
        sendTextBody.setMsisdn(sendSMS.setMsisdns(new String[]{customerMobileNumber}));
        sendTextBody.setSourceAddress("DP Communi");

        sendTextBody.setMessage("Dear Sir/Madam ,\n" +
                "\n" +
                "Your Payment LKR" + " " + lblArrears.getText() + " Successfully Received on " + lblDate.getText() + "."+ "\n" +
                "\n" +
                "Questions? Contact us at 0713856863.\n" +
                "\n" +
                "Thanks for shopping with us!\n" +
                "\n" +
                "Best,\n" +
                "DP Communication\n" +
                "The Golden Mark Of Printing Art");
        long transactionId = generateUniqueTransactionId();
        sendTextBody.setTransaction_id(String.valueOf(transactionId));
        System.out.println(transactionId);
        TransactionBody transactionBody = new TransactionBody();
        transactionBody.setTransaction_id(String.valueOf(transactionId));
        String response = sendSMS.sendText(sendTextBody,
                sendSMS.getToken(tokenBody).getToken()).getStatus();
        switch (response) {
            case "success":
                showNotification("Confirmation", "Messsage Sent Successfully !!", Pos.BOTTOM_RIGHT, "confirmation");
                break;

            case "failure":
                showNotification("Warning", "Something Went Wrong !!", Pos.BOTTOM_RIGHT, "warning");
                break;
        }

    }
    private static long generateUniqueTransactionId() {
        // Using current time in milliseconds for unique transaction ID
        return System.currentTimeMillis();
    }
    String customerMobileNumber;


    private void getCustomerMobile() throws SQLException {
        String name = String.valueOf(cmbCustomer.getValue());
        String mobile = customerRepo.getMobile(name);
        this.customerMobileNumber = mobile;
        System.out.println(customerMobileNumber);
    }
    private void showNotification(String title, String text, Pos position, String type) {
        Notifications notification = Notifications.create()
                .title(title)
                .text(text)
                .position(position)
                .hideAfter(javafx.util.Duration.seconds(5))
                .onAction(event -> System.out.println(title + " notification clicked!"));

        if ("confirmation".equals(type)) {
            notification.darkStyle();
        } else if ("warning".equals(type)) {
            notification.showWarning();
        }

        notification.show();
    }


}
