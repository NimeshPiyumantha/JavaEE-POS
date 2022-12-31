package dao.custom.impl;

import dao.custom.QueryDAO;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class QueryDAOImpl implements QueryDAO {
    @Override
    public int getSumOrders(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT COUNT(orderId) FROM `Orders`");
        if (result.next()) {
            return result.getInt(1);
        } else {
            return 0;
        }
    }

    @Override
    public int getItem(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT COUNT(code) FROM Item");
        if (result.next()) {
            return result.getInt(1);
        } else {
            return 0;
        }
    }

    @Override
    public int getCustomer(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT COUNT(id) FROM Customer");
        if (result.next()) {
            return result.getInt(1);
        } else {
            return 0;
        }
    }
}
