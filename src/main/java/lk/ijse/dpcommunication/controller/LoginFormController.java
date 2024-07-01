package lk.ijse.dpcommunication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dpcommunication.db.DbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    public PasswordField pwdPassword;
    @FXML
    private Button btnLogin;

    @FXML
    private AnchorPane imagenode;

    @FXML
    private AnchorPane mainNode;

    @FXML
    private ImageView rootNode;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    void loginOnAction(ActionEvent event) throws IOException {
        String userName = txtUserName.getText();
        String pw = pwdPassword.getText();

        try {
            checkCredential(userName, pw);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void checkCredential(String userName, String pw) throws SQLException, IOException {
        String sql = "SELECT user_name, password FROM users WHERE user_name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, userName);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String dbPw = resultSet.getString("password");

            if(pw.equals(dbPw)) {
                navigateToTheDashboard();
            } else {
                new Alert(Alert.AlertType.ERROR, "sorry! password is incorrect!").show();
            }

        } else {
            new Alert(Alert.AlertType.INFORMATION, "sorry! user id can't be find!").show();
        }
    }

    private void navigateToTheDashboard() throws IOException {
//        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));
//
//        Scene scene = new Scene(rootNode);
//
//        Stage stage = (Stage) this.mainNode.getScene().getWindow();
//        stage.setScene(scene);
//        stage.centerOnScreen();
//        stage.setTitle("Dashboard Form");
        String name = txtUserName.getText();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/dashboard_form.fxml"));
        AnchorPane anchorPane = loader.load();

        DashboardFormController dashboardFormController =loader.getController();
        dashboardFormController.setUserID(name);

        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setScene(anchorPane.getScene());
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
        stage.show();
        rootNode.getScene().getWindow().hide();
    }


    public void loginkeyOnAction(KeyEvent keyEvent) throws IOException {
        navigateToTheDashboard();
    }
}
