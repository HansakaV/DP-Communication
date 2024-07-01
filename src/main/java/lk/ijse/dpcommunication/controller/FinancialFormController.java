package lk.ijse.dpcommunication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.model.Financial;
import lk.ijse.dpcommunication.model.ItemDTO;
import lk.ijse.dpcommunication.model.order;
import lk.ijse.dpcommunication.repository.FinancialRepo;
import lk.ijse.dpcommunication.repository.customerRepo;
import lk.ijse.dpcommunication.repository.orderRepo;
import net.adeonatech.dto.*;
import net.adeonatech.dto.SendTextResponse;
import net.adeonatech.service.SendSMSImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.controlsfx.control.Notifications;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import net.adeonatech.dto.SendTextBody;
import net.adeonatech.dto.TokenBody;
import net.adeonatech.service.SendSMSImpl;
import org.controlsfx.control.Notifications;


import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

//import static lk.ijse.dpcommunication.controller.App.SENDER_ID;


public class FinancialFormController {

    public AnchorPane financialPane;
    public Label lblOrderDate;
    public Label lblmoneyRecivedDate;
    public TableColumn colOrderId;
    public TableColumn colCustomer;
    public TableColumn colPlacedDate;
    public TableColumn colRecievedMoney;
    public TableColumn colCost;
    public TableColumn ColToday;
    public TableColumn colArrears;
    public TableColumn colStatus;
    public TextField txtRecivedMoney;
    public ComboBox<String> cmbOrder;
    public Button btnReport;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colitems;
    public TableColumn colTotal;
    public ComboBox cmbCustomer;
    public TableColumn colRDate;
    public TableColumn colRmoney;
    public Label lblCustomerArrears;
    public Button btnReport1;
    public Label lblCustomerTotal;
    @FXML
    private Button btnBack;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnSendMessage;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<?> cmbStatus;

    @FXML
    private Label lblArrears;

    @FXML
    private Label lblCost;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblName;


    @FXML
    private TableView<ItemDTO> tblFinancial;

    private String[] statusList = new String[]{"Complete", "Incomplete"};
    private static final String USERNAME = "gayashanb";
    private static final String PASSWORD = "2001@Dhananka";
    private static final String SENDER_ID = "DP Communi";
    private static final ObjectMapper OBJECT_MAPPER = JacksonConfig.createObjectMapper();

    private String customerMobileNumber;


    public void initialize() throws SQLException {
        showStatus();
        setDate();
        getOrderCodes();
        getCustomers();
        setCellValueFactory();

    }
    private void setCellValueFactory(){
        colPlacedDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colitems.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colRDate.setCellValueFactory(new PropertyValueFactory<>("payDate"));
        colRmoney.setCellValueFactory(new PropertyValueFactory<>("receivedMoney"));
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

    private void getOrderCodes() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> codeList = orderRepo.getCodes();

            for (String code : codeList) {
                obList.add(code);
            }
            cmbOrder.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        lblmoneyRecivedDate.setText(String.valueOf(now));
    }

