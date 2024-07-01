package lk.ijse.dpcommunication.repository;

import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.model.item;
import lk.ijse.dpcommunication.model.orderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class itemRepo {
    public static boolean save(item item1) throws SQLException {
        String sql = "INSERT INTO items VALUES(?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, item1.getId());
        pstm.setObject(2, item1.getName());
        pstm.setObject(3, item1.getUnitPrice());
        pstm.setObject(4, item1.getQty());

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getCodes() throws SQLException {
        String sql = "SELECT item_name FROM items";
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

    public static item searchById(String code) throws SQLException {
        String sql = "SELECT * FROM items WHERE item_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, code);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return null;
    }
    public static item searchByCode(String desc) throws SQLException {
        String sql = "SELECT * FROM items WHERE item_name = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, desc);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return null;

    }
    public static  boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM items WHERE item_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }


    public static boolean update(List<orderDetail> odList, Connection connection) throws SQLException {
        for (orderDetail od : odList) {
            boolean isUpdateQty = updateQty(od.getDescription(), od.getQty());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }
    public static boolean updateT(item item1) throws SQLException {
        String sql = "UPDATE items SET item_name = ?, unit_price = ?, qty= ? WHERE item_id = ?";

        Connection connection = DbConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);

        pstmt.setString(1, item1.getName());
        pstmt.setDouble(2, item1.getUnitPrice());
        pstmt.setInt(3, item1.getQty());
        pstmt.setString(4, item1.getId());

        int rowsUpdated = pstmt.executeUpdate();
        return rowsUpdated > 0;
    }

    private static boolean updateQty(String itemCode, int qty) throws SQLException {
        String sql = "UPDATE items SET qty = qty - ? WHERE item_name = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, qty);
        pstm.setString(2, itemCode);

        return pstm.executeUpdate() > 0;
    }
    public static List<item> getAll() throws SQLException {
        String sql = "SELECT * FROM items";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<item> itemList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            double unitPrice = resultSet.getDouble(3);
            int qty = resultSet.getInt(4);

            item item1 = new item(id, name,unitPrice,qty);
            itemList.add(item1);
        }
        return itemList;
    }

}
