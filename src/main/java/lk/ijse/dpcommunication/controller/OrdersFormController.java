package lk.ijse.dpcommunication.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.model.*;
import lk.ijse.dpcommunication.model.tm.cartTm;
import lk.ijse.dpcommunication.repository.customerRepo;
import lk.ijse.dpcommunication.repository.itemRepo;
import lk.ijse.dpcommunication.repository.orderRepo;
import lk.ijse.dpcommunication.repository.placeOrderRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.controlsfx.control.Notifications;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersFormController {

    public TableColumn colDescription;
    public TableColumn colCusId;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colTotal;
    public TableColumn colDate;
    public TableColumn colOrderId;
    public Button btnViewOrders;
    public Button btnNewCus;
    public ComboBox cmbPaid;
    public Button btnGetMoney;
    @FXML
    private Button btnAddToCart;

    @FXML
    private Button btnPlaceOrder;

    @FXML
    private Button btnPrintBill;

    @FXML
    private Button btnViewBill;

    @FXML
    private ComboBox<String> cmbCustomers;

    @FXML
    private ComboBox<String> cmbItem;

    @FXML
    private AnchorPane itemsPane;

    @FXML
    private Label lblCode;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblPrice;

    @FXML
    private TableView<cartTm> tblOrders;

    @FXML
    private TextField txtQty;

    private ObservableList<cartTm> obList = FXCollections.observableArrayList();

    private String[] statusList = new String[]{"Paid", "Pay Later"};

    private int tempQty;

    public void initialize() throws SQLException {
        getItemCodes();
        getCustomers();
        getCurrentOrderId();
        calculateNetTotal();
        setCellValueFactory();
        setDate();
        showStatus();
       // saveID();
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
        this.cmbPaid.setItems(listStatus);

    }

    private void setCellValueFactory(){
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCusId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private void getItemCodes() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> codeList = itemRepo.getCodes();

            for (String code : codeList) {
                obList.add(code);
            }
            cmbItem.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void getCustomers(){
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> cusList = customerRepo.getNames();

            for (String code : cusList) {
                obList.add(code);
            }
            cmbCustomers.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));
    }
    private void getCurrentOrderId(){
        try{
            String id = orderRepo.getCurrentId();
            String nextId = generateNextOrderId(id);
            lblOrderId.setText(nextId);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
    private String generateNextOrderId(String id) {
        if (id != null && id.startsWith("DP")) {
            String[] split = id.split("DP");
            int idNum = Integer.parseInt(split[1]);
            return String.format("DP%03d", ++idNum);
        }
        return "DP001";
    }


    @FXML
    void addToCartOnAction(ActionEvent event) throws SQLException {
        String date = lblDate.getText();
        String orderId= lblOrderId.getText();
        String cusId = cmbCustomers.getValue();
        String desc = cmbItem.getValue();
        int qty = Integer.parseInt(txtQty.getText());
        tempQty = qty;
        double unitPrice = Double.parseDouble(lblPrice.getText());
        double total = qty * unitPrice;

      /*  for (int i = 0; i < tblOrders.getItems().size(); i++) {
            if (desc.equals(colDescription.getCellData(i))) {

                cartTm tm = obList.get(i);
                qty += tm.getQty();
                total = qty * unitPrice;

                tm.setQty(qty);
                tm.setTotal(total);

                tblOrders.refresh();

                calculateNetTotal();
                return;
            }
        }*/
        tblOrders.refresh();
        calculateNetTotal();

        cartTm tm = new cartTm(date,orderId,cusId, desc, qty, unitPrice, total);
        obList.add(tm);

        tblOrders.setItems(obList);
        calculateNetTotal();
        //clearItems();
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(2), // Delay duration
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        txtQty.setText(""); // Action to perform after the delay
                    }
                }
        ));

        timeline.setCycleCount(1); // Ensure it only runs once
        timeline.play(); // Start the timeline
       // txtQty.setText("");
        processTableValues();
        //System.out.println("Weda meka");
//       boolean added =  orderRepo.saveCart(tm);
//       if (added){
//           System.out.println("add done");
//       }

    }
