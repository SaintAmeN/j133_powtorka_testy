package pl.sda.j133.powtorka.model.structs;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 08.01.2023
 */
@Data
public class GamingSessionTime {
    private final LocalDateTime timeStarted;
    private final LocalDateTime timeFinished;
    private final long duration;

    public GamingSessionTime(LocalDateTime timeStarted, LocalDateTime timeFinished, long duration) {
        this.timeStarted = timeStarted;
        this.timeFinished = timeFinished;
        this.duration = duration;
    }
}
