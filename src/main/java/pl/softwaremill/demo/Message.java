package pl.softwaremill.demo;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class Message {
    private final UUID uuid;
    private final String room;
    private final String content;
    private final DateTime date;

    public Message(UUID uuid, String room, String content, DateTime date) {
        this.uuid = uuid;
        this.room = room;
        this.content = content;
        this.date = date;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getRoom() {
        return room;
    }

    public String getContent() {
        return content;
    }

    public DateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "uuid=" + uuid +
                ", room='" + room + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}
