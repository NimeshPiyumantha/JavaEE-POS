package Servlet; /**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/

import model.CustomerDTO;
import util.CrudUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CustomerServlet", value = "/CustomerServlet",urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("txtCusId");
        String name = request.getParameter("txtCusName");
        String address = request.getParameter("txtCusAddress");
        double salary = Double.parseDouble(request.getParameter("txtCustomerSalary"));
        String option = request.getParameter("option");

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