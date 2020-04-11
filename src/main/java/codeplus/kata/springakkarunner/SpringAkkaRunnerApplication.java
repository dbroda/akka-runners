package codeplus.kata.springakkarunner;

import codeplus.kata.springakkarunner.replays.StartReplay;
import codeplus.kata.springakkarunner.replays.StudioComponent;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@Slf4j
public class SpringAkkaRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAkkaRunnerApplication.class, args);
    }

    @Autowired
    private StudioComponent studioComponent;

    @EventListener(classes = ApplicationReadyEvent.class)
    public void afterStart() {


        studioComponent.startReplay(buildEvent(1L));
//        studioComponent.startReplay(buildEvent(2L));
//        studioComponent.startReplay(buildEvent(3L));
//        studioComponent.startReplay(buildEvent(4L));
    }

    private StartReplay buildEvent(Long id) {
        final long replayID = 1000000L + id;
        return StartReplay.builder().eventID(id)
            .eventStartedAt(LocalDateTime.of(2020, 4, 5, 10, 45, 0))
            .minutesDelay(0)
            .replayID(replayID)
            .speedRatio(2)
            .replayEvents(
                List.of(
                    StartReplay.ReplayEvent.builder().eventType("event").id(replayID).messageToSend("event"+replayID+"-0")
                        .originallyExecutedAt(LocalDateTime.of(2020, 4, 5, 10, 45, 10))
                        .build()
                    ,
                    StartReplay.ReplayEvent.builder().eventType("event").id(replayID).messageToSend("event"+replayID+"-1")
                        .originallyExecutedAt(LocalDateTime.of(2020, 4, 5, 10, 45, 30))
                        .build(),
                    StartReplay.ReplayEvent.builder().eventType("event").id(replayID).messageToSend("event"+replayID+"-2")
                        .originallyExecutedAt(LocalDateTime.of(2020, 4, 5, 10, 45, 33))
                        .build(),
                    StartReplay.ReplayEvent.builder().eventType("event").id(replayID).messageToSend("event"+replayID+"-3")
                        .originallyExecutedAt(LocalDateTime.of(2020, 4, 5, 10, 46, 0))
                        .build()

                )
            )
            .build();
    }

}
