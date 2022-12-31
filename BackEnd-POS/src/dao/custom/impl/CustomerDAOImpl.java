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
        return CrudUtil.execute(connection, "INSERT INTO Customer VALUES (?,?,?,?)", dto.getId(), dto.getName(), dto.getAddress(), dto.getSalary());
    }

    @Override
    public boolean update(Customer dto, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "UPDATE Customer SET name= ? , address=? , salary=? WHERE id=?", dto.getName(), dto.getAddress(), dto.getSalary(), dto.getId());
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
    public boolean delete(String id, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "DELETE FROM Customer WHERE id=?", id);
    }

    @Override
    public String generateNewID(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT id FROM Customer ORDER BY id DESC LIMIT 1");
        if (result.next()) {
            return result.getString(1);
        } else {
            return null;
        }
    }
}
