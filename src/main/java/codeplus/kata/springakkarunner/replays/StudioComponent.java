package codeplus.kata.springakkarunner.replays;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.internal.PoisonPill;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudioComponent {

    private static final ActorSystem actorSystem = ActorSystem
        .create(StudioMainActor.create(), "replays-studio");

    public void startReplay(StartReplay startReplayCommand) {
        actorSystem.tell(startReplayCommand);

    }

    public void cancelReplay(CancelReplay cancelReplayCommand) {
        actorSystem.tell(cancelReplayCommand);
    }

    private static class StudioMainActor extends AbstractBehavior<Command> {


        public static Behavior<Command> create() {
            return Behaviors.setup(StudioMainActor::new);
        }

        private StudioMainActor(ActorContext<Command> context) {
            super(context);
        }

        @Override
        public Receive<Command> createReceive() {
            return newReceiveBuilder()
                .onMessage(StartReplay.class, this::onStartReplay)
                .onMessage(CancelReplay.class, this::onCancelReplay)
                .build();
        }

        private Behavior<Command> onCancelReplay(CancelReplay cancelReplay) {
            log.info("Canceling replay {}", cancelReplay);
//            akka://replays-studio/user/

            getContext().getChild("replay-actor-" + cancelReplay.getReplayId())
                .ifPresent(ref -> {
                    ref.unsafeUpcast().tell(cancelReplay);
                });
            return this;
        }

        private Behavior<Command> onStartReplay(StartReplay command) {
            var replayActor = getContext()
                .spawn(ReplayActor.create(), "replay-actor-" + command.getReplayID());

            replayActor.tell(command);
            return this;
        }
    }
}
