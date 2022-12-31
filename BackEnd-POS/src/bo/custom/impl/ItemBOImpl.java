package bo.custom.impl;

import bo.custom.ItemBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dto.ItemDTO;
import entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class ItemBOImpl implements ItemBO {

    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public ArrayList<ItemDTO> getAllItems(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Item> allI = itemDAO.getAll(connection);
        ArrayList<ItemDTO> allItem = new ArrayList<>();

        for (Item item : allI) {
            allItem.add(new ItemDTO(item.getCode(), item.getDescription(), item.getQty(), item.getUnitPrice()));
        }
        return allItem;
    }

    @Override
    public boolean deleteItem(String code, Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(code, connection);
    }

    @Override
    public boolean saveItem(ItemDTO dto, Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.save(new Item(dto.getCode(), dto.getDescription(), dto.getQty(), dto.getUnitPrice()), connection);
    }

    @Override
    public boolean updateItem(ItemDTO dto, Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.update(new Item(dto.getCode(), dto.getDescription(), dto.getQty(), dto.getUnitPrice()), connection);
    }

    @Override
    public String generateNewItemCode(Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.generateNewID(connection);
    }

    @Override
    public ArrayList<ItemDTO> itemSearchId(String id, Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Item> all = itemDAO.searchId(id, connection);
        ArrayList<ItemDTO> allItem = new ArrayList<>();
        for (Item item : all) {
            allItem.add(new ItemDTO(item.getCode(), item.getDescription(), item.getQty(), item.getUnitPrice()));
        }
        return allItem;
    }
}
