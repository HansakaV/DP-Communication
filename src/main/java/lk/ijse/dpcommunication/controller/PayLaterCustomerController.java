package lk.ijse.dpcommunication.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.model.PayLaterDTO;
import lk.ijse.dpcommunication.model.PayLaterFinancialDTO;
import lk.ijse.dpcommunication.model.tm.cartTm;
import lk.ijse.dpcommunication.repository.FinancialRepo;
import lk.ijse.dpcommunication.repository.customerRepo;
import lk.ijse.dpcommunication.repository.orderRepo;
import net.adeonatech.dto.SendTextBody;
import net.adeonatech.dto.TokenBody;
import net.adeonatech.dto.TransactionBody;
import net.adeonatech.service.SendSMSImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayLaterCustomerController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnBill;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbCustomer;

    @FXML
    private ComboBox<?> cmbStatus;

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
    private Label lblArrears;

    @FXML
    private Label lblBalance;

    @FXML
    private Label lblDate;

    @FXML
    private TableView<PayLaterFinancialDTO> tblInstancePay;

    @FXML
    private TextField txtReceivedMoney;
    private String customerMobileNumber;
    private String[] statusList = new String[]{"Complete", "Incomplete"};

    LocalDate now = LocalDate.now();
    private String Invoice = null;

   /* private void getCurrentBillId(){
        try{
            String id = orderRepo.getCurrentBillId();
            String nextId = generateNextBillId(id);
            Invoice = nextId;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }*/
   private String generateNextBillId(String id) {
       if (id != null && id.startsWith("DP")) {
           String numericPart = id.substring(2); // Extract numeric part (after "DP")
           int idNum = Integer.parseInt(numericPart); // Convert to integer
           return String.format("DP%03d", ++idNum); // Increment and format with leading zeros
       }
       return "DP001"; // Default to DP001 if no valid ID exists
   }
   private void saveId(String id) {
       try {
           orderRepo.saveBillId(id);
           System.out.println("Bill ID saved: " + id);
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
   }

    @FXML
    void GenarateBillOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/reports/Pay_Later_DP.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        String currentBillId = orderRepo.getCurrentBillId();
        String nextBillId = generateNextBillId(currentBillId);
        saveId(nextBillId);

        Invoice = nextBillId;

        // Print or log the new bill ID (for debugging purposes)
        System.out.println("Generated Bill ID: " + Invoice);

        // Prepare parameters for the report
        Map<String, Object> data = new HashMap<>();
        data.put("customerName", cmbCustomer.getValue());
        data.put("Today", now.toString());
        data.put("InvoiceID", Invoice);



        // Fill report with data from database connection
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());

        // View the report using JasperViewer (optional, for testing)
        JasperViewer.viewReport(jasperPrint, false);


    }

    @FXML
    void backOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/financialChoose_form.fxml"));
        financialPane.getChildren().clear();
        financialPane.getChildren().add(Form);


    }

    @FXML
    void cmbCustomerOnAction(ActionEvent event) throws SQLException {
        setTable();
        setBalance();


    }

    @FXML
    void updateDatabaseOnAction(ActionEvent event) throws SQLException, IOException {
        ObservableList<PayLaterFinancialDTO> items = tblInstancePay.getItems();
        String date = null;
        String orderId = null;
        String customerId =null;
        String description = null;
        double quantity = 0;
        double unitPrice = 0;
        double total = 0;
        String payment = null;
        double updatedTot= 0;


        for (PayLaterFinancialDTO item : items) {
             date = item.getDate();
             orderId = item.getOrder_id();
             customerId = item.getCustomer_name();
             description = item.getItem_name();
             quantity = item.getQty();
             unitPrice = item.getUnit_price();
             total = item.getTotal();
             payment =item.getPayment();
             updatedTot = item.getUpdated_total();

            }
        double receivedMoney = Double.parseDouble(txtReceivedMoney.getText());
        double arrears = Double.parseDouble(lblArrears.getText());
        PayLaterDTO payLaterDTO = new PayLaterDTO(date,orderId,customerId,description,quantity,unitPrice,total,payment,receivedMoney,arrears,updatedTot);

        try {
            boolean isSaved = FinancialRepo.savePayLater(payLaterDTO);
            if (isSaved) {
                showNotification("Confirmation", "DataBase Updated Successfully  !!", Pos.BOTTOM_RIGHT, "confirmation");
                sendMessage();
            } else {
                showNotification("Warning", "Something Went Wrong !!", Pos.BOTTOM_RIGHT, "warning");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void initialize() throws SQLException {
        setDate();
        setCellValueFactory();
        getCustomers();
        showStatus();
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
    private void setDate(){
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));
    }
    private void getCustomers(){
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> cusList = customerRepo.getNames();

            for (String code : cusList) {
                obList.add(code);
            }
            cmbCustomer.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private void setTable() throws SQLException {
        String name = String.valueOf(cmbCustomer.getValue());
        System.out.println(name);
        List<PayLaterFinancialDTO> o1 = FinancialRepo.getPayLaterViaCustomer(name);
        System.out.println(o1);

        tblInstancePay.getItems().clear();

        if (o1 != null) {
            tblInstancePay.getItems().addAll(o1);
        }
    }
    private void setBalance() throws SQLException {
        String name = String.valueOf(cmbCustomer.getValue());
        double balance = FinancialRepo.getCustomerBalance(name);
        lblBalance.setText(String.valueOf(balance));
    }
    public void showStatus() {
        List<String> listS = new ArrayList();
        String[] var2 = this.statusList;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String data = var2[var4];
            listS.add(data);
        }

        ObservableList listStatus = FXCollections.observableArrayList(listS);
        this.cmbStatus.setItems(listStatus);

    }
    public void calculateArrearsOnAction() {
        String SreceivedMoney = txtReceivedMoney.getText();
        String SorderCost = lblBalance.getText();

        double receivedMoney = Double.parseDouble(SreceivedMoney);
        double orderCost = Double.parseDouble(SorderCost);

        double arrears = orderCost - receivedMoney;
        lblArrears.setText(String.valueOf(arrears));

    }
    public void sendMessage() throws IOException, SQLException {
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
        System.out.println("Working");
        sendTextBody.setMessage("Dear Sir/Madam ,\n" +
                "\n" +
                "Your Payment LKR" + " " + txtReceivedMoney.getText() + " Successfully Received on " + lblDate.getText() +"."+ "Your Total Payable Amount LKR " +" "+ lblArrears.getText() + "\n" +
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

    private void getCustomerMobile() throws SQLException {
        String name = cmbCustomer.getValue();
        String mobile = customerRepo.getMobile(name);
        this.customerMobileNumber = mobile;
        System.out.println(customerMobileNumber);
    }

    public void getStatusOnAction(ActionEvent actionEvent) {
        calculateArrearsOnAction();
    }
}
