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
import java.io.PrintWriter;
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
        JsonArrayBuilder allItems = Json.createArrayBuilder();
        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        String code = req.getParameter("code");
        String option = req.getParameter("option");

        PrintWriter writer = resp.getWriter();
        if (option.equals("searchItemCode")) {
            try {
                ResultSet result = CrudUtil.execute("SELECT * FROM Item WHERE code=?", code);
                if (result.next()) {
                    JsonObjectBuilder item = Json.createObjectBuilder();
                    item.add("code", result.getString(1));
                    item.add("description", result.getString(2));
                    item.add("qty", String.valueOf(result.getInt(3)));
                    item.add("unitPrice", String.valueOf(result.getDouble(4)));
                    writer.print(item.build());

                } else {
                    throw new RuntimeException("Empty Result..!");
                }

            } catch (SQLException | ClassNotFoundException e) {

                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state", "Error");
                rjo.add("message", e.getLocalizedMessage());
                rjo.add("data", "");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print(rjo.build());
            }

        } else if (option.equals("loadAllItem")) {
            try {
                ResultSet result = CrudUtil.execute("SELECT * FROM Item");
                while (result.next()) {
                    obList.add(new ItemDTO(result.getString(1), result.getString(2), result.getInt(3), result.getDouble(4)));
                }
                for (ItemDTO itemDTO : obList) {
                    JsonObjectBuilder item = Json.createObjectBuilder();
                    item.add("code", itemDTO.getCode());
                    item.add("description", itemDTO.getDescription());
                    item.add("qty", itemDTO.getQty());
                    item.add("unitPrice", itemDTO.getUnitPrice());
                    allItems.add(item.build());
                }

                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("state", "Ok");
                job.add("message", "Successfully Loaded..!");
                job.add("data", allItems.build());
                resp.getWriter().print(job.build());

            } catch (ClassNotFoundException | SQLException e) {
                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state", "Error");
                rjo.add("message", e.getLocalizedMessage());
                rjo.add("data", "");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print(rjo.build());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getParameter("code");
        String description = req.getParameter("description");
        int qty = Integer.parseInt(req.getParameter("qty"));
        double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));

        resp.addHeader("Access-Control-Allow-Origin", "*");


        try {
            //Save Item
            ItemDTO i = new ItemDTO(code, description, qty, unitPrice);
            boolean b = CrudUtil.execute("INSERT INTO Item VALUES (?,?,?,?)", i.getCode(), i.getDescription(), i.getQty(), i.getUnitPrice());
            if (b) {

                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state", "Ok");
                responseObject.add("message", "Successfully added..!");
                responseObject.add("data", "");
                resp.getWriter().print(responseObject.build());

            }
        } catch (SQLException e) {

            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state", "Error");
            error.add("message", e.getLocalizedMessage());
            error.add("data", "");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(error.build());

        } catch (ClassNotFoundException e) {

            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state", "Error");
            error.add("message", e.getLocalizedMessage());
            error.add("data", "");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(error.build());

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
        resp.setContentType("application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        //Update Item
        ItemDTO iU = new ItemDTO(code, description, qty, unitPrice);
        try {
            boolean b = CrudUtil.execute("UPDATE Item SET description= ? , qty=? , unitPrice=? WHERE code=?", iU.getDescription(), iU.getQty(), iU.getUnitPrice(), iU.getCode());
            if (b) {

                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state", "Ok");
                responseObject.add("message", "Successfully Updated..!");
                responseObject.add("data", "");
                resp.getWriter().print(responseObject.build());

            } else {
                throw new RuntimeException("Wrong Code, Please Check The Code..!");
            }

        } catch (RuntimeException e) {

            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state", "Error");
            rjo.add("message", e.getLocalizedMessage());
            rjo.add("data", "");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());

        } catch (ClassNotFoundException | SQLException e) {

            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state", "Error");
            rjo.add("message", e.getLocalizedMessage());
            rjo.add("data", "");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());

        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject item = reader.readObject();

        String code = item.getString("code");
        resp.setContentType("application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        //Delete Item
        try {
            boolean b = CrudUtil.execute("DELETE FROM Item WHERE code=?", code);
            if (b) {

                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state", "Ok");
                rjo.add("message", "Successfully Deleted..!");
                rjo.add("data", "");
                resp.getWriter().print(rjo.build());

            } else {
                throw new RuntimeException("There is no such Item for that ID..!");
            }
        } catch (RuntimeException e) {

            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state", "Error");
            rjo.add("message", e.getLocalizedMessage());
            rjo.add("data", "");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());

        } catch (ClassNotFoundException | SQLException e) {

            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state", "Error");
            rjo.add("message", e.getLocalizedMessage());
            rjo.add("data", "");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());

        }
    }

}
