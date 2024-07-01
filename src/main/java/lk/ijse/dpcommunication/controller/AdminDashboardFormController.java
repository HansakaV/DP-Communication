package lk.ijse.dpcommunication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dpcommunication.repository.dashboardRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AdminDashboardFormController {

    public Label lblTodayReceived;
    public Label lblPayLater;
    @FXML
    private Button btnCost;

    @FXML
    private AnchorPane customerNode;

    @FXML
    private Label lblA3;

    @FXML
    private Label lblA4;

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
    private Button lblProfits;

    @FXML
    void getCostOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/costs_form.fxml"));

        customerNode.getChildren().clear();
        customerNode.getChildren().add(Form);

    }

    @FXML
    void getProfitsOnAction(ActionEvent event) throws IOException {
        AnchorPane Form = FXMLLoader.load(this.getClass().getResource("/view/profit.fxml"));

        customerNode.getChildren().clear();
        customerNode.getChildren().add(Form);

    }
    public void initialize() throws SQLException {
         setItemsCount();
        setIncome();
    }
    private void setItemsCount() throws SQLException {
        double A3qty = dashboardRepo.getA3Qty();
        lblA3.setText(String.valueOf(A3qty));

        double A4qty =dashboardRepo.getA4Qty();
        lblA4.setText(String.valueOf(A4qty));

        double ex40 =dashboardRepo.getEx40();
        lblP40.setText(String.valueOf(ex40));

        double ex80 =dashboardRepo.getEx80();
        lblP80.setText(String.valueOf(ex80));

        double ex120 =dashboardRepo.getEx120();
        lblP120.setText(String.valueOf(ex120));

        double ex160 =dashboardRepo.getEx160();
        lblP160.setText(String.valueOf(ex160));

        double ex200 =dashboardRepo.getEx200();
        lblP200.setText(String.valueOf(ex200));

    }
    private void setIncome() throws SQLException {
        LocalDate now = LocalDate.now();
        double todayIncome = dashboardRepo.getTodayTotal(String.valueOf(now));
        lblIncomeToday.setText(String.valueOf(todayIncome));

        double todayReceivedInstance = dashboardRepo.getTodayReceivedInstance(String.valueOf(now));
        double todayReceivedLater = dashboardRepo.getTodayReceivedLater(String.valueOf(now));
        double TottodayReceived = todayReceivedLater+todayReceivedInstance;
        lblTodayReceived.setText(String.valueOf(TottodayReceived));

        double todayLater = dashboardRepo.getTodayLater(String.valueOf(now));
        lblPayLater.setText(String.valueOf(todayLater));

        double yesterdayIncome = dashboardRepo.getYesterdayIncome(String.valueOf(now));
        lblIncomeYesterday.setText(String.valueOf(yesterdayIncome));

    }

}
