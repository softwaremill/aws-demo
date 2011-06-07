package pl.softwaremill.demo.servlets;

import org.hibernate.SessionFactory;
import pl.softwaremill.demo.QueueListener;
import pl.softwaremill.demo.impl.hibernate.HibernateMessageAdder;
import pl.softwaremill.demo.impl.hibernate.HibernateMessageLister;
import pl.softwaremill.demo.impl.hibernate.SessionFactoryProvider;
import pl.softwaremill.demo.impl.jms.JMSQueueService;
import pl.softwaremill.demo.impl.sdb.AwsAccessKeys;
import pl.softwaremill.demo.impl.sdb.MessagesDomainProvider;
import pl.softwaremill.demo.impl.sdb.SDBMessageAdder;
import pl.softwaremill.demo.impl.sdb.SDBMessageLister;
import pl.softwaremill.demo.service.MessageAdder;
import pl.softwaremill.demo.service.QueueService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * User: szimano
 */
public class ContextSetup implements ServletContextListener {

    public static final String MESSAGE_LISTER = "message_lister";
    public static final String MESSAGE_ADDER = "message_adder";
    public static final String QUEUE_SERVICE = "queue_service";
    public static final String QUEUE_LISTENER = "queue_listener";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        MessageAdder messageAdder = null;
        QueueService queueService = null;

        if (System.getProperty("local") == null) {
            try {
                AwsAccessKeys awsAccessKeys = AwsAccessKeys.createFromResources();

                MessagesDomainProvider messagesDomainProvider = new MessagesDomainProvider(awsAccessKeys);
                context.setAttribute(MESSAGE_ADDER, messageAdder = new SDBMessageAdder(messagesDomainProvider));
                context.setAttribute(MESSAGE_LISTER, new SDBMessageLister(messagesDomainProvider));

                //TODO add sqsqueryservice
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            SessionFactory sessionFactory = new SessionFactoryProvider().getSessionFactory();

            context.setAttribute(MESSAGE_ADDER,
                    messageAdder = new HibernateMessageAdder(sessionFactory));
            context.setAttribute(MESSAGE_LISTER, new HibernateMessageLister(sessionFactory));

            context.setAttribute(QUEUE_SERVICE, queueService = new JMSQueueService());
        }

        QueueListener queueListener = new QueueListener(messageAdder, queueService);

        new Thread(queueListener).start();

        context.setAttribute(QUEUE_LISTENER, queueListener);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ((QueueListener) sce.getServletContext().getAttribute(QUEUE_LISTENER)).stop();
    }
}
