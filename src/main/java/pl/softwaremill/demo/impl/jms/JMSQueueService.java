package pl.softwaremill.demo.impl.jms;

import pl.softwaremill.demo.entity.Message;
import pl.softwaremill.demo.service.QueueService;

import javax.jms.*;
import javax.naming.InitialContext;

/**
 * User: szimano
 */
public class JMSQueueService implements QueueService {

    @Override
    public void sendMessage(Message message) {
        try {
            Connection connection = null;
            InitialContext initialContext = null;
            try {
                initialContext = new InitialContext();

                Queue queue = (Queue) initialContext.lookup("java:comp/env/jms/queues/MessageQueue");

                ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("/ConnectionFactory");

                connection = cf.createConnection();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                MessageProducer producer = session.createProducer(queue);

                connection.start();

                producer.send(session.createObjectMessage(message));

                System.out.println("Message sent");
            } finally {
                if (initialContext != null) {
                    initialContext.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message readMessage() {
        try {
            Connection connection = null;
            InitialContext initialContext = null;
            try {
                initialContext = new InitialContext();

                Queue queue = (Queue) initialContext.lookup("java:comp/env/jms/queues/MessageQueue");

                ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("/ConnectionFactory");

                connection = cf.createConnection();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                MessageConsumer messageConsumer = session.createConsumer(queue);

                connection.start();

                ObjectMessage messageReceived = (ObjectMessage) messageConsumer.receive();

                if (messageReceived != null) {
                    return (Message) messageReceived.getObject();
                }

                return null;
            } finally {
                if (initialContext != null) {
                    initialContext.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
