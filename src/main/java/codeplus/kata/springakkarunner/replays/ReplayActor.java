package codeplus.kata.springakkarunner.replays;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import codeplus.kata.springakkarunner.replays.KafkaProducerActor.SendToKafka;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ReplayActor extends AbstractBehavior<Command> {

    private ActorRef<Command> kafkaActorRef;

    @Builder
    @Value
    public static class ExecuteReplayEvent implements Command {

        String event;
        LocalDateTime executeAt;
    }

    public ReplayActor(
        ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(ReplayActor::new);
    }


    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder().onMessage(StartReplay.class, this::onStartReplay)
            .onMessage(ExecuteReplayEvent.class, this::onReplayEvent)
            .onMessage(CancelReplay.class, this::onCancelReplay)
            .build();

    }

    private Behavior<Command> onCancelReplay(CancelReplay cancelReplay) {
//        if(kafkaActorRef != null) {
//            kafkaActorRef.unsafeUpcast().tell(PoisonPill.instance());
//        }
        return Behaviors.stopped();
    }

    private Behavior<Command> onReplayEvent(ExecuteReplayEvent executeReplayEvent) {
        log.info("{}, {}, Executing event {}", this, this.getContext().getSelf(),
            executeReplayEvent);
        return this;
    }

    private Behavior<Command> onStartReplay(StartReplay startReplayCommand) {
        log.info("starting replay {} {}", this, startReplayCommand);

        final LocalDateTime now = LocalDateTime.now();
        final long timeOffsetInMillis = ChronoUnit.MILLIS
            .between(startReplayCommand.getEventStartedAt(), now);

        final LocalDateTime localDateTime = ChronoUnit.MILLIS
            .addTo(startReplayCommand.getEventStartedAt(), timeOffsetInMillis);

         kafkaActorRef = getContext().spawn(KafkaProducerActor.create(),
            "kafka-producer-" + startReplayCommand.getReplayID(),
            DispatcherSelector.blocking());
//            DispatcherSelector.fromConfig("kafka-producer-dispatcher"));

//        getContext().watch(kafkaActorRef);

        final List<ExecuteReplayEvent> eventsToReplay = startReplayCommand.getReplayEvents()
            .stream()
            .map(x -> ExecuteReplayEvent.builder()
                .event(x.getMessageToSend())
                .executeAt(ChronoUnit.MILLIS.addTo(
                    x.getOriginallyExecutedAt(), timeOffsetInMillis
                ))
                .build()
            ).collect(Collectors.toList());

        log.info("starting replay {} {}", this, eventsToReplay);

        eventsToReplay.stream()
            .forEach(e ->
                {
                    final Duration delay = Duration.between(now, e.executeAt);
                    final Duration finalDelay = delay.dividedBy(startReplayCommand.getSpeedRatio());
                    log.info("actor {}, ref {}, delay {}, finalDelay {}", this,
                        this.getContext().getSelf(), delay, finalDelay);
                    getContext().scheduleOnce(finalDelay, kafkaActorRef, new SendToKafka(e.getEvent()));


                }
            );

        return this;
    }
}
