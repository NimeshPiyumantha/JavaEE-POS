package Servlet;

import bo.BOFactory;
import bo.custom.OrderBO;
import bo.custom.OrderDetailsBO;
import bo.custom.QueryBO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
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
    private final QueryBO queryBO = (QueryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOM);
    private final OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDERS);
    private final OrderDetailsBO orderDetailBO = (OrderDetailsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDERDETAILS);


    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        JsonArray oDetail = jsonObject.getJsonArray("detail");

        String customerId = jsonObject.getString("customerId");
        String date = jsonObject.getString("date");
        String orderId = jsonObject.getString("orderId");

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            OrderDTO orderDTO = new OrderDTO(orderId, date, customerId);
            boolean b = orderBO.purchaseOrder(orderDTO, connection);
            if (!(b)) {
                connection.rollback();
                connection.setAutoCommit(true);

                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state", "Error");
                rjo.add("message", "Order Issue");
                rjo.add("data", "");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print(rjo.build());

            } else {
                for (JsonValue orderDetail : oDetail) {
                    JsonObject object = orderDetail.asJsonObject();

                    String orId = object.getString("orderId");
                    String itId = object.getString("itemId");
                    int qty = Integer.parseInt(object.getString("qty"));
                    double price = Double.parseDouble(object.getString("unitPrice"));

                    OrderDetailDTO orderDetailDTO = new OrderDetailDTO(orId, itId, qty, price);
                    boolean b1 = orderDetailBO.purchaseOrderDetails(orderDetailDTO, connection);

                    if (!(b1)) {
                        connection.rollback();
                        connection.setAutoCommit(true);

                        JsonObjectBuilder rjo = Json.createObjectBuilder();
                        rjo.add("state", "Error");
                        rjo.add("message", "Order Details Issue");
                        rjo.add("data", "");
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.getWriter().print(rjo.build());
                    }
                }
                connection.commit();
                connection.setAutoCommit(true);


                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("state", "Ok");
                job.add("message", "Successfully Place Order..!");
                job.add("data", "");
                resp.getWriter().print(job.build());

            }

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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        JsonArray oDetail = jsonObject.getJsonArray("detail");
        try (Connection connection = dataSource.getConnection()) {
            for (JsonValue orderDetail : oDetail) {
                JsonObject object = orderDetail.asJsonObject();

                String orId = object.getString("orderId");
                String itId = object.getString("itemId");
                int qty = Integer.parseInt(object.getString("qty"));
                double price = Double.parseDouble(object.getString("unitPrice"));

                orderBO.mangeItems(qty, itId, connection);

            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        JsonArrayBuilder allOrders = Json.createArrayBuilder();
        JsonArrayBuilder allOrderDetails = Json.createArrayBuilder();

        String option = req.getParameter("option");
        PrintWriter writer = resp.getWriter();

        switch (option) {
            case "OrderIdGenerate":
                try (Connection connection = dataSource.getConnection()) {
                    String orderId = orderBO.generateNewOrder(connection);

                    JsonObjectBuilder ordID = Json.createObjectBuilder();
                    ordID.add("orderId", orderId);
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
                    ArrayList<OrderDTO> orderDTOS = orderBO.getAllOrders(connection);

                    for (OrderDTO orderDTO : orderDTOS) {
                        JsonObjectBuilder order = Json.createObjectBuilder();
                        order.add("orderId", orderDTO.getId());
                        order.add("date", orderDTO.getDate());
                        order.add("cusId", orderDTO.getCustomerId());
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
                    ArrayList<OrderDetailDTO> orderDetailDTO = orderDetailBO.getAllOrderDetails(connection);

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
            case "ordersCount":
                try (Connection connection = dataSource.getConnection()) {
                    int countOrders = queryBO.getSumOrders(connection);

                    JsonObjectBuilder count = Json.createObjectBuilder();
                    count.add("count", countOrders);
                    writer.print(count.build());


                } catch (SQLException | ClassNotFoundException e) {

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
