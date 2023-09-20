package com.example;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.util.stream.Stream;

@ApplicationScoped
public class MyReactiveMessagingApplication {

    /**
     * Consume the message from the "words-in" channel, uppercase it and send it to the uppercase channel.
     * Messages come from the broker.
     **/
    @Incoming("words-in")
    @Outgoing("uppercase-out")
    public String toUpperCase(String message) {
        System.out.println("WORDS-IN >> " + message);
        return message.toUpperCase();
    }

    /**
     * Consume the uppercase channel (Kafka) and print the messages.
     **/
    @Incoming("uppercase-in")
    public void sink(String word) {
        System.out.println("UPPERCASE-IN >> " + word);
    }
}
