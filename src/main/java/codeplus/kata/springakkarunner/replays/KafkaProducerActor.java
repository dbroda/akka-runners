package codeplus.kata.springakkarunner.replays;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class KafkaProducerActor extends AbstractBehavior<Command> {

    public KafkaProducerActor(
        ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(KafkaProducerActor::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder().onMessage(SendToKafka.class, this::onSendToKafka)
            .build();
    }

    private Behavior<Command> onSendToKafka(SendToKafka sendToKafka) {

        log.info("Sending to kafka message actor {}, ref {},  {}", this, getContext().getSelf(),
            sendToKafka);
        return this;
    }

    @Value
    public static class SendToKafka implements Command {

        String message;
    }
}
