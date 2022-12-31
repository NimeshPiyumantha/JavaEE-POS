package dao.custom.impl;

import dao.custom.CustomerDAO;
import dto.CustomerDTO;
import entity.Customer;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public ArrayList<Customer> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT * FROM Customer");
        ArrayList<Customer> obList = new ArrayList<>();
        while (result.next()) {
            obList.add(new Customer(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4)));
        }
        return obList;
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
        ResultSet rst = CrudUtil.execute(connection, "SELECT * FROM Customer WHERE id=?", id);

        ArrayList<Customer> allCustomer = new ArrayList<>();
        while (rst.next()) {
            allCustomer.add(new Customer(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4)));
        }
        return allCustomer;
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
