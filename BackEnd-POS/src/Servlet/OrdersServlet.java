package Servlet;

import model.CustomerDTO;
import model.OrderDTO;
import model.OrderDetailDTO;
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

@WebServlet(urlPatterns = "/orders")
public class OrdersServlet extends HttpServlet {
    @Override

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        JsonArray oDetail = jsonObject.getJsonArray("detail");

        System.out.println(oDetail);

        String customerId = jsonObject.getString("customerId");
        String date = jsonObject.getString("date");
        String orderId = jsonObject.getString("orderId");
        System.out.println(customerId + " " + date + " " + orderId);

        try {
            OrderDTO orderDTO = new OrderDTO(orderId, date, customerId);
            CrudUtil.execute("INSERT INTO orders VALUES(?,?,?)", orderDTO.getId(), orderDTO.getDate(), orderDTO.getCustomerId());

            for (JsonValue orderDetail : oDetail) {
                JsonObject object = orderDetail.asJsonObject();

                String orId = object.getString("orderId");
                String itId = object.getString("itemId");
                int qty = Integer.parseInt(object.getString("qty"));
                double price = Double.parseDouble(object.getString("unitPrice"));
                OrderDetailDTO orderDetailDTO = new OrderDetailDTO(orId, itId, qty, price);
                CrudUtil.execute("INSERT INTO OrderDetail VALUES(?,?,?,?)", orderDetailDTO.getOrderId(), orderDetailDTO.getItemCode(), orderDetailDTO.getQty(), orderDetailDTO.getTotal());
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
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "PUT,DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "content-type");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();
        ArrayList<OrderDetailDTO> orderDetailDTO = new ArrayList<>();
        JsonArrayBuilder allOrders = Json.createArrayBuilder();
        JsonArrayBuilder allOrderDetails = Json.createArrayBuilder();
        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        String option = req.getParameter("option");
        PrintWriter writer = resp.getWriter();

        if (option.equals("OrderId")) {
            try {
                ResultSet result = CrudUtil.execute("SELECT orderId FROM `Orders` ORDER BY orderId DESC LIMIT 1");

                JsonObjectBuilder ordID = Json.createObjectBuilder();
                ordID.add("orderId", result.getString(1));
                System.out.println(ordID);
                writer.print(ordID.build());


            } catch (SQLException | ClassNotFoundException e) {

                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state", "Error");
                rjo.add("message", e.getLocalizedMessage());
                rjo.add("data", "");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print(rjo.build());
            }

        } else if (option.equals("LoadOrders")) {
            try {
                ResultSet result = CrudUtil.execute("SELECT * FROM `Orders`");
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
        } else if (option.equals("LoadOrderDetails")) {
            try {
                ResultSet result = CrudUtil.execute("SELECT * FROM `OrderDetail`");
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

        }

    }
}