package dao.custom.impl;

import dao.custom.OrderDAO;
import entity.Orders;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class OrderDAOImpl implements OrderDAO {
    @Override
    public ArrayList<Orders> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT * FROM `Orders`");

        ArrayList<Orders> obList = new ArrayList<>();
        while (result.next()) {
            obList.add(new Orders(result.getString(1), result.getString(2), result.getString(3)));
        }
        return obList;
    }

    @Override
    public boolean save(Orders orderDTO, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "INSERT INTO orders VALUES(?,?,?)", orderDTO.getOrderId(), orderDTO.getOrderDate(), orderDTO.getCusId());
    }

    @Override
    public boolean update(Orders dto, Connection connection) {
        return false;
    }

    @Override
    public ArrayList<Orders> searchId(String id, Connection connection) {
        return null;
    }

    @Override
    public boolean delete(String s, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT orderId FROM `Orders` ORDER BY orderId DESC LIMIT 1");
        if (result.next()) {
            return result.getString(1);
        } else {
            return null;
        }
    }

    @Override
    public boolean mangeItems(int qty, String code, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "UPDATE Item SET qty=qty-? WHERE code=?", qty, code);
    }
}
