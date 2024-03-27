package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.kafka.KafkaConsumer;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaExtension implements SuiteExtension {

    private static final KafkaConsumer KAFKA_CONSUMER = new KafkaConsumer();
    private static final ExecutorService es = Executors.newSingleThreadExecutor();

    @Override
    public void beforeAllTests(ExtensionContext context) {
        es.execute(KAFKA_CONSUMER);
        es.shutdown();
    }

    @Override
    public void afterAllTests() {
        KAFKA_CONSUMER.shutdown();
    }
}
