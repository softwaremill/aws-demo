package pl.softwaremill.demo;

import pl.softwaremill.demo.entity.*;
import pl.softwaremill.demo.entity.Message;
import pl.softwaremill.demo.service.MessageAdder;
import pl.softwaremill.demo.service.MessagesLister;
import pl.softwaremill.demo.service.QueueService;


/**
 * User: szimano
 */
public class QueueListener implements Runnable {
    boolean runs = true;

    private MessageAdder messageAdder;
    private QueueService queueService;

    public QueueListener(MessageAdder messageAdder, QueueService queueService) {
        this.messageAdder = messageAdder;
        this.queueService = queueService;
    }

    @Override
    public void run() {
        while (runs) {
            Message messageReceived;

            // read from the queue
            if ((messageReceived = queueService.readMessage()) != null) {
                System.out.println("Message read: " + messageReceived);

                System.out.println("Processing");

                // Simulate message processing
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //
                }

                messageAdder.addMessage(messageReceived);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //
            }
        }
    }

    public void stop() {
        runs = false;
    }
}
