package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // rule 1
    private static DBConnection dbConnection=null;
    private final Connection connection;

    // rule 2
    private DBConnection() throws ClassNotFoundException, SQLException {
        //create new connection
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ThogakadeM", "root", "1234");

    }

    // rule 3
    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
//        if (dbConnection == null) {
//            dbConnection = new DBConnection();
//        }
//        return dbConnection;
        return (dbConnection==null)?dbConnection = new DBConnection():dbConnection;
    }

    public Connection getConnection() {
        return connection;//DBConnection.getInstance().getConnection();
    }
}
