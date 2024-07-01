package lk.ijse.dpcommunication.repository;

import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinancialRepo {
    public static boolean save(Financial financial) throws SQLException {
        String insertFinancialSql = "INSERT INTO financial_paynow VALUES(?, ?, ?, ?, ?, ?, ?)";
        String updateOrdersSql = "UPDATE orders SET updated_total = updated_total - ? WHERE order_id = ?";

        Connection connection = null;
        PreparedStatement insertFinancialStmt = null;
        PreparedStatement updateOrdersStmt = null;

        try {
            connection = DbConnection.getInstance().getConnection();

            // Insert into financial table
            insertFinancialStmt = connection.prepareStatement(insertFinancialSql);
            insertFinancialStmt.setObject(1, financial.getOrderID());
            insertFinancialStmt.setObject(2, financial.getCustomerName());
            insertFinancialStmt.setObject(3, financial.getOrderDate());
            insertFinancialStmt.setObject(4, financial.getCost());
            insertFinancialStmt.setObject(5, financial.getReceivedMoney());
            insertFinancialStmt.setObject(6, financial.getArrears());
            insertFinancialStmt.setObject(7, financial.getStatus());

            int financialRowsAffected = insertFinancialStmt.executeUpdate();

            if (financialRowsAffected > 0) {
                // Update orders table
                updateOrdersStmt = connection.prepareStatement(updateOrdersSql);
                updateOrdersStmt.setObject(1, financial.getReceivedMoney());
                updateOrdersStmt.setObject(2, financial.getOrderID());

                int ordersRowsAffected = updateOrdersStmt.executeUpdate();

                return ordersRowsAffected > 0;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (insertFinancialStmt != null) {
                insertFinancialStmt.close();
            }
            if (updateOrdersStmt != null) {
                updateOrdersStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    public static boolean savePayLater(PayLaterDTO financial) throws SQLException {
        String checkExistingSql = "SELECT COUNT(*) FROM financial_paylater WHERE order_id = ?";
        String insertFinancialSql = "INSERT INTO financial_paylater VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String updateFinancialSql = "UPDATE financial_paylater SET date = ?, customer_name = ?, item_name = ?, order_qty = ?, order_unit_price = ?, order_total = ?, payment = ?, received_money = ?, arrears = ?, updated_total = ? WHERE order_id = ?";
        String updateOrdersSql = "UPDATE orders SET updated_total = updated_total - ? WHERE order_id = ?";

        Connection connection = null;
        PreparedStatement checkExistingStmt = null;
        PreparedStatement insertFinancialStmt = null;
        PreparedStatement updateFinancialStmt = null;
        PreparedStatement updateOrdersStmt = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Check if the record already exists
            checkExistingStmt = connection.prepareStatement(checkExistingSql);
            checkExistingStmt.setObject(1, financial.getOrder_id());
            ResultSet rs = checkExistingStmt.executeQuery();
            boolean recordExists = false;
            if (rs.next()) {
                recordExists = rs.getInt(1) > 0;
            }

            if (recordExists) {
                // Update existing record
                updateFinancialStmt = connection.prepareStatement(updateFinancialSql);
                updateFinancialStmt.setObject(1, financial.getDate());
                updateFinancialStmt.setObject(2, financial.getCustomer_name());
                updateFinancialStmt.setObject(3, financial.getItem_name());
                updateFinancialStmt.setObject(4, financial.getQty());
                updateFinancialStmt.setObject(5, financial.getUnit_price());
                updateFinancialStmt.setObject(6, financial.getTotal());
                updateFinancialStmt.setObject(7, financial.getPayment());
                updateFinancialStmt.setObject(8, financial.getReceivedMoney());
                updateFinancialStmt.setObject(9, financial.getArrears());
                updateFinancialStmt.setObject(10, financial.getUpdated_total());
                updateFinancialStmt.setObject(11, financial.getOrder_id());

                int financialRowsAffected = updateFinancialStmt.executeUpdate();
                if (financialRowsAffected <= 0) {
                    connection.rollback();
                    return false;
                }
            } else {
                // Insert new record
                insertFinancialStmt = connection.prepareStatement(insertFinancialSql);
                insertFinancialStmt.setObject(1, financial.getDate());
                insertFinancialStmt.setObject(2, financial.getOrder_id());
                insertFinancialStmt.setObject(3, financial.getCustomer_name());
                insertFinancialStmt.setObject(4, financial.getItem_name());
                insertFinancialStmt.setObject(5, financial.getQty());
                insertFinancialStmt.setObject(6, financial.getUnit_price());
                insertFinancialStmt.setObject(7, financial.getTotal());
                insertFinancialStmt.setObject(8, financial.getPayment());
                insertFinancialStmt.setObject(9, financial.getReceivedMoney());
                insertFinancialStmt.setObject(10, financial.getArrears());
                insertFinancialStmt.setObject(11, financial.getUpdated_total());

                int financialRowsAffected = insertFinancialStmt.executeUpdate();
                if (financialRowsAffected <= 0) {
                    connection.rollback();
                    return false;
                }
            }

            // Update orders table
            updateOrdersStmt = connection.prepareStatement(updateOrdersSql);
            updateOrdersStmt.setObject(1, financial.getReceivedMoney());
            updateOrdersStmt.setObject(2, financial.getOrder_id());

            int ordersRowsAffected = updateOrdersStmt.executeUpdate();
            if (ordersRowsAffected <= 0) {
                connection.rollback();
                return false;
            }

            connection.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (checkExistingStmt != null) {
                checkExistingStmt.close();
            }
            if (insertFinancialStmt != null) {
                insertFinancialStmt.close();
            }
            if (updateFinancialStmt != null) {
                updateFinancialStmt.close();
            }
            if (updateOrdersStmt != null) {
                updateOrdersStmt.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true); // Restore default auto-commit mode
                connection.close();
            }
        }
    }

    public static List<ItemDTO> searchByName(String customerName) throws SQLException {

        List<ItemDTO> itemList = new ArrayList<>();
        String sql = "SELECT o.date, od.item_name, od.unit_price, od.qty, od.total, f.pay_date,f.status, f.received_money " +
                "FROM orders o " +
                "INNER JOIN order_detail od ON o.order_id = od.order_id " +
                "INNER JOIN financial f ON o.order_id = f.order_id AND o.customer_name = f.customer_name " +
                "WHERE o.customer_name = ?";

        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstm.setString(1, customerName);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
               ItemDTO item = new ItemDTO(
                        resultSet.getString("date"),
                        resultSet.getString("item_name"),
                        resultSet.getString("status"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getInt("qty"),
                        resultSet.getDouble("total"),
                        resultSet.getString("pay_date"),
                        resultSet.getDouble("received_money")
                );
                itemList.add(item);
            }

            // Logging the number of items fetched
            System.out.println("Number of items fetched for customer " + customerName + ": " + itemList.size());

        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
            throw e; // rethrow the exception to handle it higher up in the call stack
        }

        return itemList;
    }
    public static double getTotal(String name) throws SQLException {
        String sql = "SELECT SUM(total) AS total_sum FROM orders WHERE customer_name = ?;";
        double income = 0;
        System.out.println(name);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, name); // Set the parameter for the prepared statement

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) { // Use if instead of while since we expect only one result
                    income = resultSet.getDouble(1);
                    System.out.println(income);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return income;

    }
    public static List<InstanceFinancialDTO> getInstancePayments() throws SQLException {

        List<InstanceFinancialDTO> itemList = new ArrayList<>();
        String sql = "SELECT   fp.customer_name,fp.order_date, od.item_name, od.unit_price, od.qty,od.total, fp.received_money,fp.arrears,fp.status FROM financial_paynow fp JOIN  order_detail od ON fp.order_id = od.order_id";

        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                InstanceFinancialDTO item = new InstanceFinancialDTO(
                        resultSet.getString("customer_name"),
                        resultSet.getString("order_date"),
                        resultSet.getString("item_name"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getDouble("qty"),
                        resultSet.getDouble("total"),
                        resultSet.getDouble("received_money"),
                        resultSet.getDouble("arrears"),
                        resultSet.getString("status")
                );
                itemList.add(item);
            }



        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
            throw e; // rethrow the exception to handle it higher up in the call stack
        }

        return itemList;
    }

    public static List<InstanceFinancialDTO> getInstancePaymentsViaCustomer() throws SQLException {

        List<InstanceFinancialDTO> itemList = new ArrayList<>();
        String sql = "SELECT   fp.customer_name,fp.order_date, od.item_name, od.unit_price, od.qty,od.total, fp.received_money,fp.arrears,fp.status FROM financial_paynow fp JOIN  order_detail od ON fp.order_id = od.order_id WHERE fp.status = 'Incomplete'";

        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                InstanceFinancialDTO item = new InstanceFinancialDTO(
                        resultSet.getString("customer_name"),
                        resultSet.getString("order_date"),
                        resultSet.getString("item_name"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getDouble("qty"),
                        resultSet.getDouble("total"),
                        resultSet.getDouble("received_money"),
                        resultSet.getDouble("arrears"),
                        resultSet.getString("status")
                );
                itemList.add(item);
            }



        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
            throw e; // rethrow the exception to handle it higher up in the call stack
        }

        return itemList;
    }

    public static List<PayLaterFinancialDTO> getPayLater() throws SQLException {

        List<PayLaterFinancialDTO> itemList = new ArrayList<>();
        String sql = "SELECT  o.date, o.order_id,  o.customer_name,od.item_name , od.qty , od.unit_price ,od.total ,o.payment,o.updated_total FROM  orders o JOIN order_detail od ON o.order_id = od.order_id WHERE o.payment = 'Pay Later'";

        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                PayLaterFinancialDTO item = new PayLaterFinancialDTO(
                        resultSet.getString("date"),
                        resultSet.getString("order_id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("item_name"),
                        resultSet.getDouble("qty"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getDouble("total"),
                        resultSet.getString("payment"),
                        resultSet.getDouble("updated_total")
                );
                itemList.add(item);
            }



        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
            throw e; // rethrow the exception to handle it higher up in the call stack
        }

        return itemList;
    }
    public static List<PayLaterFinancialDTO> getProfit(String date) throws SQLException {

        List<PayLaterFinancialDTO> itemList = new ArrayList<>();
        String sql = "SELECT  o.date, o.order_id,  o.customer_name,od.item_name , od.qty , od.unit_price ,od.total ,o.payment,o.updated_total FROM  orders o JOIN order_detail od ON o.order_id = od.order_id WHERE o.date = ?";

        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstm.setString(1, date);
            ResultSet resultSet = pstm.executeQuery();



            while (resultSet.next()) {
                PayLaterFinancialDTO item = new PayLaterFinancialDTO(
                        resultSet.getString("date"),
                        resultSet.getString("order_id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("item_name"),
                        resultSet.getDouble("qty"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getDouble("total"),
                        resultSet.getString("payment"),
                        resultSet.getDouble("updated_total")
                );
                itemList.add(item);
            }



        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
            throw e; // rethrow the exception to handle it higher up in the call stack
        }

        return itemList;
    }

    public static List<PayLaterFinancialDTO> getProfitMonthly(String year, String month) throws SQLException {

        List<PayLaterFinancialDTO> itemList = new ArrayList<>();
        String sql = "SELECT o.date, o.order_id, o.customer_name, od.item_name, od.qty, od.unit_price, od.total, o.payment, o.updated_total " +
                "FROM orders o " +
                "JOIN order_detail od ON o.order_id = od.order_id " +
                "WHERE o.date BETWEEN ? AND ?";

        // Construct the start and end date for the given month and year
        String startDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-31"; // This will work for months with 30 days and 31 days

        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstm.setString(1, startDate);
            pstm.setString(2, endDate);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                PayLaterFinancialDTO item = new PayLaterFinancialDTO(
                        resultSet.getString("date"),
                        resultSet.getString("order_id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("item_name"),
                        resultSet.getDouble("qty"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getDouble("total"),
                        resultSet.getString("payment"),
                        resultSet.getDouble("updated_total")
                );
                itemList.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
            throw e; // rethrow the exception to handle it higher up in the call stack
        }

        return itemList;
    }

    public static List<PayLaterFinancialDTO> getPayLaterViaCustomer(String customerName) throws SQLException {
        System.out.println(customerName);
        List<PayLaterFinancialDTO> itemList = new ArrayList<>();
        String sql = "SELECT  o.date, o.order_id,  o.customer_name,od.item_name , od.qty , od.unit_price ,od.total ,o.payment,o.updated_total FROM  orders o JOIN order_detail od ON o.order_id = od.order_id WHERE o.payment = 'Pay Later' AND updated_total != 0 AND o.customer_name = ?";

        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstm.setString(1, customerName);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                PayLaterFinancialDTO item = new PayLaterFinancialDTO(
                        resultSet.getString("date"),
                        resultSet.getString("order_id"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("item_name"),
                        resultSet.getDouble("qty"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getDouble("total"),
                        resultSet.getString("payment"),
                        resultSet.getDouble("updated_total")
                );
                itemList.add(item);
            }



        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
            throw e; // rethrow the exception to handle it higher up in the call stack
        }

        return itemList;
    }

    public static double getCustomerBalance(String customerName) throws SQLException {
        String sql = "SELECT SUM(updated_total) AS total_updated_total FROM ( SELECT DISTINCT o.order_id, o.updated_total FROM orders o JOIN order_detail od ON o.order_id = od.order_id WHERE o.payment = 'Pay Later'AND o.customer_name = ?) AS distinct_orders";
        double income = 0;
        //System.out.println(date);

        try (
                PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql)
        ) {
            pstm.setString(1, customerName); // Set the parameter for the prepared statement

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
