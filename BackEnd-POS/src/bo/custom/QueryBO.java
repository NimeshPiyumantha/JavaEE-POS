package bo.custom;

import bo.SuperBO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public interface QueryBO extends SuperBO {
    int getSumOrders(Connection connection) throws SQLException, ClassNotFoundException;

    int getItem(Connection connection) throws SQLException, ClassNotFoundException;

    int getCustomer(Connection connection) throws SQLException, ClassNotFoundException;

}
