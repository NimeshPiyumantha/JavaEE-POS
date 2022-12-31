package bo.custom;

import bo.SuperBO;
import dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public interface ItemBO extends SuperBO {
    ArrayList<ItemDTO> getAllItems(Connection connection) throws SQLException, ClassNotFoundException;

    boolean deleteItem(String code, Connection connection) throws SQLException, ClassNotFoundException;

    boolean saveItem(ItemDTO dto, Connection connection) throws SQLException, ClassNotFoundException;

    boolean updateItem(ItemDTO dto, Connection connection) throws SQLException, ClassNotFoundException;

    String generateNewItemCode(Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> itemSearchId(String id, Connection connection) throws SQLException, ClassNotFoundException;

}
