package lk.ijse.dpcommunication.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.model.order;

import java.io.IOException;
import java.sql.SQLException;

import static lk.ijse.dpcommunication.repository.orderRepo.viewCart;

public class OrderViewFormController {

    public AnchorPane ordersPane;
    @FXML
    private Button btnBack;

    @FXML
    private Button btnGetList;

    @FXML
    private TableColumn<?, ?> colCustomer;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colItem;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<order> tblOrders;

    public void initialize(){
        setCellValueFactory();
        loadAllOrders();

    }

    private void setCellValueFactory(){
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }
    private void loadAllOrders(){
        try {
            ObservableList<order> orderDetails = viewCart();
            tblOrders.setItems(orderDetails);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void backOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/orders_from.fxml"));
        ordersPane.getChildren().clear();
        ordersPane.getChildren().add(Form);



    }

    @FXML
    void getListOnAction(ActionEvent event) {

    }

}
