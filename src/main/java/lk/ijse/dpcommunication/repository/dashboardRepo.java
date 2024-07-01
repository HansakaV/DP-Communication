package lk.ijse.dpcommunication.repository;

import lk.ijse.dpcommunication.db.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dashboardRepo {
    public static double getA3Qty() throws SQLException {
        String sql = "SELECT qty FROM items WHERE LEFT(item_name, 1) = 'A' AND item_name LIKE 'A3 Photo%'";
        double qty = 0;

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) { // Use if instead of while since we expect only one result
            qty = resultSet.getDouble(1);
        }
        return qty;
    }
    public static double getA4Qty() throws SQLException {
        String sql = "SELECT qty FROM items WHERE LEFT(item_name, 1) = 'A' AND item_name LIKE 'A4 Photo%'";
        double qty = 0;

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) { // Use if instead of while since we expect only one result
            qty = resultSet.getDouble(1);
        }
        return qty;

    }
    public static double getEx40() throws SQLException {
        String sql = "SELECT qty FROM items WHERE item_id = 'I010'";
        double qty = 0;

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) { // Use if instead of while since we expect only one result
            qty = resultSet.getDouble(1);
        }
        return qty;

    }
    public static double getEx80() throws SQLException {
        String sql = "SELECT qty FROM items WHERE item_id = 'I011'";
        double qty = 0;

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) { // Use if instead of while since we expect only one result
            qty = resultSet.getDouble(1);
        }
        return qty;

    }
    public static double getEx120() throws SQLException {
        String sql = "SELECT qty FROM items WHERE item_id = 'I012'";
        double qty = 0;

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) { // Use if instead of while since we expect only one result
            qty = resultSet.getDouble(1);
        }
        return qty;

    }
    public static double getEx160() throws SQLException {
        String sql = "SELECT qty FROM items WHERE item_id = 'I013'";
        double qty = 0;

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) { // Use if instead of while since we expect only one result
            qty = resultSet.getDouble(1);
        }
        return qty;

    }
    public static double getEx200() throws SQLException {
        String sql = "SELECT qty FROM items WHERE item_id = 'I014'";
        double qty = 0;

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) { // Use if instead of while since we expect only one result
            qty = resultSet.getDouble(1);
        }
        return qty;

    }
    public static double getTodayIncome(String date) throws SQLException {
        String sql = "SELECT SUM(total) AS total_received_money FROM orders WHERE date = ? AND payment = 'Paid'";
        double income = 0;
        //System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, date); // Set the parameter for the prepared statement

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getTodayTotal(String date) throws SQLException {
        String sql = "SELECT SUM(total) AS total FROM orders WHERE date = ? ;";
        double income = 0;
        //System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, date); // Set the parameter for the prepared statement

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getTodayReceivedInstance(String date) throws SQLException {
        String sql = "SELECT SUM(received_money) AS total_received_money FROM financial_paylater WHERE date = ? ";
        double income = 0;
        System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, date); // Set the parameter for the prepared statement

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getTodayReceivedLater(String date) throws SQLException {
        String sql = "SELECT SUM(received_money) AS total_received_money FROM financial_paynow WHERE order_date = ? ";
        double income = 0;
        System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, date); // Set the parameter for the prepared statement

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getTodayLater(String date) throws SQLException {
        String sql = "SELECT SUM(total) AS total_received_money FROM orders WHERE date = ? AND payment = 'Pay Later'";
        double income = 0;
        System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, date);

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }

    public static double getYesterdayIncome(String date) throws SQLException {
        String sql = "SELECT SUM(total) AS total_received_money FROM orders WHERE date = ? - INTERVAL 1 DAY";
        double income = 0;
        System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, date); // Set the parameter for the prepared statement

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }

    public static double getTodayLaterArrears(String date) throws SQLException {
        String sql = "SELECT SUM(arrears) AS total_received_money FROM financial_paylater WHERE date = ? ";
        double income = 0;
        System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, date);

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getTodayInstanceArrears(String date) throws SQLException {
        String sql = "SELECT SUM(arrears) AS total_received_money FROM financial_paynow WHERE order_date = ? ";
        double income = 0;
        System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, date);

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }
    public static double getTodayReceivableAmount(String date) throws SQLException {
        String sql = "SELECT SUM(updated_total) AS total_received_money FROM orders WHERE date = ? ";
        double income = 0;
        System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, date);

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            }
        }

        return income; // Return the total income (0 if not found)
    }

}
