package com.linkedin.camus.etl.kafka.coders;



import com.linkedin.camus.coders.CamusWrapper;
import com.linkedin.camus.coders.MessageDecoder;
import com.linkedin.camus.coders.Message;
import org.apache.log4j.Logger;
import java.util.Properties;
import java.io.UnsupportedEncodingException;


/**
 * 转换Kafka中的Payload为CSV String（即原来就是String）
 *
 * - MessageDecoder class that will convert the payload into a String object,
 * - System.currentTimeMillis() will be used to set CamusWrapper's
 * timestamp property

 * @todo System.currentTimeMillis的返回如何划分partitions
 * @todo 为何使用byte[]，而不是Message message?
 */

public class CSVStringMessageDecoder extends MessageDecoder<Message, String> {
    private static final Logger log = Logger.getLogger(CSVStringMessageDecoder.class);

    @Override
    public void init(Properties props, String topicName) {
        this.props = props;
        this.topicName = topicName;
    }

    @Override
    public CamusWrapper<String> decode(Message message) {
        long timestamp = 0;
        String payloadString;

        try {
            payloadString = new String(message.getPayload(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Unable to load UTF-8 encoding, falling back to system default", e);
            payloadString = new String(message.getPayload());
        }


        timestamp = System.currentTimeMillis();

        return new CamusWrapper<String>(payloadString, timestamp);
    }
}     

