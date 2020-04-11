package codeplus.kata.springakkarunner.replays;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ReplayActor extends AbstractBehavior<StartReplay> {

    public ReplayActor(
        ActorContext<StartReplay> context) {
        super(context);
    }

    public static Behavior<StartReplay> create() {
        return Behaviors.setup(ReplayActor::new);
    }


    @Override
    public Receive<StartReplay> createReceive() {
        return newReceiveBuilder().onMessage(StartReplay.class, this::onStartReplay).build();

    }

    private Behavior<StartReplay> onStartReplay(StartReplay startReplayCommand) {
        log.info("starting replay {} {}", this, startReplayCommand);
        final long timeOffsetInMillis = ChronoUnit.MILLIS
            .between(LocalDateTime.now(), startReplayCommand.getEventStartedAt());

        return Behaviors.empty();
    }
}
