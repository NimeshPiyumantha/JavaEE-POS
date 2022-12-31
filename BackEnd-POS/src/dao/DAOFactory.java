package dao;

import dao.custom.impl.CustomerDAOImpl;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return daoFactory == null ? daoFactory = new DAOFactory() : daoFactory;
    }

    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerDAOImpl();

            default:
                return null;
        }
    }

    public enum DAOTypes {
        CUSTOMER
    }
}