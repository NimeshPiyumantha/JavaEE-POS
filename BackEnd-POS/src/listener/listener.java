package listener;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
@WebListener
public class listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/ThogaKadeM");
        bds.setPassword("1234");
        bds.setUsername("root");
        bds.setMaxTotal(2);// how many connection
        bds.setInitialSize(2);// how many connection should be initialized from created connections

        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("poll",bds);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
