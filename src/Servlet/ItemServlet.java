package Servlet;

import model.ItemDTO;
import util.CrudUtil;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<ItemDTO> obList = new ArrayList<>();
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM Item");
            while (result.next()) {
                obList.add(new ItemDTO(result.getString(1), result.getString(2), result.getInt(3), result.getDouble(4)));
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        JsonArrayBuilder allItems = Json.createArrayBuilder();
        for (ItemDTO itemDTO : obList) {
            JsonObjectBuilder item = Json.createObjectBuilder();
            item.add("code", itemDTO.getCode());
            item.add("description", itemDTO.getDescription());
            item.add("qty", itemDTO.getQty());
            item.add("unitPrice", itemDTO.getUnitPrice());
            allItems.add(item.build());
        }

        resp.addHeader("Content-Type", "application/json");
        resp.getWriter().print(allItems.build());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getParameter("txtItemID");
        String description = req.getParameter("txtItemName");
        int qty = Integer.parseInt(req.getParameter("txtItemQty"));
        double unitPrice = Double.parseDouble(req.getParameter("txtItemPrice"));


        try {
            //Save Item
            ItemDTO i = new ItemDTO(code, description, qty, unitPrice);
            CrudUtil.execute("INSERT INTO Item VALUES (?,?,?,?)", i.getCode(), i.getDescription(), i.getQty(), i.getUnitPrice());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject item = reader.readObject();

        String code = item.getString("code");
        String description = item.getString("description");
        int qty = Integer.parseInt(item.getString("qty"));
        double unitPrice = Double.parseDouble(item.getString("unitPrice"));

        //Update Item
        ItemDTO iU = new ItemDTO(code, description, qty, unitPrice);
        try {
            CrudUtil.execute("UPDATE Item SET description= ? , qtyOnHand=? , unitPrice=? WHERE code=?", iU.getDescription(), iU.getQty(), iU.getUnitPrice(), iU.getCode());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject item = reader.readObject();

        String code = item.getString("code");

        //Delete Item
        try {
            CrudUtil.execute("DELETE FROM Item WHERE code=?", code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