    @FXML
    void DeleteOnAction(ActionEvent event) {
        String oid = cmbOrder.getValue();
        try {
            boolean isDeleted = customerRepo.delete(oid);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted From System !");
            } else {
                new Alert(Alert.AlertType.WARNING, "Order Not Found !!");
            }
            tblFinancial.refresh();
        } catch (SQLException dp) {
            throw new RuntimeException(dp);
        }

    }

    @FXML
    void backOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        financialPane.getChildren().clear();
        financialPane.getChildren().add(Form);


    }

    @FXML
    void clearOnAction(ActionEvent event) {
        cmbOrder.setValue("");
        lblName.setText("");
        lblOrderDate.setText("");
        lblCost.setText("");
        txtRecivedMoney.setText("");
        lblArrears.setText("");
        lblmoneyRecivedDate.setText("");
        //cmbStatus.setValue();

    }

    @FXML
    void saveOnAction(ActionEvent event) {
        String oID = cmbOrder.getValue();
        String cusName = lblName.getText();
        String Odate = lblOrderDate.getText();
        double cost = Double.parseDouble(lblCost.getText());
        double recivedMoney = Double.parseDouble(txtRecivedMoney.getText());
        double arrears = Double.parseDouble(lblArrears.getText());
        String status = String.valueOf(cmbStatus.getValue());

        Financial financial = new Financial(oID, cusName, Odate, cost, recivedMoney, arrears, status);

        try {
            boolean isSaved = FinancialRepo.save(financial);
            if (isSaved) {
                showNotification("Confirmation", "Added To System !!", Pos.BOTTOM_RIGHT, "confirmation");
                clearFields();
            } else {
                showNotification("Warning", "Something Went Wrong !!", Pos.BOTTOM_RIGHT, "warning");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setCustomerTotal(String name) throws SQLException {
        double total = FinancialRepo.getTotal(name);
       lblCustomerTotal.setText(String.valueOf(total));

    }

    private void clearFields() {
        cmbOrder.setValue("");
        lblName.setText("");
        lblOrderDate.setText("");
        lblCost.setText("");
        txtRecivedMoney.setText("");
        lblArrears.setText("");
        lblmoneyRecivedDate.setText("");
        //cmbStatus.setValue();

    }

    @FXML
    void sendMessageOnAction(ActionEvent event) throws IOException, SQLException {
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
                "Your Payment LKR" + " " + txtRecivedMoney.getText() + " Successfully Received on " + lblmoneyRecivedDate.getText() + "\n" +
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

    // Method to generate a unique transaction ID
    private static long generateUniqueTransactionId() {
        // Using current time in milliseconds for unique transaction ID
        return System.currentTimeMillis();
    }

    private void getCustomerMobile() throws SQLException {
        String name = lblName.getText();
        String mobile = customerRepo.getMobile(name);
        this.customerMobileNumber = mobile;
        System.out.println(customerMobileNumber);
    }

    public void cmbOrderOnAction(ActionEvent actionEvent) {
        String desc = cmbOrder.getValue();

        try {
            order o1 = orderRepo.searchByCode(desc);
            // System.out.println(o1);
            if (o1 != null) {
                lblName.setText(o1.getCustomerId());
                lblOrderDate.setText(o1.getDate());
                lblCost.setText(String.valueOf(o1.getTotal()));
            }
            txtRecivedMoney.requestFocus();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void calculateOnAction(MouseEvent keyEvent) {

    }

    public void calculateArrearsOnAction(ActionEvent actionEvent) {
        String SreceivedMoney = txtRecivedMoney.getText();
        String SorderCost = lblCost.getText();

        double receivedMoney = Double.parseDouble(SreceivedMoney);
        double orderCost = Double.parseDouble(SorderCost);

        double arrears = receivedMoney - orderCost;
        System.out.println(arrears);
        lblArrears.setText(String.valueOf(arrears));

    }

    public void getReportOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        // Load JRXML file
        InputStream inputStream = getClass().getResourceAsStream("/reports/Financial.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load(inputStream);

        // Compile JasperReport from JRXML
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        // Fill report with data
        Map<String, Object> data = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());

        // Display the report in a viewer (optional)
        JasperViewer.viewReport(jasperPrint, false);
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

    public void cmbCustomerOnAction(ActionEvent actionEvent) throws SQLException {
        String name = String.valueOf(cmbCustomer.getValue());
        setCustomerTotal(name);
        List<ItemDTO> o1 = FinancialRepo.searchByName(name);
        System.out.println(o1);

        tblFinancial.getItems().clear();

        // Add items from itemList to tableView
        if (o1 != null) {
            tblFinancial.getItems().addAll(o1);
        }
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

    public void getCustomerReportOnAction(ActionEvent actionEvent) {
    }
}

