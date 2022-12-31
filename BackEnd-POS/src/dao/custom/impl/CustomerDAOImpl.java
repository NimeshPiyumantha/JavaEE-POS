package dao.custom.impl;

import dao.custom.CustomerDAO;
import entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public ArrayList<Customer> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Customer dto, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Customer dto, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ArrayList<Customer> searchId(String id, Connection connection) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean delete(String s, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID(Connection connection) throws SQLException, ClassNotFoundException {
        return null;
    }
}
