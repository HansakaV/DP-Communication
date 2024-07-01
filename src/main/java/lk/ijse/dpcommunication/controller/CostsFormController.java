package lk.ijse.dpcommunication.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.model.cost;
import lk.ijse.dpcommunication.model.tm.costTm;
import lk.ijse.dpcommunication.repository.profitsRepo;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CostsFormController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnSave;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private AnchorPane customerNode;

    @FXML
    private DatePicker pickDate;

    @FXML
    private TableView<costTm> tblcosts;

    @FXML
    private TextField txtCost;

    @FXML
    private TextField txtDesc;

    @FXML
    void BackOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/financialChoose_form.fxml"));

        customerNode.getChildren().clear();
        customerNode.getChildren().add(Form);


    }

    @FXML
    void clearOnAction(ActionEvent event) {
        clear();

    }

    @FXML
    void saveOnAction(ActionEvent event) {
        String date = String.valueOf(pickDate.getValue());
        String desc = txtDesc.getText();
        double value = Double.parseDouble(txtCost.getText());

        cost c1 = new cost(date,desc,value);

        try {
            boolean isSaved = profitsRepo.save(c1);
            if (isSaved){
                showNotification("Confirmation", "Added To System !!", Pos.BOTTOM_RIGHT, "confirmation");
                clear();
            }else {
                showNotification("Warning", "Something Went Wrong !!", Pos.BOTTOM_RIGHT, "warning");
            }
            tblcosts.refresh();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
    public void initialize(){
        setCellValueFactory();
        loadAllCustomers();

    }

    private void setCellValueFactory(){
        colID.setCellValueFactory(new PropertyValueFactory<>("date"));
        colName.setCellValueFactory(new PropertyValueFactory<>("description"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("value"));
    }
    private void loadAllCustomers(){
        ObservableList<costTm> obList = FXCollections.observableArrayList();
        try {
            List<cost> customerList = profitsRepo.getAll();
            for (cost c1 : customerList){
                costTm tm = new costTm(
                        c1.getDate(),
                        c1.getDescription(),
                        c1.getValue()
                );
                obList.add(tm);
            }
            tblcosts.setItems(obList);
        }catch (SQLException e){
            throw new RuntimeException(e);
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
                notification.darkStyle();
                break;
            case "warning":
                notification.showWarning();
                break;
            // Add more cases if needed for different styles
        }

        notification.show();
    }
    private void clear(){
        txtDesc.setText("");
        txtCost.setText("");
        pickDate.setValue(null);
    }


}