//    private void saveID() throws SQLException {
//        String Id = lblOrderId.getText();
//        OrderId orderId1 = new OrderId(Id);
//       boolean saved =  orderRepo.saveID(orderId1);
//       if (saved){
//           System.out.println("Saved");
//       }
//
//    }
    private void clearItems(){
        cmbItem.setValue("");
        txtQty.setText("");
        lblPrice.setText("");
        lblCode.setText("");

    }
    private void processTableValues() throws SQLException {
        // Get the items from the table (assuming tblOrders is a TableView<cartTm>)
        ObservableList<cartTm> items = tblOrders.getItems();

        // Iterate over the items and perform operations
        for (cartTm item : items) {
            String date = item.getDate();
            String orderId = item.getOrderId();
            String customerId = item.getCustomerId();
            String description = item.getDescription();
            int quantity = item.getQty();
            double unitPrice = item.getUnitPrice();
            double total = item.getTotal();
//orderRepo.saveCart();

            // Perform any additional operations with these values
            // For example, you might want to print them or save them to the database
            System.out.println("Date: " + date + ", Order ID: " + orderId + ", Customer ID: " + customerId +
                    ", Description: " + description + ", Quantity: " + quantity +
                    ", Unit Price: " + unitPrice + ", Total: " + total);
        }
    }


    private void calculateNetTotal() {
        double netTotal = 0;  // Change int to double
        for (int i = 0; i < tblOrders.getItems().size(); i++) {
            netTotal += (double) colTotal.getCellData(i);  // No need to cast since colTotal.getCellData(i) returns a double
        }
        lblNetTotal.setText(String.format("%.2f", netTotal));  // Format to 2 decimal places for currency display
    }


    @FXML
    void placeOrderOnAction(ActionEvent event) throws SQLException {
        String date = lblDate.getText();
        String orderId = lblOrderId.getText();
        String cusId = cmbCustomers.getValue();
        String desc = cmbItem.getValue();
        String payment = String.valueOf(cmbPaid.getValue());
        double unitPrice = Double.parseDouble(lblPrice.getText());
        //System.out.println("Weda ne 1");
        double total = Double.parseDouble(lblNetTotal.getText());
        double updatedTotal = Double.parseDouble(lblNetTotal.getText());

        var order1 = new order(date, orderId, cusId, desc, tempQty, unitPrice, total,payment,updatedTotal);

        List<orderDetail> odList = new ArrayList<>();

        for (int i = 0; i < tblOrders.getItems().size(); i++) {
            cartTm tm = obList.get(i);

            orderDetail od = new orderDetail(
                    orderId,
                    tm.getDescription(),
                    tm.getUnitPrice(),
                    tm.getQty(),
                    tm.getTotal()
            );

            odList.add(od);
            System.out.println("Adding order detail: " + od);
        }

        placeOrder po = new placeOrder(order1, odList);
        System.out.println("Placing order: " + order1);

        try {
            boolean isPlaced = placeOrderRepo.placeOrder(po);
            if (isPlaced) {
                showNotification("Confirmation", "Order Placed SuccessFullY !!", Pos.BOTTOM_RIGHT, "confirmation");
            } else {
                showNotification("Warning", "Something Went Wrong !!", Pos.BOTTOM_RIGHT, "warning");
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }
    private void showNotification(String title, String text, Pos position, String type) {
        Notifications notification = Notifications.create()
                .title(title)
                .text(text)
                .position(position)
                .hideAfter(javafx.util.Duration.seconds(5))
                .onAction(event -> {
                    System.out.println(title + " notification clicked!");
                });

        // Add different styles for different types of notifications
        switch (type) {
            case "confirmation":
                notification.showConfirm();
                break;
            case "warning":
                notification.showWarning();
                break;
            // Add more cases if needed for different styles
        }
    }


        @FXML
    void printBillOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/dp_order.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("Order",lblOrderId.getText());
        System.out.println(data);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        //JasperViewer.viewReport(jasperPrint,true);
        JasperPrintManager.printReport(jasperPrint, true);

    }
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
            String[] split = id.split("DP");
            int idNum = Integer.parseInt(split[1]);
            return String.format("DP%03d", ++idNum);
        }
        return "DP001";
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
    void viewBillOnAction(ActionEvent event) throws SQLException, JRException {
        // Load and compile dp_order.jrxml
        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/reports/ORDER_DP.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        String currentBillId = orderRepo.getCurrentBillId();
        String nextBillId = generateNextBillId(currentBillId);
        saveId(nextBillId);

        Invoice = nextBillId;

        // Print or log the new bill ID (for debugging purposes)
        System.out.println("Generated Bill ID: " + Invoice);

        // Prepare parameters for the report
        Map<String, Object> data = new HashMap<>();
        data.put("Order", lblOrderId.getText());
        data.put("Today", now.toString());
        data.put("InvoiceId", Invoice);

        // Fill report with data from database connection
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());

        // View the report using JasperViewer (optional, for testing)
        JasperViewer.viewReport(jasperPrint, false);

    }




    public void cmbItemOnAction(ActionEvent actionEvent) {
        String desc = cmbItem.getValue();

        try {
            item item1 = itemRepo.searchByCode(desc);
            if (item1!=null){
                lblCode.setText(item1.getId());
                lblPrice.setText(String.valueOf(item1.getUnitPrice()));
            }
            txtQty.requestFocus();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public void viewOrdersOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/ordersView_form.fxml"));
        itemsPane.getChildren().clear();
        itemsPane.getChildren().add(Form);
    }
    public void newCustomerOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml"));
        itemsPane.getChildren().clear();
        itemsPane.getChildren().add(Form);

    }

    public void GetMoneyOnAction(ActionEvent actionEvent) throws IOException {
       AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/payNow_form.fxml"));
       itemsPane.getChildren().clear();
       itemsPane.getChildren().add(Form);

    }

    public void cmbPaidOnAction(ActionEvent actionEvent) {
        String selectedValue = cmbPaid.getValue().toString();
        if ("Paid".equals(selectedValue)) {
            btnGetMoney.setVisible(true); // Hide the button
        } else {
            btnGetMoney.setVisible(false); // Show the button
        }
    }

    public void customOrderOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/custom_orders_from.fxml"));
        itemsPane.getChildren().clear();
        itemsPane.getChildren().add(Form);


    }
}
