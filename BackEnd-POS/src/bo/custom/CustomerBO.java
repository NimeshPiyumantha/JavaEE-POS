package bo.custom;

import bo.SuperBO;
import dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public interface CustomerBO extends SuperBO {
    ArrayList<CustomerDTO> getAllCustomers(Connection connection) throws SQLException, ClassNotFoundException;

    boolean saveCustomer(CustomerDTO dto, Connection connection) throws SQLException, ClassNotFoundException;

    boolean updateCustomer(CustomerDTO dto, Connection connection) throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(String id, Connection connection) throws SQLException, ClassNotFoundException;

    String generateNewCustomerID(Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<CustomerDTO> customerSearchId(String id, Connection connection) throws SQLException, ClassNotFoundException;

}
