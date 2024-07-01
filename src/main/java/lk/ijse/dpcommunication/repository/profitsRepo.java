package lk.ijse.dpcommunication.repository;

import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.model.InstanceFinancialDTO;
import lk.ijse.dpcommunication.model.cost;
import lk.ijse.dpcommunication.model.cost;
import lk.ijse.dpcommunication.model.InstanceFinancialDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class profitsRepo {
    public static boolean save(cost cost1) throws SQLException {
        String sql = "INSERT INTO cost VALUES(?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, cost1.getDate());
        pstm.setObject(2, cost1.getDescription());
        pstm.setObject(3, cost1.getValue());

        return pstm.executeUpdate() > 0;
    }

    public static List<cost> getAll() throws SQLException {
        String sql = "SELECT * FROM cost";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<cost> cosList = new ArrayList<>();

        while (resultSet.next()) {
            String date = resultSet.getString(1);
            String dsec = resultSet.getString(2);
            double value = resultSet.getDouble(3);

            cost cost1 = new cost(date, dsec,  value);
            cosList.add(cost1);
        }
        return cosList;
    }
    public static double getTodayCosts(String date) throws SQLException {
        String sql = "SELECT SUM(value) AS total_value FROM cost WHERE date = ? ";
        double income = 0;

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, date);

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getMonthlyTotal(String year, String month) throws SQLException {
        String sql = "SELECT SUM(total) AS total FROM orders WHERE date BETWEEN ? AND ?";
        double income = 0;

        // Construct the start and end date for the given month and year
        String startDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-31"; // This will work for months with 30 and 31 days

        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstm.setString(1, startDate); // Set the start date parameter
            pstm.setString(2, endDate);   // Set the end date parameter

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
            throw e; // rethrow the exception to handle it higher up in the call stack
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getMonthlyReceivedInstance(String year,String month) throws SQLException {
        String sql = "SELECT SUM(received_money) AS total_received_money FROM financial_paylater WHERE date BETWEEN ? AND ? ";
        double income = 0;

        String startDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-31"; // This will work for months with 30 and 31 days


        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, startDate); // Set the start date parameter
            pstm.setString(2, endDate);   // Set the end date parameter


            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getMonthlyReceivedLater(String year,String month) throws SQLException {
        String sql = "SELECT SUM(received_money) AS total_received_money FROM financial_paynow WHERE order_date BETWEEN ? AND ? ";
        double income = 0;

        String startDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-31"; // This will work for months with 30 and 31 days


        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, startDate); // Set the start date parameter
            pstm.setString(2, endDate);   // Set the end date parameter


            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }

    public static double getMonthlyLaterArrears(String year,String month) throws SQLException {
        String sql = "SELECT SUM(arrears) AS total_received_money FROM financial_paylater WHERE date BETWEEN ? AND ? ";
        double income = 0;

        String startDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-31"; // This will work for months with 30 and 31 days



        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, startDate); // Set the start date parameter
            pstm.setString(2, endDate);   // Set the end date parameter


            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getMonthlyInstanceArrears(String year,String month) throws SQLException {
        String sql = "SELECT SUM(arrears) AS total_received_money FROM financial_paynow WHERE order_date BETWEEN ? AND ? ";
        double income = 0;

        String startDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-31"; // This will work for months with 30 and 31 days



        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, startDate); // Set the start date parameter
            pstm.setString(2, endDate);   // Set the end date parameter


            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getMonthlyReceivableAmount(String year,String month) throws SQLException {
        String sql = "SELECT SUM(updated_total) AS total_received_money FROM orders WHERE date BETWEEN ? AND ? ";
        double income = 0;
        String startDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-31"; // This will work for months with 30 and 31 days


        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, startDate); // Set the start date parameter
            pstm.setString(2, endDate);   // Set the end date parameter


            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);

                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getMonthlyCosts(String year,String month) throws SQLException {
        String sql = "SELECT SUM(value) AS total_value FROM cost WHERE date BETWEEN ? AND ? ";
        double income = 0;

        String startDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-31"; // This will work for months with 30 and 31 days


        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, startDate); // Set the start date parameter
            pstm.setString(2, endDate);   // Set the end date parameter


            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static List<String> getInstanceName() throws SQLException {
        String sql = "SELECT DISTINCT fp.customer_name FROM financial_paynow fp " +
                "JOIN order_detail od ON fp.order_id = od.order_id " +
                "WHERE fp.status = 'Incomplete'";

        List<String> cusList = new ArrayList<>();

        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet resultSet = pstm.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("customer_name");
                cusList.add(name);
            }
        }

        return cusList;
    }
    private static final String updateArrearsSqlSetZero = "UPDATE financial_paynow SET arrears = 0 WHERE customer_name = ?";
    private static final String updateArrearsSqlAddReceived = "UPDATE financial_paynow SET received_money = received_money + ? WHERE customer_name = ? AND arrears = ?";
    private static final String updateOrdersSql = "UPDATE orders SET updated_total = updated_total - ? WHERE customer_name = ? AND updated_total = ? AND total = ?";

    public static boolean updateGetArrears(String customerName, boolean setZero, double arrears, double cost) {
        Connection connection = null;
        PreparedStatement updateArrearsStmt = null;
        PreparedStatement updateOrdersStmt = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Update financial_paynow table
            String updateArrearsSql = setZero ? updateArrearsSqlSetZero : updateArrearsSqlAddReceived;
            updateArrearsStmt = connection.prepareStatement(updateArrearsSql);

            if (!setZero) {
                updateArrearsStmt.setDouble(1, arrears);
                updateArrearsStmt.setString(2, customerName);
            } else {
                updateArrearsStmt.setString(1, customerName);
            }

            System.out.println("Executing SQL: " + updateArrearsSql);
            System.out.println("With parameters: " + arrears + ", " + customerName);

            int arrearsRowsAffected = updateArrearsStmt.executeUpdate();

            if (arrearsRowsAffected == 0) {
                System.out.println("No rows affected in financial_paynow update. Rolling back.");
                connection.rollback(); // Rollback transaction if update failed
                return false;
            }

            // Update orders table
            updateOrdersStmt = connection.prepareStatement(updateOrdersSql);
            updateOrdersStmt.setDouble(1, arrears);
            updateOrdersStmt.setString(2, customerName);
            updateOrdersStmt.setDouble(3, arrears);
            updateOrdersStmt.setDouble(4, cost);

            System.out.println("Executing SQL: " + updateOrdersSql);
            System.out.println("With parameters: " + arrears + ", " + customerName + ", " + arrears + ", " + cost);

            int ordersRowsAffected = updateOrdersStmt.executeUpdate();

            if (ordersRowsAffected == 0) {
                System.out.println("No rows affected in orders update. Rolling back.");
                connection.rollback(); // Rollback transaction if update failed
                return false;
            }

            connection.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback transaction on exception
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            // Close statements and connection
            try {
                if (updateArrearsStmt != null) {
                    updateArrearsStmt.close();
                }
                if (updateOrdersStmt != null) {
                    updateOrdersStmt.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true); // Restore auto-commit mode
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getId(String name,double cost) throws SQLException {
        String sql = "SELECT order_id FROM orders WHERE customer_name = ? AND total =?" ;
        String income = null;
       // System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, name);
            pstm.setDouble(2, cost);

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getString(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }


}
