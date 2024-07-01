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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.model.Financial;
import lk.ijse.dpcommunication.model.order;
import lk.ijse.dpcommunication.repository.FinancialRepo;
import lk.ijse.dpcommunication.repository.customerRepo;
import lk.ijse.dpcommunication.repository.orderRepo;
import net.adeonatech.dto.SendTextBody;
import net.adeonatech.dto.TokenBody;
import net.adeonatech.dto.TransactionBody;
import net.adeonatech.service.SendSMSImpl;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayNowFormController {

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<?> cmbStatus;

    @FXML
    private AnchorPane financialPane;

    @FXML
    private Label lblArrears;

    @FXML
    private Label lblCode;

    @FXML
    private Label lblCost;

    @FXML
    private Label lblName;

    @FXML
    private Label lblOrderDate;

    @FXML
    private TextField txtRecivedMoney;
    private String[] statusList = new String[]{"Complete", "Incomplete"};
    private String customerMobileNumber;

    public void initialize() throws SQLException {
        setStatus();
        setOrder();
        //setDetails();
    }


    @FXML
    void calculateArrearsOnAction(ActionEvent event) {
        String SreceivedMoney = txtRecivedMoney.getText();
        String SorderCost = lblCost.getText();

        double receivedMoney = Double.parseDouble(SreceivedMoney);
        double orderCost = Double.parseDouble(SorderCost);

        double arrears = orderCost - receivedMoney;
        System.out.println(arrears);
        lblArrears.setText(String.valueOf(arrears));

    }

    @FXML
    void calculateOnAction(MouseEvent event) {

    }

    @FXML
    void saveOnAction(ActionEvent event) throws IOException {
        String orderId = lblCode.getText();
        String  name = lblName.getText();
        String date = lblOrderDate.getText();
        double cost = Double.parseDouble(lblCost.getText());
        double receivedMoney = Double.parseDouble(txtRecivedMoney.getText());
        String status = String.valueOf(cmbStatus.getValue());
        double arrears = Double.parseDouble(lblArrears.getText());

        Financial financial = new Financial(orderId,name,date, cost,receivedMoney, arrears, status);

        try {
            boolean isSaved = FinancialRepo.save(financial);
            if (isSaved) {
                showNotification("Confirmation", "Added To System !!", Pos.BOTTOM_RIGHT, "confirmation");
                sendMessage();
                back();
            } else {
                showNotification("Warning", "Something Went Wrong !!", Pos.BOTTOM_RIGHT, "warning");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    private void back() throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/orders_from.fxml"));
        financialPane.getChildren().clear();
        financialPane.getChildren().add(Form);

    }
    private void setStatus(){
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
    private void setOrder(){
        try {
            String latestOrderID = orderRepo.getOrderID();
            lblCode.setText(latestOrderID);
            setDetails(latestOrderID);
            System.out.println("Latest Order ID: " + latestOrderID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setDetails(String id) throws SQLException {
            order o1 = orderRepo.searchByCode(id);
            if (o1 != null) {
                lblName.setText(o1.getCustomerId());
                lblOrderDate.setText(o1.getDate());
                lblCost.setText(String.valueOf(o1.getTotal()));
            }
            txtRecivedMoney.requestFocus();
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
                "Your Payment LKR" + " " + txtRecivedMoney.getText() + " Successfully Received on " + lblOrderDate.getText() + "Your Total Payable Amount LKR "+lblArrears.getText()+"."+ "\n" +
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
        String name = lblName.getText();
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
