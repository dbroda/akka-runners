package codeplus.kata.springakkarunner.replays;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
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
            .onMessage(CancelReplay.class, this::onCancelReplay)
            .onSignal(PostStop.class, signal -> onPostStop())
            .build();
    }

    private Behavior<Command> onPostStop() {
        log.warn("Stopping by onPostStop replay processing!");
        return this;
    }

    private Behavior<Command> onCancelReplay(CancelReplay cancelReplay) {
        log.warn("Stopping by canceling replay processing! {}", cancelReplay);
        return Behaviors.stopped();
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
