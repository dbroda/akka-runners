package codeplus.kata.springakkarunner.replays;

import lombok.Value;
import lombok.With;

@Value
@With
public class CancelReplay implements Command {

    private Long replayId;
}
