package bo.custom.impl;

import bo.custom.OrderBO;
import dao.DAOFactory;
import dao.custom.OrderDAO;
import dto.OrderDTO;
import entity.Orders;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class OrderBOImpl implements OrderBO {
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);

    @Override
    public boolean purchaseOrder(OrderDTO dto, Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.save(new Orders(dto.getId(), dto.getDate(), dto.getCustomerId()), connection);
    }

    @Override
    public ArrayList<OrderDTO> getAllOrders(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Orders> all = orderDAO.getAll(connection);

        ArrayList<OrderDTO> allOrders = new ArrayList<>();
        for (Orders orders : all) {
            allOrders.add(new OrderDTO(orders.getOrderId(), orders.getOrderDate(), orders.getCusId()));
        }
        return allOrders;
    }

    @Override
    public String generateNewOrder(Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewID(connection);
    }

    @Override
    public boolean mangeItems(int qty, String code, Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.mangeItems(qty, code, connection);
    }
}
