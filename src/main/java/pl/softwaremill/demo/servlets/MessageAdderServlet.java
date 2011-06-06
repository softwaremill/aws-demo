package pl.softwaremill.demo.servlets;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.joda.time.DateTime;
import pl.softwaremill.demo.impl.hibernate.HibernateMessageAdder;
import pl.softwaremill.demo.impl.hibernate.HibernateMessageLister;
import pl.softwaremill.demo.impl.hibernate.SessionFactoryProvider;
import pl.softwaremill.demo.impl.sdb.AwsAccessKeys;
import pl.softwaremill.demo.entity.Message;
import pl.softwaremill.demo.impl.sdb.MessagesDomainProvider;
import pl.softwaremill.demo.impl.sdb.SDBMessageAdder;
import pl.softwaremill.demo.service.MessageAdder;

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
    private MessageAdder messageAdder;

    @Override
    public void init() throws ServletException {

        if (System.getProperty("local") == null) {
            try {
                AwsAccessKeys awsAccessKeys = AwsAccessKeys.createFromResources();

                MessagesDomainProvider messagesDomainProvider = new MessagesDomainProvider(awsAccessKeys);
                messageAdder = new SDBMessageAdder(messagesDomainProvider);
            } catch (IOException e) {
                throw new ServletException(e);
            }
        }
        else {
            messageAdder = new HibernateMessageAdder(new SessionFactoryProvider().getSessionFactory());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Message message = new Message(
                UUID.randomUUID(),
                req.getParameter("room"),
                req.getParameter("content"),
                new DateTime()
        );

        messageAdder.addMessage(message);

        resp.getWriter().print("OK");
    }
}
