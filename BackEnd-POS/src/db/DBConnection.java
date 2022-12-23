package db;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    BasicDataSource bds;

    private DBConnection() throws SQLException {
        bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/ThogaKadeM");
        bds.setPassword("1234");
        bds.setUsername("root");

        bds.setMaxTotal(2);// how many connection

        bds.setInitialSize(2);// how many connection should be initialized from created connections


    }

    public static DBConnection getDbConnection() throws SQLException {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() throws SQLException {
        return bds.getConnection();
    }
}
