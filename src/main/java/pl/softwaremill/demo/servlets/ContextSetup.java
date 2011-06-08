package pl.softwaremill.demo.servlets;

import com.google.common.collect.ImmutableMap;
import org.hibernate.SessionFactory;
import pl.softwaremill.common.conf.Configuration;
import pl.softwaremill.common.conf.PropertiesProvider;
import pl.softwaremill.demo.QueueListener;
import pl.softwaremill.demo.impl.hibernate.HibernateMessageAdder;
import pl.softwaremill.demo.impl.hibernate.HibernateMessageLister;
import pl.softwaremill.demo.impl.hibernate.SessionFactoryProvider;
import pl.softwaremill.demo.impl.jms.JMSQueueService;
import pl.softwaremill.demo.impl.sdb.AwsAccessKeys;
import pl.softwaremill.demo.impl.sdb.MessagesDomainProvider;
import pl.softwaremill.demo.impl.sdb.SDBMessageAdder;
import pl.softwaremill.demo.impl.sdb.SDBMessageLister;
import pl.softwaremill.demo.impl.sqs.SQSQueueService;
import pl.softwaremill.demo.service.MessageAdder;
import pl.softwaremill.demo.service.MessagesLister;
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

        MessageAdder messageAdder;
        MessagesLister messagesLister;
        QueueService queueService;

        if (System.getProperty("local") == null) {
            AwsAccessKeys awsAccessKeys;
            try {
                awsAccessKeys = AwsAccessKeys.createFromResources();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            setupSQSPropertiesProvider(awsAccessKeys);
            MessagesDomainProvider messagesDomainProvider = new MessagesDomainProvider(awsAccessKeys);

            messageAdder = new SDBMessageAdder(messagesDomainProvider);
            messagesLister = new SDBMessageLister(messagesDomainProvider);
            queueService = new SQSQueueService();
        } else {
            SessionFactory sessionFactory = new SessionFactoryProvider().getSessionFactory();

            messageAdder = new HibernateMessageAdder(sessionFactory);
            messagesLister = new HibernateMessageLister(sessionFactory);
            queueService = new JMSQueueService();
        }

        context.setAttribute(MESSAGE_ADDER, messageAdder);
        context.setAttribute(MESSAGE_LISTER, messagesLister);
        context.setAttribute(QUEUE_SERVICE, queueService);

        QueueListener queueListener = new QueueListener(messageAdder, queueService);

        new Thread(queueListener).start();

        context.setAttribute(QUEUE_LISTENER, queueListener);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ((QueueListener) sce.getServletContext().getAttribute(QUEUE_LISTENER)).stop();
    }

    private void setupSQSPropertiesProvider(final AwsAccessKeys awsAccessKeys) {
        Configuration.registerPropertiesProvider(new PropertiesProvider() {
            @Override
            public ImmutableMap<String, String> lookupProperties(String name) {
                if ("sqs".equals(name)) {
                    return ImmutableMap.of("sqsServer", "sqs.eu-west-1.amazonaws.com",
                            "AWSAccessKeyId", awsAccessKeys.getAccessKeyId(),
                            "SecretAccessKey", awsAccessKeys.getSecretAccessKey());
                }

                return null;
            }
        });
    }
}
