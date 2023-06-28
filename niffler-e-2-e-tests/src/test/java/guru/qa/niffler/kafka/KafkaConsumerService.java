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
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class KafkaConsumerService implements Runnable {

    private static final Logger LOG = Logger.getLogger(KafkaConsumerService.class.getName());
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

    public KafkaConsumerService() {
        this(CFG.kafkaTopics());
    }

    public KafkaConsumerService(List<String> stringTopics) {
        stringConsumer = new KafkaConsumer<>(STR_KAFKA_PROPERTIES);
        stringConsumer.subscribe(stringTopics);
    }

    public KafkaConsumerService withReadTimeout(long maxReadTimeout) {
        MAX_READ_TIMEOUT = maxReadTimeout;
        return this;
    }

    public KafkaConsumerService withFileLogger(String pathToLogFile) {
        if (pathToLogFile.endsWith("/")) {
            pathToLogFile = pathToLogFile.substring(0, pathToLogFile.length() - 1);
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
            Handler fh = new FileHandler(pathToLogFile + "/KAFKA_MyLogFile_"
                    + format.format(Calendar.getInstance().getTime()) + ".log");
            fh.setFormatter(new SimpleFormatter());
            LOG.addHandler(fh);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void shutdown() {
        running.set(false);
    }

    public static Map<String, UserJson> getMessages() {
        return MESSAGES.getAsMap();
    }

    @Nullable
    public static UserJson getMessage(String username) {
        return getMessage(username, MAX_READ_TIMEOUT);
    }

    @Nullable
    public static UserJson getMessage(String username, long timeoutMs) {
        return MESSAGES.wait(username, timeoutMs);
    }

    @Nonnull
    public static UserJson getRequiredMessage(String username) {
        return getRequiredMessage(username, MAX_READ_TIMEOUT);
    }

    @Nonnull
    public static UserJson getRequiredMessage(String username, long timeoutMs) {
        return Objects.requireNonNull(getMessage(username, timeoutMs));
    }

    @Override
    public void run() {
        try {
            LOG.info("### Consumer subscribed... " + Arrays.toString(stringConsumer.subscription().toArray()) + "###");
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
                    LOG.warning("### Commit failed: " + e.getMessage());
                }
            }
        } finally {
            LOG.info("### Close consumer ###");
            stringConsumer.close();
            LogManager.getLogManager().reset();
            Thread.currentThread().interrupt();
        }
    }

    private void deserializeRecord(String recordValue) {
        try {
            UserJson userJson = OM.readValue(recordValue, UserJson.class);

            if (userJson == null || userJson.getUsername() == null) {
                LOG.info("### Empty username in message ###");
                return;
            }

            MESSAGES.provide(userJson.getUsername(), userJson);
        } catch (JsonProcessingException e) {
            LOG.warning("### Parse message fail: " + e.getMessage());
        }
    }

    private void logRecord(ConsumerRecord<String, String> record) {
        LOG.info(String.format("topic = %s, \npartition = %d, \noffset = %d, \nkey = %s, \nvalue = %s\n\n",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                record.value()));
    }
}