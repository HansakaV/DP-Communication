package lk.ijse.dpcommunication.repository;

import lk.ijse.dpcommunication.controller.CustomOrder;
import lk.ijse.dpcommunication.db.DbConnection;
import lk.ijse.dpcommunication.model.placeOrder;

import java.sql.Connection;
import java.sql.SQLException;

public class placeOrderRepo {
    public static boolean placeOrder(placeOrder po) throws SQLException {
        Connection connection = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            System.out.println("Starting transaction");

            boolean isOrderSaved = orderRepo.save(po.getPlaceorder(), connection);
            System.out.println("Order saved: " + isOrderSaved);
            if (!isOrderSaved) {
                connection.rollback();
                return false;
            }

           /* boolean isQtyUpdated = itemRepo.update(po.getOdList(), connection);
            System.out.println("Quantity updated: " + isQtyUpdated);
            if (!isQtyUpdated) {
                connection.rollback();
                return false;
            }*/


            boolean isOrderDetailSaved = orderDetailRepo.save(po.getOdList(), connection);
            System.out.println("Order detail saved: " + isOrderDetailSaved);
            if (!isOrderDetailSaved) {
                connection.rollback();
                return false;
            }

            connection.commit();
            System.out.println("Transaction committed successfully");
            return true;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Transaction rolled back due to exception: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.out.println("Error during rollback: " + rollbackEx.getMessage());
                }
            } else {
                System.out.println("Connection was null during exception handling");
            }
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException autoCommitEx) {
                    System.out.println("Error setting auto-commit back to true: " + autoCommitEx.getMessage());
                }
            }
        }
    }
}

