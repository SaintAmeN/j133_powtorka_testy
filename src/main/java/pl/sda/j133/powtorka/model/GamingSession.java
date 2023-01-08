package pl.sda.j133.powtorka.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 07.01.2023
 */
@Data
@AllArgsConstructor
public class GamingSession {
    private LocalDateTime timeStarted;
    private LocalDateTime timeFinished;

    private int matchesPlayed;  // ile meczy rozegrał użytkownik

    private int matchesWon;     // ile meczy wygrał
    private int matchesLost;    // ile meczy przegrał

    private String gameIdentifier;  // identyfikator gry do jej rozróżnienia.
                                    // fifa-01
                                    // cs-1.6-01

    public long getSessionDuration() {
        return Duration.between(timeStarted, timeFinished).getSeconds();
    }
}
