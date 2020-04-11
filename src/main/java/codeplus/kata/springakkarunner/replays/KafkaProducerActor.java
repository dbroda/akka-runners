package codeplus.kata.springakkarunner.replays;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class KafkaProducerActor extends AbstractBehavior<KafkaProducerActor.SendToKafka> {

    public KafkaProducerActor(
        ActorContext<SendToKafka> context) {
        super(context);
    }

    public Behavior<KafkaProducerActor.SendToKafka> create() {
        return Behaviors.setup(KafkaProducerActor::new);
    }

    @Override
    public Receive<SendToKafka> createReceive() {
        return newReceiveBuilder().onMessage(SendToKafka.class, this::onSendToKafka).build();
    }

    private Behavior<SendToKafka> onSendToKafka(SendToKafka sendToKafka) {


        return this;
    }

    @Value
    public class SendToKafka {

        String message;
    }
}
