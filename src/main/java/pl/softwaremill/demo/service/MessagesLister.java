package pl.softwaremill.demo.service;

import pl.softwaremill.demo.entity.Message;

import java.util.List;

/**
 * User: szimano
 */
public interface MessagesLister {
    List<Message> listRecentMessages(String room);
}
