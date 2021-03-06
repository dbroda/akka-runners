package codeplus.kata.springakkarunner.replays;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class StartReplay implements Command {

    private final UUID correlationID = UUID.randomUUID();
    private Long eventID;
    private Long replayID;
    private LocalDateTime eventStartedAt;
    private int speedRatio;
    private int minutesDelay;
    private List<ReplayEvent> replayEvents;

    @Builder(toBuilder = true)
    @Value
    public static class ReplayEvent {

        private String eventType;
        private Long id;
        private LocalDateTime originallyExecutedAt;
        private String messageToSend;
    }



}
