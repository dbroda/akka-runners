package codeplus.kata.springakkarunner;

import codeplus.kata.springakkarunner.replays.CancelReplay;
import codeplus.kata.springakkarunner.replays.StudioComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
class ReplaysController {

    @Autowired
    StudioComponent studioComponent;

    @GetMapping("/replays/cancel/{replayId}")
    public void cancel(@PathVariable Long replayId) {

        log.info("Cancelling replay {}", replayId);
        studioComponent.cancelReplay(new CancelReplay(replayId));
    }
}
