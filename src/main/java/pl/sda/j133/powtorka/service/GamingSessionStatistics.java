package pl.sda.j133.powtorka.service;

import pl.sda.j133.powtorka.model.User;
import pl.sda.j133.powtorka.model.structs.GamingSessionTime;

import java.time.LocalDateTime;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 07.01.2023
 */
public interface GamingSessionStatistics {

    /**
     * Calculates average session time for given game in seconds.
     * @param user - user with gaming sessions
     * @param gameId - game to calculate session time.
     * @return average session time in seconds
     */
    public int calculateAverageSessionTimeForGivenGameInSeconds(User user, String gameId);

    /**
     * Calculates average session time for all games in seconds.
     * @param user - user with gaming sessions
     * @return average session time in seconds
     */
    public int calculateAverageSessionTimeInTotalInSeconds(User user);

    public int calculateAverageSessionTimeInLast7DaysSeconds(User user);

    public LocalDateTime findLastSessionDateTime(User user);

    public GamingSessionTime findLastSessionTimes(User user);
}
