package Servlet;

import model.CustomerDTO;
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
@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<CustomerDTO> obList = new ArrayList<>();
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM Customer");
            while (result.next()) {
                obList.add(new CustomerDTO(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4)));
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        JsonArrayBuilder allCustomers = Json.createArrayBuilder();
        for (CustomerDTO customerDTO : obList) {
            JsonObjectBuilder customer = Json.createObjectBuilder();
            customer.add("id", customerDTO.getId());
            customer.add("name", customerDTO.getName());
            customer.add("address", customerDTO.getAddress());
            customer.add("salary", customerDTO.getSalary());
            allCustomers.add(customer.build());
        }
        resp.addHeader("Content-Type", "application/json");
        resp.getWriter().print(allCustomers.build());
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String id = req.getParameter("txtCusId");
        String name = req.getParameter("txtCusName");
        String address = req.getParameter("txtCusAddress");
        double salary = Double.parseDouble(req.getParameter("txtCustomerSalary"));

        try {
            //Save Customer
            CustomerDTO c = new CustomerDTO(id, name, address, salary);
            CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?)", c.getId(), c.getName(), c.getAddress(), c.getSalary());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();

        String id = customer.getString("id");
        String name = customer.getString("name");
        String address = customer.getString("address");
        double salary = Double.parseDouble(customer.getString("salary"));

        //Update Customer
        CustomerDTO cU = new CustomerDTO(id, name, address, salary);
        try {
            CrudUtil.execute("UPDATE Customer SET name= ? , address=? , salary=? WHERE id=?", cU.getName(), cU.getAddress(), cU.getSalary(), cU.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("txtCusId");

        //Delete Customer
        try {
            CrudUtil.execute("DELETE FROM Customer WHERE id=?", id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

