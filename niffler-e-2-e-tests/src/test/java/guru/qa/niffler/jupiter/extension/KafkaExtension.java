package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.kafka.KafkaConsumerService;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaExtension implements AroundAllTestsExtension {

    private static final KafkaConsumerService kafkaConsumerService = new KafkaConsumerService();
    private static final ExecutorService es = Executors.newSingleThreadExecutor();

    @Override
    public void beforeAllTests(ExtensionContext context) {
        es.execute(new KafkaConsumerService());
        es.shutdown();
    }

    @Override
    public void afterAllTests() {
        kafkaConsumerService.shutdown();
    }
}
