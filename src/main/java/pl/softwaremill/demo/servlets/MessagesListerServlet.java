package pl.softwaremill.demo.servlets;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import pl.softwaremill.demo.impl.hibernate.HibernateMessageLister;
import pl.softwaremill.demo.impl.sdb.AwsAccessKeys;
import pl.softwaremill.demo.MessageListSerializer;
import pl.softwaremill.demo.entity.Message;
import pl.softwaremill.demo.impl.sdb.MessagesDomainProvider;
import pl.softwaremill.demo.impl.sdb.SDBMessageLister;
import pl.softwaremill.demo.service.MessagesLister;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class MessagesListerServlet extends HttpServlet {
    private MessagesLister messagesLister;

    @Override
    public void init() throws ServletException {
//        try {
//            AwsAccessKeys awsAccessKeys = AwsAccessKeys.createFromResources();
//
//            MessagesDomainProvider messagesDomainProvider = new MessagesDomainProvider(awsAccessKeys);
//
//            messagesLister = new SDBMessageLister(messagesDomainProvider);
//
//        } catch (IOException e) {
//            throw new ServletException(e);
//        }

        // start hibernate
        SessionFactory sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();

        messagesLister = new HibernateMessageLister(sessionFactory);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Message> messages = messagesLister.listRecentMessages(req.getParameter("room"));

        MessageListSerializer messageListSerializer = new MessageListSerializer();
        String serialized = messageListSerializer.serialize(messages);

        resp.setContentType("application/json");
        resp.getWriter().print(serialized);
    }
}
