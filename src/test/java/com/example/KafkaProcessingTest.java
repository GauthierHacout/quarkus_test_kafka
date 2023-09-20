package com.example;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.kafka.companion.ConsumerTask;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
public class KafkaProcessingTest {

    @InjectKafkaCompanion
    KafkaCompanion companion;

    @Test
    void testKafkaMessageProcessing() {

        companion.produceStrings().fromRecords(
                new ProducerRecord<>("words", "A", "Gauthier"),
                new ProducerRecord<>("words", "B", "Boris"));

        System.out.println("Waiting");

        ConsumerTask<String, String> kafkaRecords = companion.consumeStrings().fromTopics("uppercase", 2);
        kafkaRecords.awaitCompletion();

        kafkaRecords.stream().forEach(r -> System.out.println("Received record inside test: "+ r.value()));
        Assertions.assertEquals(2, kafkaRecords.count());
    }
}
