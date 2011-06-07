package pl.softwaremill.demo.service;

import pl.softwaremill.demo.entity.Message;

/**
 * User: szimano
 */
public interface QueueService {

    void sendMessage(Message message);

    Message readMessage();
}
