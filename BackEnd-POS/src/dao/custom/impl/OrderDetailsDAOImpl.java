package dao.custom.impl;

import dao.custom.OrderDetailsDAO;
import entity.OrderDetail;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    @Override
    public ArrayList<OrderDetail> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT * FROM `OrderDetail`");

        ArrayList<OrderDetail> orderDetailDTO = new ArrayList<>();
        while (result.next()) {
            orderDetailDTO.add(new OrderDetail(result.getString(1), result.getString(2), result.getInt(3), result.getDouble(4)));
        }
        return orderDetailDTO;
    }

    @Override
    public boolean save(OrderDetail orderDetailDTO, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "INSERT INTO OrderDetail VALUES(?,?,?,?)", orderDetailDTO.getOrderId(), orderDetailDTO.getItemCode(), orderDetailDTO.getQty(), orderDetailDTO.getTotal());
    }

    @Override
    public boolean update(OrderDetail dto, Connection connection) {
        return false;
    }

    @Override
    public ArrayList<OrderDetail> searchId(String id, Connection connection) {
        return null;
    }

    @Override
    public boolean delete(String s, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID(Connection connection) {
        return null;
    }
}
