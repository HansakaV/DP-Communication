package lk.ijse.dpcommunication.repository;

import lk.ijse.dpcommunication.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class userRepo {
    public static boolean changePassword( String oldPassword, String newPassword) throws SQLException {
        String updateSQL = "UPDATE users SET password = ? WHERE password = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement updateStmt = null;

        try {
            // Update password if old password matches
            updateStmt = connection.prepareStatement(updateSQL);
            updateStmt.setString(1, newPassword);
            updateStmt.setString(2, oldPassword);

            int rowsUpdated = updateStmt.executeUpdate();
            return rowsUpdated > 0;
        } finally {
            // Close resources
            if (updateStmt != null) updateStmt.close();
            if (connection != null) connection.close();
        }
    }

}
