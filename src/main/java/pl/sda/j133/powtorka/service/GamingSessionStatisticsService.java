package pl.sda.j133.powtorka.service;

import pl.sda.j133.powtorka.model.GamingSession;
import pl.sda.j133.powtorka.model.User;
import pl.sda.j133.powtorka.model.structs.GamingSessionTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 07.01.2023
 */
public class GamingSessionStatisticsService implements GamingSessionStatistics {

    @Override
    public int calculateAverageSessionTimeForGivenGameInSeconds(User user, String gameId) {
        List<GamingSession> sessions = user.getGamingSessions()
                .stream()
                .filter(gamingSession -> gamingSession.getGameIdentifier().equals(gameId))
                .toList();

        OptionalDouble averageTime = sessions.stream()
                .mapToLong(session -> Duration.between(session.getTimeStarted(), session.getTimeFinished()).getSeconds())
                .average();

        if (averageTime.isEmpty()) {
            throw new IllegalArgumentException("No average time, probably no sessions found");
        }

        return (int) averageTime.getAsDouble();
    }

    @Override
    public int calculateAverageSessionTimeInTotalInSeconds(User user) {
        OptionalDouble averageTime = user.getGamingSessions().stream()
                .mapToLong(session -> Duration.between(session.getTimeStarted(), session.getTimeFinished()).getSeconds())
                .average();

        if (averageTime.isEmpty()) {
            throw new IllegalArgumentException("No average time, probably no sessions found");
        }

        return (int) averageTime.getAsDouble();
    }

    @Override
    public int calculateAverageSessionTimeInLast7DaysSeconds(User user) {
        OptionalDouble averageTime = user.getGamingSessions().stream()
                .filter(gamingSession -> gamingSession.getTimeStarted().isAfter(LocalDateTime.now().minusDays(7)))
                .mapToLong(session -> Duration.between(session.getTimeStarted(), session.getTimeFinished()).getSeconds())
                .average();

        if (averageTime.isEmpty()) {
            throw new IllegalArgumentException("No average time, probably no sessions found");
        }

        return (int) averageTime.getAsDouble();
    }

    @Override
    public LocalDateTime findLastSessionDateTime(User user) {
//        GamingSession gamingSession = user.getGamingSessions().stream()
//                .max(Comparator.comparing(GamingSession::getTimeStarted))
//                .orElseThrow(() -> new IllegalArgumentException("No session found."));

        Optional<GamingSession> gamingSessionOptional = user.getGamingSessions().stream()
                .max(Comparator.comparing(GamingSession::getTimeStarted));

        if (gamingSessionOptional.isEmpty()) {
            throw new IllegalArgumentException("No session found.");
        }

        return gamingSessionOptional.get().getTimeStarted();
    }

    @Override
    public GamingSessionTime findLastSessionTimes(User user) {
        Optional<GamingSession> gamingSessionOptional = user.getGamingSessions().stream()
                .max(Comparator.comparing(GamingSession::getTimeStarted));

        if (gamingSessionOptional.isEmpty()) {
            throw new IllegalArgumentException("No session found.");
        }

        GamingSession gamingSession = gamingSessionOptional.get();
        return new GamingSessionTime(
                gamingSession.getTimeStarted(),
                gamingSession.getTimeFinished(),
                Duration.between(gamingSession.getTimeStarted(), gamingSession.getTimeFinished()).getSeconds()
        );
    }
}
