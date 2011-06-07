package pl.softwaremill.demo.servlets;

import org.joda.time.DateTime;
import pl.softwaremill.demo.QueueListener;
import pl.softwaremill.demo.impl.hibernate.HibernateMessageAdder;
import pl.softwaremill.demo.impl.hibernate.SessionFactoryProvider;
import pl.softwaremill.demo.impl.jms.JMSQueueService;
import pl.softwaremill.demo.impl.sdb.AwsAccessKeys;
import pl.softwaremill.demo.entity.Message;
import pl.softwaremill.demo.impl.sdb.MessagesDomainProvider;
import pl.softwaremill.demo.impl.sdb.SDBMessageAdder;
import pl.softwaremill.demo.service.MessageAdder;
import pl.softwaremill.demo.service.QueueService;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class MessageAdderServlet extends HttpServlet {
    private QueueService queueService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        queueService = (QueueService) config.getServletContext().getAttribute(ContextSetup.QUEUE_SERVICE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Message message = new Message(
                UUID.randomUUID(),
                req.getParameter("room"),
                req.getParameter("content"),
                new DateTime(),
                null
        );

        resp.getWriter().print("OK");

        queueService.sendMessage(message);
    }
}
