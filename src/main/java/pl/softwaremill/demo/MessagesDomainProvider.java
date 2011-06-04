package pl.softwaremill.demo;

import com.xerox.amazonws.simpledb.Domain;
import com.xerox.amazonws.simpledb.SDBException;
import com.xerox.amazonws.simpledb.SimpleDB;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class MessagesDomainProvider {
    private final AwsAccessKeys awsAccessKeys;

    public MessagesDomainProvider(AwsAccessKeys awsAccessKeys) {
        this.awsAccessKeys = awsAccessKeys;
    }

    public Domain getDomain() throws SDBException {
        SimpleDB simpleDB = new SimpleDB(awsAccessKeys.getAccessKeyId(), awsAccessKeys.getSecretAccessKey(),
                true, "sdb.eu-west-1.amazonaws.com");
        return simpleDB.getDomain(MessageMappingConstants.MESSAGES_DOMAIN);
    }
}
