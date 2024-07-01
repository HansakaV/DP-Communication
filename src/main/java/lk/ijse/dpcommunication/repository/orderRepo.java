package lk.ijse.dpcommunication.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.model.order;
import lk.ijse.dpcommunication.model.orderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class orderRepo {
    String oID;
    public static String getCurrentId() throws SQLException {
        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String orderId = resultSet.getString(1);
            return orderId;
        }
        return null;
    }


    public static order searchByCode(String desc) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (
                Connection connection = DbConnection.getInstance().getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, desc);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Assuming Order class has an appropriate constructor
                    return new order(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getInt(5),
                            rs.getDouble(6),
                            rs.getDouble(7),
                            rs.getString(8),
                            rs.getDouble(9)
                    );
                }
            }
        }
        return null; // No matching order found
    }
    public static ObservableList<order> viewCart() throws SQLException {
        String sql = "SELECT o.date, o.order_id, o.customer_name, od.item_name, od.unit_price, od.qty, od.total " +
                "FROM orders o " +
                "JOIN order_detail od ON o.order_id = od.order_id;";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet rs = pstm.executeQuery();

        ObservableList<order> orderDetails = FXCollections.observableArrayList();

        while (rs.next()) {
            String date = rs.getString("date");
            String orderId = rs.getString("order_id");
            String customerName = rs.getString("customer_name");
            String itemName = rs.getString("item_name");
            int qty = rs.getInt("qty");
            double unitPrice = rs.getDouble("unit_price");
            double total = rs.getDouble("total");
            String payment = rs.getString("payment");
            double updatedTotal = rs.getDouble("updated_total");

            order orderDetail = new order( date,orderId, customerName,itemName, qty,unitPrice,total,payment,updatedTotal);
            orderDetails.add(orderDetail);
        }

        return orderDetails;
    }


    public static List<String> getCodes() throws SQLException {
        String sql = "SELECT order_id FROM orders";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> codeList = new ArrayList<>();
        while (resultSet.next()) {
            codeList.add(resultSet.getString(1));
        }
        return codeList;
    }
    public static String getOrderID() throws SQLException {
        String sql = "SELECT order_id " +
                "FROM orders " +
                "ORDER BY CAST(SUBSTRING(order_id, 3) AS UNSIGNED) DESC " +
                "LIMIT 1";

        // Assuming DbConnection is a utility class that handles the connection
        Connection connection = DbConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {
            return resultSet.getString("order_id");
        } else {
            return null; // or handle accordingly if no order_id is found
        }
    }

    public static List<order> getAll() throws SQLException {
        String sql = "SELECT * FROM orders";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<order> orderList = new ArrayList<>();

        while (resultSet.next()) {
            String date = resultSet.getString(1);
            String orderID = resultSet.getString(2);
            String cusID = resultSet.getString(3);
            String desc = resultSet.getString(4);
            int qty = resultSet.getInt(5);
            double price = resultSet.getDouble(6);
            double tot = resultSet.getDouble(7);
            String payment = resultSet.getString(8);
            double updatedTot = resultSet.getDouble(7);

            order order1 = new order(date, orderID, cusID,desc,qty,price,tot,payment,updatedTot);
            orderList.add(order1);
        }
        return orderList;
    }


    public static boolean save(order order1, Connection connection) throws SQLException {

       String sql = "INSERT INTO orders (date,order_id,customer_name,item_name,qty,unit_price,total,payment,updated_total)VALUES (?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            System.out.println("Preparing statement...");

            pstm.setString(1, order1.getDate());
            pstm.setString(2, order1.getOrderId());
            pstm.setString(3, order1.getCustomerId());
            pstm.setString(4, order1.getDescription());
            pstm.setInt(5, order1.getQty());
            pstm.setDouble(6, order1.getUnitPrice());
            pstm.setDouble(7, order1.getTotal());
            pstm.setString(8, order1.getPayment());
            pstm.setDouble(9, order1.getUpdatedTotal());

           // System.out.println("Executing update...");
            boolean result = pstm.executeUpdate() > 0;
           // System.out.println("Update executed successfully.");
            return result;
        } catch (SQLException e) {
           // System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rethrow the exception to be handled by the calling method
        } catch (Exception e) {
           // System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
