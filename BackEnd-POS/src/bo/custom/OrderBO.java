package bo.custom;

import bo.SuperBO;
import dto.OrderDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public interface OrderBO extends SuperBO {
    boolean purchaseOrder(OrderDTO dto, Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<OrderDTO> getAllOrders(Connection connection) throws SQLException, ClassNotFoundException;

    String generateNewOrder(Connection connection) throws SQLException, ClassNotFoundException;

    boolean mangeItems(int qty, String code, Connection connection) throws SQLException, ClassNotFoundException;

}
