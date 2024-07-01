package lk.ijse.dpcommunication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.repository.dashboardRepo;
import lk.ijse.dpcommunication.repository.profitsRepo;
import net.adeonatech.dto.SendTextBody;
import net.adeonatech.dto.TokenBody;
import net.adeonatech.dto.TransactionBody;
import net.adeonatech.service.SendSMSImpl;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DashboardFormController {

    public Label lblDate;
    public Button btnAdmin;
    public Button btnEndDay;
    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnFinancial;

    @FXML
    private Button btnItems;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btndashboard;

    @FXML
    private AnchorPane childrenpane;

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private Label lblA3;

    @FXML
    private Label lblA4;


    @FXML
    private Label lblIncomeMonth;

    @FXML
    private Label lblIncomeToday;

    @FXML
    private Label lblIncomeYesterday;

    @FXML
    private Label lblP120;

    @FXML
    private Label lblP160;

    @FXML
    private Label lblP200;

    @FXML
    private Label lblP40;

    @FXML
    private Label lblP80;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblTop1;

    @FXML
    private Label lblTop2;

    @FXML
    private Label lblTop3;

    @FXML
    private Label lblUser;

    @FXML
    private AnchorPane mainPane;
    private String name;
    double todayIncome;
    double TottodayReceived;
    double cost ;

    double netProfit ;


    public void initialize() throws SQLException {
        setDate();
        setTime();
        if (name!=null){
            setUserID(name);
        }
       // setItemsCount();
        //setIncome();

    }



    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));
    }

    private void setTime(){
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm a");
        lblTime.setText(now.format(formatter));
    }


    @FXML
    void customerOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml"));

        childrenpane.getChildren().clear();
        childrenpane.getChildren().add(Form);

    }

    @FXML
    void dashboardOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        mainPane.getChildren().clear();
        mainPane.getChildren().add(Form);

    }

    @FXML
    void financeOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/financialChoose_form.fxml"));

        childrenpane.getChildren().clear();
        childrenpane.getChildren().add(Form);


    }

    @FXML
    void itemsOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/otherItems_form.fxml"));

        childrenpane.getChildren().clear();
        childrenpane.getChildren().add(Form);

    }

    @FXML
    void logOutOnAction(ActionEvent event) {
        System.exit(0);

    }

    @FXML
    void ordersOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/orders_from.fxml"));

        childrenpane.getChildren().clear();
        childrenpane.getChildren().add(Form);

    }

    @FXML
    void settingsOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/settings_form.fxml"));

        childrenpane.getChildren().clear();
        childrenpane.getChildren().add(Form);

    }

    public void setUserID(String id){
        this.name =id;
        setUserName(id);
    }
    public void setUserName(String name){
        lblUser.setText(name);
    }

    public void logAsAdminOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/adminLogin_form.fxml"));

        childrenpane.getChildren().clear();
        childrenpane.getChildren().add(Form);
    }

    public void endDayOnAction(ActionEvent actionEvent) throws SQLException, IOException {
         todayIncome = dashboardRepo.getTodayTotal(lblDate.getText());

        double todayReceivedInstance = dashboardRepo.getTodayReceivedInstance(lblDate.getText());
        double todayReceivedLater = dashboardRepo.getTodayReceivedLater(lblDate.getText());
         TottodayReceived = todayReceivedLater+todayReceivedInstance;

         cost = profitsRepo.getTodayCosts(lblDate.getText());

         netProfit = todayIncome-cost;
         sendMessage();



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
    public void sendMessage() throws IOException, SQLException {
        TokenBody tokenBody = new TokenBody();

        tokenBody.setUsername("gayashanb");
        tokenBody.setPassword("2001@Dhananka");

        SendSMSImpl SMS = new SendSMSImpl();
        String token = SMS.getToken(tokenBody).getToken();
        System.out.println(token);

        SendSMSImpl sendSMS = new SendSMSImpl();
        SendTextBody sendTextBody = new SendTextBody();
        sendTextBody.setMsisdn(sendSMS.setMsisdns(new String[]{"0713856863"}));
        sendTextBody.setSourceAddress("DP Communi");
        System.out.println("Working");
        sendTextBody.setMessage("Dear Admin ,\n" +
                "\n" +
                "Your Business Going Well !!.Today Net Profit LKR" + " " + netProfit + " And Total Income " + todayIncome +"."+ "Your Business Total Cost LKR " +" "+ cost + "\n" +
                "\n" +
                "If you can Visit the Business.\n" +
                "\n" +
                "Have a Nice Day!\n" +
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
}
