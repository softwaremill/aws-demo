package pl.softwaremill.demo.impl.sdb;

import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class AwsAccessKeys {
    private final String accessKeyId;
    private final String secretAccessKey;

    private AwsAccessKeys(String accessKeyId, String secretAccessKey) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public static AwsAccessKeys createFromResources() throws IOException {
        Properties props = new Properties();
        InputStream propsIS = Resources.getResource("aws.properties").openStream();
        try {
            props.load(propsIS);
        } finally {
            propsIS.close();
        }

        return new AwsAccessKeys(props.getProperty("access.key.id"), props.getProperty("secret.access.key"));
    }
}
