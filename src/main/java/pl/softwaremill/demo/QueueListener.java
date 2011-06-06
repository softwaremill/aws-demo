package pl.softwaremill.demo;

import javax.jms.*;
import javax.naming.InitialContext;

/**
 * User: szimano
 */
public class QueueListener implements Runnable {
    boolean runs = true;


    @Override
    public void run() {
        while (runs) {
            System.out.println("Reading from the queue");

            // read from the queue

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

                    ObjectMessage messageReceived = (ObjectMessage) messageConsumer.receive(5000);

                    // got message
                    if (messageReceived != null)
                        System.out.println("Message read: " + messageReceived.getObject());
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

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //
            }
        }
    }
}
