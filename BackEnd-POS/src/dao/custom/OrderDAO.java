package dao.custom;

import dao.CrudDAO;
import entity.Orders;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public interface OrderDAO extends CrudDAO<Orders, String> {
    boolean mangeItems(int qty, String code, Connection connection) throws SQLException, ClassNotFoundException;

}
