package lk.ijse.dpcommunication.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.model.customer;
import lk.ijse.dpcommunication.model.tm.customerTm;
import lk.ijse.dpcommunication.repository.customerRepo;
import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;


import java.sql.SQLException;
import java.util.List;

public class CustomerFormController {

    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colTel;
    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnGetCustomers;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private AnchorPane customerNode;

    @FXML
    private TableView<customerTm> tblCustomers;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTel;

    public void initialize(){
        setCellValueFactory();
        loadAllCustomers();

    }

    private void setCellValueFactory(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
    }
    private void loadAllCustomers(){
        ObservableList<customerTm> obList = FXCollections.observableArrayList();
        try {
            List<customer> customerList = customerRepo.getAll();
            for (customer c1 : customerList){
                customerTm tm = new customerTm(
                        c1.getId(),
                        c1.getName(),
                        c1.getTel()
                );
                obList.add(tm);
            }
            FXCollections.sort(obList);
            tblCustomers.setItems(obList);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearOnAction(ActionEvent event) {clearFields();}

    @FXML
    void deleteOnAction(ActionEvent event) {
        String id = txtId.getText()
;
        try {
            boolean isDeleted = customerRepo.delete(id);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted From System !");
            }else {
                new Alert(Alert.AlertType.WARNING,"Customer Not Found !!");
            }
            tblCustomers.refresh();
        }catch (SQLException dp){
            throw new RuntimeException(dp);
        }
    }

    @FXML
    void getCustomersOnAction(ActionEvent event) {

    }

    @FXML
    void saveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String tel = txtTel.getText();

        customer c1 = new customer(id,name,tel);

        try {
            boolean isSaved = customerRepo.save(c1);
            if (isSaved){
                showNotification("Confirmation", "Added To System !!", Pos.BOTTOM_RIGHT, "confirmation");
                clearFields();
            }else {
                showNotification("Warning", "Something Went Wrong !!", Pos.BOTTOM_RIGHT, "warning");
            }
            tblCustomers.refresh();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
    private void clearFields(){
        txtId.setText("");
        txtName.setText("");
        txtTel.setText("");
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
                notification.darkStyle();
                break;
            case "warning":
                notification.showWarning();
                break;
            // Add more cases if needed for different styles
        }

        notification.show();
    }

        @FXML
    void updateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String tel = txtTel.getText();

        customer c1 = new customer(id,name,tel);

        try {
            boolean isUpdated = customerRepo.update(c1);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Update SuccessFull");
            }else {
                new Alert(Alert.AlertType.WARNING,"Something Went Wrong !!");
            }
            tblCustomers.refresh();
        }catch (SQLException dp){
            throw new RuntimeException(dp);
        }

    }

}
