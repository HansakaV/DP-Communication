package lk.ijse.dpcommunication.repository;

import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.model.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class customerRepo {
    public static boolean save(customer customer1) throws SQLException {
        String sql = "INSERT INTO customers VALUES(?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, customer1.getId());
        pstm.setObject(2, customer1.getName());
        pstm.setObject(3, customer1.getTel());

        return pstm.executeUpdate() > 0;
    }

    public static customer searchById(String id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String cus_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String tel = resultSet.getString(3);

            customer customer1 = new customer(cus_id, name,  tel);

            return customer1;
        }

        return null;
    }

    public static boolean update(customer customer1) throws SQLException {
        String sql = "UPDATE customers SET customer_name = ?, tel = ? WHERE customer_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        // Set parameters
        pstm.setString(1, customer1.getName());
        pstm.setString(2, customer1.getTel());
        pstm.setString(3, customer1.getId());

        // Print for debugging
       // System.out.println("Executing query: " + sql);
      //  System.out.println("Parameters: " + customer1.getName() + ", " + customer1.getTel() + ", " + customer1.getId());

        // Execute update and return result
        int rowsUpdated = pstm.executeUpdate();
       // System.out.println("Rows updated: " + rowsUpdated);

        return rowsUpdated > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static List<customer> getAll() throws SQLException {
        String sql = "SELECT * FROM customers";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<customer> cusList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String tel = resultSet.getString(3);

            customer customer1 = new customer(id, name,  tel);
            cusList.add(customer1);
        }
        return cusList;
    }

    public static List<String> getNames() throws SQLException {
        String sql = "SELECT customer_name FROM customers";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }
    public static String getMobile(String name) throws SQLException {
        String sql = "SELECT tel FROM customers WHERE customer_name = ?";
        String mobile = null;

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, name); // Set the parameter for the prepared statement

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) { // Use if instead of while since we expect only one result
            mobile = resultSet.getString(1);
        }

        return mobile; // Return the mobile number (null if not found)
    }

}
