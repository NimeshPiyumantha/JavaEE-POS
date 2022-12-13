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
        JsonArrayBuilder allCustomers = Json.createArrayBuilder();
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM Customer");
            while (result.next()) {
                obList.add(new CustomerDTO(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4)));
            }
            for (CustomerDTO customerDTO : obList) {
                JsonObjectBuilder customer = Json.createObjectBuilder();
                customer.add("id", customerDTO.getId());
                customer.add("name", customerDTO.getName());
                customer.add("address", customerDTO.getAddress());
                customer.add("salary", customerDTO.getSalary());
                allCustomers.add(customer.build());
            }

            resp.addHeader("Content-Type", "application/json");

            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("state", "Ok");
            job.add("message", "Successfully Loaded..!");
            job.add("data", allCustomers.build());
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


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String id = req.getParameter("txtCusId");
        String name = req.getParameter("txtCusName");
        String address = req.getParameter("txtCusAddress");
        double salary = Double.parseDouble(req.getParameter("txtCustomerSalary"));

        try {
            //Save Customer
            CustomerDTO c = new CustomerDTO(id, name, address, salary);
            boolean b = CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?)", c.getId(), c.getName(), c.getAddress(), c.getSalary());
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
        JsonObject customer = reader.readObject();

        String id = customer.getString("id");
        String name = customer.getString("name");
        String address = customer.getString("address");
        double salary = Double.parseDouble(customer.getString("salary"));
        resp.setContentType("application/json");

        //Update Customer
        CustomerDTO cU = new CustomerDTO(id, name, address, salary);
        try {
            boolean b = CrudUtil.execute("UPDATE Customer SET name= ? , address=? , salary=? WHERE id=?", cU.getName(), cU.getAddress(), cU.getSalary(), cU.getId());
            if (b) {

                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state", "Ok");
                responseObject.add("message", "Successfully Updated..!");
                responseObject.add("data", "");
                resp.getWriter().print(responseObject.build());

            } else {
                throw new RuntimeException("Wrong ID, Please Check The ID..!");
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
        JsonObject customer = reader.readObject();

        String id = customer.getString("id");
        resp.setContentType("application/json");
        //Delete Customer
        try {
            boolean b = CrudUtil.execute("DELETE FROM Customer WHERE id=?", id);
            if (b) {

                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state", "Ok");
                rjo.add("message", "Successfully Deleted..!");
                rjo.add("data", "");
                resp.getWriter().print(rjo.build());

            } else {
                throw new RuntimeException("There is no such customer for that ID..!");
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

