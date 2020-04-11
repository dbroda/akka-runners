package codeplus.kata.springakkarunner.replays;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.springframework.stereotype.Component;

@Component
public class StudioComponent {

    private static final ActorSystem actorSystem = ActorSystem
        .create(StudioMainActor.create(), "replays-studio");

    public void startReplay(StartReplay startReplayCommand) {
        actorSystem.tell(startReplayCommand);

    }

    private static class StudioMainActor extends AbstractBehavior<StartReplay> {


        public static Behavior<StartReplay> create() {
            return Behaviors.setup(StudioMainActor::new);
        }

        private StudioMainActor(ActorContext<StartReplay> context) {
            super(context);
        }

        @Override
        public Receive<StartReplay> createReceive() {
            return newReceiveBuilder().onMessage(StartReplay.class, this::onStartReplay).build();
        }

        private Behavior<StartReplay> onStartReplay(StartReplay command) {
            var replayActor = getContext()
                .spawn(ReplayActor.create(), "replay-actor-" + command.getReplayID());

            replayActor.tell(command);
            return this;
        }
    }
}
