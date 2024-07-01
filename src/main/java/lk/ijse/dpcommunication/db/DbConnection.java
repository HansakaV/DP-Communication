package lk.ijse.dpcommunication.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*public class DbConnection {
    private static DbConnection dbConnection;
    private static Connection connection;

    private DbConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DpCommunication",
                "root",
                "Ijse@1234"
        );
    }

    public static DbConnection getInstance() throws SQLException {
        if(dbConnection == null) {
            dbConnection = new DbConnection();
        }
        return dbConnection;
    }

    public static Connection getConnection() {
        return connection;
    }
}*/
public class DbConnection {
    private static DbConnection dbConnection;
    private static Connection connection;

    private DbConnection() throws SQLException {
        try {
            // Initialize the connection here
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dpcommunication", "root", "Ijse@1234");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to create the database connection.");
        }
    }

    public static synchronized DbConnection getInstance() throws SQLException {
        if (dbConnection == null) {
            dbConnection = new DbConnection();
        }
        return dbConnection;
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dpcommunication", "root", "Ijse@1234");
        }
        return connection;
    }
}