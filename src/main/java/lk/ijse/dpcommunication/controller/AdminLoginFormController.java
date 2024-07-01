package lk.ijse.dpcommunication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class AdminLoginFormController {

    @FXML
    private Button btnLogin;

    @FXML
    private AnchorPane customerNode;

    @FXML
    private PasswordField pwdPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    void loginOnAction(ActionEvent event) throws IOException {
        checkCredentials();

    }
    String userName = "Admin";
    String password = "DP@2004";
    private void checkCredentials() throws IOException {
        String user = txtUsername.getText();
        String pw = pwdPassword.getText();
        if (user.equals(userName)&&pw.equals(password)){
            navigateToTheAdminPanel();
        }else {
            showNotification("Warning", "Something Went Wrong !!", Pos.BOTTOM_RIGHT, "warning");

        }
    }
    private void navigateToTheAdminPanel() throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/adminDashboard_form.fxml"));

        customerNode.getChildren().clear();
        customerNode.getChildren().add(Form);
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
