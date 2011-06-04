package pl.softwaremill.demo;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.xerox.amazonws.simpledb.SDBException;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Map;
import java.util.Set;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class MessageAdder {
    private final MessagesDomainProvider messagesDomainProvider;

    public MessageAdder(MessagesDomainProvider messagesDomainProvider) {
        this.messagesDomainProvider = messagesDomainProvider;
    }

    public void addMessage(Message msg) {
        System.out.println("Adding message: " + msg);

        SetMultimap<String, String> attrs = HashMultimap.create();
        attrs.put(MessageMappingConstants.ROOM, msg.getRoom());
        attrs.put(MessageMappingConstants.CONTENT, msg.getContent());
        attrs.put(MessageMappingConstants.DATE,
                ISODateTimeFormat.dateTimeNoMillis().withZone(DateTimeZone.UTC).print(msg.getDate()));

        try {
            String itemId = msg.getUuid().toString();
            Map<String,Set<String>> itemAttr = (Map<String,Set<String>>) (Map) attrs.asMap();

            messagesDomainProvider.getDomain().addItem(itemId, itemAttr, null);
        } catch (SDBException e) {
            throw new RuntimeException(e);
        }
    }
}
