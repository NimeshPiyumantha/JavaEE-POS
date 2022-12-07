package Servlet;

import model.CustomerDTO;
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

        String json = "[";

        for (CustomerDTO customer : obList) {
            json += "{\"id\":\"" + customer.getId() + "\",\"name\":\"" + customer.getName() + "\",\"address\":\"" + customer.getAddress() + "\",\"salary\":" + customer.getSalary() + "},";
        }
        String finalArray = json.substring(0, json.length()-1);
        finalArray += "]";

        resp.addHeader("Content-Type", "application/json");
        resp.getWriter().write(finalArray);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String id = req.getParameter("txtCusId");
        String name = req.getParameter("txtCusName");
        String address = req.getParameter("txtCusAddress");
        double salary = Double.parseDouble(req.getParameter("txtCustomerSalary"));
        String option = req.getParameter("option");

        try {
            switch (option) {
                //Save Customer
                case "add":
                    CustomerDTO c = new CustomerDTO(id, name, address, salary);
                    CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?)", c.getId(), c.getName(), c.getAddress(), c.getSalary());
                    break;

                case "update":
                    //Update Customer
                    CustomerDTO cU = new CustomerDTO(id, name, address, salary);
                    CrudUtil.execute("UPDATE Customer SET name= ? , address=? , salary=? WHERE id=?", cU.getName(), cU.getAddress(), cU.getSalary(), cU.getId());
                    break;

                case "delete":
                    //Delete Customer
                    CrudUtil.execute("DELETE FROM Customer WHERE id=?", id);
                    break;
            }
        } catch (SQLException | ClassNotFoundException ignored) {

        }
    }
}

