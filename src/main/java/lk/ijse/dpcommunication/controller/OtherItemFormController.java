package lk.ijse.dpcommunication.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.model.item;
import lk.ijse.dpcommunication.model.tm.itemTm;
import lk.ijse.dpcommunication.repository.customerRepo;
import lk.ijse.dpcommunication.repository.itemRepo;

import java.sql.SQLException;
import java.util.List;

public class OtherItemFormController {

    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colPrice;
    public TableColumn colQty;
    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnGetItems;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private AnchorPane customerNode;

    @FXML
    private TableView<itemTm> tblOtherItems;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtcode;


    public void initialize(){
        setCellValueFactory();
        loadAllItems();

    }

    private void setCellValueFactory(){
        colCode.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

 /*   private void loadAllItems(){
        ObservableList<itemTm> obList = FXCollections.observableArrayList();
        try {
            List<item> itemList = itemRepo.getAll();
            for (item c1 : itemList){
                itemTm tm = new itemTm(
                        c1.getId(),
                        c1.getName(),
                        c1.getUnitPrice(),
                        c1.getQty()
                );
                obList.add(tm);
            }
            tblOtherItems.setItems(obList);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }*/
 private void loadAllItems() {
     ObservableList<itemTm> obList = FXCollections.observableArrayList();
     try {
         List<item> itemList = itemRepo.getAll();
         for (item c1 : itemList) {
             itemTm tm = new itemTm(
                     c1.getId(),
                     c1.getName(),
                     c1.getUnitPrice(),
                     c1.getQty()
             );
             obList.add(tm);
         }
         FXCollections.sort(obList); // Sort the list in descending order
         tblOtherItems.setItems(obList);
     } catch (SQLException e) {
         throw new RuntimeException(e);
     }
 }


    @FXML
    void clearOnAction(ActionEvent event) {
        txtcode.setText("");
        txtDescription.setText("");
        txtQty.setText("");
        txtPrice.setText("");

    }
    private  void clearFields(){
        txtcode.setText("");
        txtDescription.setText("");
        txtQty.setText("");
        txtPrice.setText("");
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        String id = txtcode.getText()
                ;
        try {
            boolean isDeleted = itemRepo.delete(id);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted From System !");
            }else {
                new Alert(Alert.AlertType.WARNING,"Item Not Found !!");
            }
            tblOtherItems.refresh();
        }catch (SQLException dp){
            throw new RuntimeException(dp);
        }

    }

    @FXML
    void getItemsOnAction(ActionEvent event) {

    }

    @FXML
    void saveOnAction(ActionEvent event) {
        String code = txtcode.getText();
        String description = txtDescription.getText();
        double unitPride = Double.parseDouble(txtPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQty.getText());

        item item1 = new item(code, description, unitPride, qtyOnHand);

        try {
            boolean isSaved = itemRepo.save(item1);
            if(isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item saved!").show();
            }
            tblOtherItems.refresh();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }




    @FXML
    void updateOnAction(ActionEvent event) {
        String code = txtcode.getText();
        String description = txtDescription.getText();
        double unitPride = Double.parseDouble(txtPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQty.getText());

        item item1 = new item(code, description, unitPride, qtyOnHand);

        try {
            boolean isUpdated = itemRepo.updateT(item1);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item Updated!").show();
                clearFields();
            }
            tblOtherItems.refresh();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

}
