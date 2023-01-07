package pl.sda.j133.powtorka.service;

import pl.sda.j133.powtorka.model.GamingSession;
import pl.sda.j133.powtorka.model.User;

import java.time.Duration;
import java.util.List;
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

        if (averageTime.isEmpty()){
            throw new IllegalArgumentException("No average time, probably no sessions found");
        }

        return (int) averageTime.getAsDouble();
    }

    @Override
    public int calculateAverageSessionTimeInTotalInSeconds(User user) {
        return 0;
    }
}
