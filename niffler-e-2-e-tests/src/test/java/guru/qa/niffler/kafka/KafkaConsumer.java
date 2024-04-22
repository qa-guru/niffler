package guru.qa.niffler.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.utils.WaitForOne;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;


public class KafkaConsumer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);
    private static final Config CFG = Config.getConfig();
    private static final WaitForOne<String, UserJson> MESSAGES = new WaitForOne<>();
    private static final ObjectMapper OM = new ObjectMapper();

    private static final Properties STR_KAFKA_PROPERTIES = new Properties();
    private static long MAX_READ_TIMEOUT = 5000L;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Consumer<String, String> stringConsumer;

    static {
        STR_KAFKA_PROPERTIES.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, CFG.kafkaAddress());
        STR_KAFKA_PROPERTIES.put(ConsumerConfig.GROUP_ID_CONFIG, "stringKafkaStringConsumerService");
        STR_KAFKA_PROPERTIES.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        STR_KAFKA_PROPERTIES.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        STR_KAFKA_PROPERTIES.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    public KafkaConsumer() {
        this(CFG.kafkaTopics());
    }

    public KafkaConsumer(@Nonnull List<String> stringTopics) {
        stringConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(STR_KAFKA_PROPERTIES);
        stringConsumer.subscribe(stringTopics);
    }

    public KafkaConsumer withReadTimeout(long maxReadTimeout) {
        MAX_READ_TIMEOUT = maxReadTimeout;
        return this;
    }

    public void shutdown() {
        running.set(false);
    }

    @Nonnull
    public static Map<String, UserJson> getMessages() {
        return MESSAGES.getAsMap();
    }

    @Nullable
    public static UserJson getMessage(@Nonnull String username) {
        return getMessage(username, MAX_READ_TIMEOUT);
    }

    @Nullable
    public static UserJson getMessage(@Nonnull String username, long timeoutMs) {
        return MESSAGES.wait(username, timeoutMs);
    }

    @Nonnull
    public static UserJson getRequiredMessage(@Nonnull String username) {
        return getRequiredMessage(username, MAX_READ_TIMEOUT);
    }

    @Nonnull
    public static UserJson getRequiredMessage(@Nonnull String username, long timeoutMs) {
        return Objects.requireNonNull(getMessage(username, timeoutMs));
    }

    @Override
    public void run() {
        try {
            LOG.info("### Consumer subscribed... {}###", Arrays.toString(stringConsumer.subscription().toArray()));
            running.set(true);
            while (running.get()) {
                ConsumerRecords<String, String> strRecords = stringConsumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> record : strRecords) {
                    logRecord(record);
                    deserializeRecord(record.value());
                }
                try {
                    stringConsumer.commitSync();
                } catch (CommitFailedException e) {
                    LOG.error("### Commit failed", e);
                }
            }
        } finally {
            LOG.debug("### Close consumer ###");
            stringConsumer.close();
            Thread.currentThread().interrupt();
        }
    }

    private void deserializeRecord(@Nonnull String recordValue) {
        try {
            UserJson userJson = Objects.requireNonNull(
                    OM.readValue(recordValue, UserJson.class)
            );
            MESSAGES.provide(userJson.username(), userJson);
        } catch (JsonProcessingException e) {
            LOG.error("### Parse message fail", e);
        }
    }

    private void logRecord(@Nonnull ConsumerRecord<String, String> record) {
        LOG.debug(String.format("topic = %s, \npartition = %d, \noffset = %d, \nkey = %s, \nvalue = %s\n\n",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                record.value()));
    }
}