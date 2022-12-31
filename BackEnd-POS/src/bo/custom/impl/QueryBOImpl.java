package bo.custom.impl;

import bo.custom.QueryBO;
import dao.DAOFactory;
import dao.custom.QueryDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class QueryBOImpl implements QueryBO {

    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOM);

    @Override
    public int getSumOrders(Connection connection) throws SQLException, ClassNotFoundException {
        return queryDAO.getSumOrders(connection);
    }

    @Override
    public int getItem(Connection connection) throws SQLException, ClassNotFoundException {
        return queryDAO.getItem(connection);
    }

    @Override
    public int getCustomer(Connection connection) throws SQLException, ClassNotFoundException {
        return queryDAO.getCustomer(connection);
    }
}
