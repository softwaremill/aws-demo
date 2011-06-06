package pl.softwaremill.demo;

import org.codehaus.jackson.map.ObjectMapper;
import pl.softwaremill.demo.entity.Message;

import java.io.IOException;
import java.util.List;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class MessageListSerializer {
    public String serialize(List<Message> messages) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(messages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
