package lk.ijse.dpcommunication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dpcommunication.repository.customerRepo;
import lk.ijse.dpcommunication.repository.userRepo;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;

public class SettingsFormController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnChangePw;

    @FXML
    private AnchorPane settingsNode;

    @FXML
    private TextField txtOldPw;

    @FXML
    private TextField txtReNew;

    @FXML
    private TextField txtnewPw;


    @FXML
    void changePasswordOnAction(ActionEvent event) throws SQLException {
        String oldPw = txtOldPw.getText();
        String newPw = txtnewPw.getText();
        String reNewPw = txtReNew.getText();

        if (newPw.equals(reNewPw)){
           boolean done =  userRepo.changePassword(oldPw,newPw);
            if (done == true){
                showNotification("Confirmation", "Password Changed Successfully !!", Pos.BOTTOM_RIGHT, "confirmation");
                clearFields();
            }
        }else {
            showNotification("Warning", "Something Went Wrong !!", Pos.BOTTOM_RIGHT, "warning");
        }

    }
    private void clearFields(){
        txtOldPw.setText("");
        txtnewPw.setText("");
        txtReNew.setText("");
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
