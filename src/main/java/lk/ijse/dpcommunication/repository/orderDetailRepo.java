package lk.ijse.dpcommunication.repository;

import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.model.orderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class orderDetailRepo {
    public static boolean save(List<orderDetail> odList, Connection connection) throws SQLException {
        for (orderDetail od : odList) {
            boolean isSaved = save(od);
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(orderDetail od) throws SQLException {
        String sql = "INSERT INTO order_detail VALUES(?, ?, ?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, od.getOrderId());
        pstm.setString(2, od.getDescription());
        pstm.setDouble(3, od.getUnitPrice());
        pstm.setInt(4, od.getQty());
        pstm.setDouble(5, od.getTotal());

        return pstm.executeUpdate() > 0;    //false ->  |
    }
}
