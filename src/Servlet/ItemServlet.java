package Servlet;

import model.ItemDTO;
import util.CrudUtil;

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

        String json = "[";

        for (ItemDTO itemDTO : obList) {
            json += "{\"code\":\"" + itemDTO.getCode() + "\",\"description\":\"" + itemDTO.getDescription() + "\",\"qty\":\"" + itemDTO.getQty() + "\",\"unitPrice\":" + itemDTO.getUnitPrice() + "},";
        }
        String finalArray = json.substring(0, json.length() - 1);
        finalArray += "]";

        resp.addHeader("Content-Type", "application/json");
        resp.getWriter().write(finalArray);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getParameter("txtItemID");
        String description = req.getParameter("txtItemName");
        int qty = Integer.parseInt(req.getParameter("txtItemQty"));
        double unitPrice = Double.parseDouble(req.getParameter("txtItemPrice"));
        String option = req.getParameter("option");

        try {
            switch (option) {
                //Save Item
                case "add":
                    ItemDTO i = new ItemDTO(code, description, qty, unitPrice);
                    CrudUtil.execute("INSERT INTO Item VALUES (?,?,?,?)", i.getCode(), i.getDescription(), i.getQty(), i.getUnitPrice());
                    break;

                case "update":
                    //Update Item
                    ItemDTO iU = new ItemDTO(code, description, qty, unitPrice);
                    CrudUtil.execute("UPDATE Item SET description= ? , qtyOnHand=? , unitPrice=? WHERE code=?", iU.getDescription(), iU.getQty(), iU.getUnitPrice(), iU.getCode());
                    break;

                case "delete":
                    //Delete Item
                    CrudUtil.execute("DELETE FROM Item WHERE code=?", code);
                    break;
            }
        } catch (SQLException | ClassNotFoundException ignored) {

        }

    }
}
