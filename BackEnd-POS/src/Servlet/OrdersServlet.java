package Servlet;

import model.OrderDTO;
import model.OrderDetailDTO;
import util.CrudUtil;

import javax.annotation.Resource;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/

@WebServlet(urlPatterns = "/orders")
public class OrdersServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    @Override

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        JsonArray oDetail = jsonObject.getJsonArray("detail");

        System.out.println(oDetail);

        String customerId = jsonObject.getString("customerId");
        String date = jsonObject.getString("date");
        String orderId = jsonObject.getString("orderId");
        System.out.println(customerId + " " + date + " " + orderId);

        try (Connection connection = dataSource.getConnection()) {
            OrderDTO orderDTO = new OrderDTO(orderId, date, customerId);
            CrudUtil.execute(connection, "INSERT INTO orders VALUES(?,?,?)", orderDTO.getId(), orderDTO.getDate(), orderDTO.getCustomerId());

            for (JsonValue orderDetail : oDetail) {
                JsonObject object = orderDetail.asJsonObject();

                String orId = object.getString("orderId");
                String itId = object.getString("itemId");
                int qty = Integer.parseInt(object.getString("qty"));
                double price = Double.parseDouble(object.getString("unitPrice"));
                OrderDetailDTO orderDetailDTO = new OrderDetailDTO(orId, itId, qty, price);
                CrudUtil.execute(connection, "INSERT INTO OrderDetail VALUES(?,?,?,?)", orderDetailDTO.getOrderId(), orderDetailDTO.getItemCode(), orderDetailDTO.getQty(), orderDetailDTO.getTotal());
            }
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("state", "Ok");
            job.add("message", "Successfully Place Order..!");
            job.add("data", "");
            resp.getWriter().print(job.build());
        } catch (SQLException | ClassNotFoundException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state", "Error");
            rjo.add("message", e.getLocalizedMessage());
            rjo.add("data", "");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();
        ArrayList<OrderDetailDTO> orderDetailDTO = new ArrayList<>();
        JsonArrayBuilder allOrders = Json.createArrayBuilder();
        JsonArrayBuilder allOrderDetails = Json.createArrayBuilder();

        String option = req.getParameter("option");
        PrintWriter writer = resp.getWriter();

        switch (option) {
            case "OrderIdGenerate":
                try (Connection connection = dataSource.getConnection()) {
                    JsonObjectBuilder ordID = Json.createObjectBuilder();
                    ResultSet result = CrudUtil.execute(connection, "SELECT orderId FROM `Orders` ORDER BY orderId DESC LIMIT 1");
                    while (result.next()) {
                        ordID.add("orderId", result.getString(1));
                    }
                    writer.print(ordID.build());


                } catch (SQLException | ClassNotFoundException e) {

                    JsonObjectBuilder rjo = Json.createObjectBuilder();
                    rjo.add("state", "Error");
                    rjo.add("message", e.getLocalizedMessage());
                    rjo.add("data", "");
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(rjo.build());
                }

                break;
            case "LoadOrders":
                try (Connection connection = dataSource.getConnection()) {
                    ResultSet result = CrudUtil.execute(connection, "SELECT * FROM `Orders`");
                    while (result.next()) {
                        orderDTOS.add(new OrderDTO(result.getString(1), result.getString(2), result.getString(3)));
                    }
                    for (OrderDTO customerDTO : orderDTOS) {
                        JsonObjectBuilder order = Json.createObjectBuilder();
                        order.add("orderId", customerDTO.getId());
                        order.add("date", customerDTO.getDate());
                        order.add("cusId", customerDTO.getCustomerId());
                        allOrders.add(order.build());
                    }

                    JsonObjectBuilder job = Json.createObjectBuilder();
                    job.add("state", "Ok");
                    job.add("message", "Successfully Loaded..!");
                    job.add("data", allOrders.build());
                    resp.getWriter().print(job.build());

                } catch (ClassNotFoundException | SQLException e) {
                    JsonObjectBuilder rjo = Json.createObjectBuilder();
                    rjo.add("state", "Error");
                    rjo.add("message", e.getLocalizedMessage());
                    rjo.add("data", "");
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(rjo.build());
                }
                break;
            case "LoadOrderDetails":
                try (Connection connection = dataSource.getConnection()) {
                    ResultSet result = CrudUtil.execute(connection, "SELECT * FROM `OrderDetail`");
                    while (result.next()) {
                        orderDetailDTO.add(new OrderDetailDTO(result.getString(1), result.getString(2), result.getInt(3), result.getDouble(4)));
                    }
                    for (OrderDetailDTO customerDTO : orderDetailDTO) {
                        JsonObjectBuilder orderDetails = Json.createObjectBuilder();
                        orderDetails.add("OrderId", customerDTO.getOrderId());
                        orderDetails.add("code", customerDTO.getItemCode());
                        orderDetails.add("qty", customerDTO.getQty());
                        orderDetails.add("unitPrice", customerDTO.getTotal());
                        allOrderDetails.add(orderDetails.build());
                    }

                    JsonObjectBuilder job = Json.createObjectBuilder();
                    job.add("state", "Ok");
                    job.add("message", "Successfully Loaded..!");
                    job.add("data", allOrderDetails.build());
                    resp.getWriter().print(job.build());

                } catch (ClassNotFoundException | SQLException e) {
                    JsonObjectBuilder rjo = Json.createObjectBuilder();
                    rjo.add("state", "Error");
                    rjo.add("message", e.getLocalizedMessage());
                    rjo.add("data", "");
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(rjo.build());
                }

                break;
        }

    }
}
