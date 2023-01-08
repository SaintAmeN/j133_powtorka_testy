package pl.sda.j133.powtorka.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.sda.j133.powtorka.model.GamingSession;
import pl.sda.j133.powtorka.model.User;
import pl.sda.j133.powtorka.model.structs.GamingSessionTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 07.01.2023
 * <p>
 * Liczenie statystyki czasu:
 * - średni czas gry
 * - najdłuższy czas gry
 * - najkrótszy czas gry
 * - średni czas gry z ostatnich 7 dni
 * - data i czas (rozpoczęcia) ostatniej sesji
 * - data i czas (rozpoczęcia) ostatniej sesji (* + długość sesji)
 * <p>
 * Liczenie statystyki rozegranych meczy:
 * - średnia ilość rozegranych meczy
 * - średnie ratio zwycięstw do porażek (32/16) 2:1 (2.0) ///  8/16 = 0.5
 * - okres 7 dni
 * - okres od początku (wszystkie)
 * - per sesja (oblicz jaka jest statystyka z jednej wybranej sesji)
 * - wyznacz ranking top3
 */
class GamingSessionStatisticsServiceTest {

    private final GamingSessionStatistics gamingSessionStatistics = new GamingSessionStatisticsService();

    @Test
    @DisplayName("Oblicza średni czas gry per wybrana gra")
    public void test_calculateAverageSessionTimeForGivenGame() {
        // PREPARATION
        User testUser = new User("irrelevant", "irrelevant");
        testUser.setGamingSessions(
                new HashSet<>(
                        List.of(
                                new GamingSession(
                                        LocalDateTime.of(2000, 1, 1, 3, 0, 0),
                                        LocalDateTime.of(2000, 1, 1, 4, 30, 0),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "fifa-01"),
                                new GamingSession(
                                        LocalDateTime.of(2000, 1, 1, 4, 55, 0),
                                        LocalDateTime.of(2000, 1, 1, 5, 40, 30),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "fifa-01"),
                                new GamingSession(
                                        LocalDateTime.of(2000, 1, 1, 6, 20, 0),
                                        LocalDateTime.of(2000, 1, 1, 8, 20, 45),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "metin-01")
                        )
                )
        );

        // TESTING
        int result = gamingSessionStatistics.calculateAverageSessionTimeForGivenGameInSeconds(testUser, "fifa-01");
        assertEquals(4065, result); // session1 = 90 min + session2 = 45 min + 30 seconds / 135 * 60 = 8100
    }

    @Test
    @DisplayName("Oblicza średni czas gry ze wszystkich sesji")
    public void test_calculateAverageSessionTimeTotal() {
        // PREPARATION
        User testUser = new User("irrelevant", "irrelevant");
        testUser.setGamingSessions(
                new HashSet<>(
                        List.of(
                                new GamingSession(
                                        LocalDateTime.of(2000, 1, 1, 3, 0, 0),
                                        LocalDateTime.of(2000, 1, 1, 4, 30, 0),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "fifa-01"),
                                new GamingSession(
                                        LocalDateTime.of(2000, 1, 1, 4, 55, 0),
                                        LocalDateTime.of(2000, 1, 1, 5, 40, 30),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "fifa-01"),
                                new GamingSession(
                                        LocalDateTime.of(2000, 1, 1, 6, 20, 0),
                                        LocalDateTime.of(2000, 1, 1, 8, 20, 45),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "metin-01")
                        )
                )
        );

        // TESTING
        int result = gamingSessionStatistics.calculateAverageSessionTimeInTotalInSeconds(testUser);
        assertEquals(5125, result); // 90 min + 45 min + 120 min = 255 * 60 = 15300 + 30+45 / 3 = 5125
    }

    @Test
    @DisplayName("Oblicza średni czas gry z sesji rozegranych podczas ostatnich 7 dni")
    public void test_calculateAverageSessionTimeFromSessionsInLast7Days() {
        // PREPARATION
        User testUser = new User("irrelevant", "irrelevant");

        long totalTimeValid = 0;
        final long numberOfSessions = 10;
        for (int i = 0; i < numberOfSessions; i++) {
            GamingSession generatedSession = createRandomGamingSessionBetweenDates(
                    LocalDateTime.now().minusDays(7).plusHours(1),
                    LocalDateTime.now(),
                    10800,
                    List.of("fifa-01", "cs-1.6-01", "metin-01"));
            totalTimeValid += generatedSession.getSessionDuration();

            testUser.getGamingSessions().add(generatedSession);
        }

        for (int i = 0; i < numberOfSessions; i++) {
            GamingSession generatedSession = createRandomGamingSessionBetweenDates(
                    LocalDateTime.now().minusDays(365),
                    LocalDateTime.now().minusDays(7).minusSeconds(1),
                    10800,
                    List.of("fifa-01", "cs-1.6-01", "metin-01"));
            testUser.getGamingSessions().add(generatedSession);
        }

        // TESTING
        int result = gamingSessionStatistics.calculateAverageSessionTimeInLast7DaysSeconds(testUser);
        assertEquals(totalTimeValid/numberOfSessions, result);
    }

    @Test
    @DisplayName("Znajduje datę i czas ostatniej sesji")
    public void test_findLastSession() {
        // PREPARATION
        final LocalDateTime expectedResult = LocalDateTime.of(2020, 2, 1, 4, 55, 0);
        User testUser = new User("irrelevant", "irrelevant");
        testUser.setGamingSessions(
                new HashSet<>(
                        List.of(
                                new GamingSession(
                                        LocalDateTime.of(2000, 1, 1, 3, 0, 0),
                                        LocalDateTime.of(2000, 1, 1, 4, 30, 0),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "fifa-01"),
                                new GamingSession(
                                        expectedResult,
                                        LocalDateTime.of(2020, 2, 2, 10, 40, 30),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "fifa-01"),
                                new GamingSession(
                                        LocalDateTime.of(2020, 1, 1, 6, 20, 0),
                                        LocalDateTime.of(2020, 1, 1, 8, 20, 45),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "metin-01")
                        )
                )
        );


        // TESTING
        LocalDateTime result = gamingSessionStatistics.findLastSessionDateTime(testUser);
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Znajduje datę i czas i długość ostatniej sesji")
    public void test_findLastSessionTimes() {
        // PREPARATION
        final LocalDateTime timeStart = LocalDateTime.of(2020, 2, 1, 4, 55, 0);
        final LocalDateTime timeEnd = LocalDateTime.of(2020, 2, 2, 10, 40, 30);
        final long expectedResultDuration = Duration.between(timeStart, timeEnd).getSeconds();

        final GamingSessionTime expectedResult = new GamingSessionTime(timeStart, timeEnd, expectedResultDuration);

        User testUser = new User("irrelevant", "irrelevant");
        testUser.setGamingSessions(
                new HashSet<>(
                        List.of(
                                new GamingSession(
                                        LocalDateTime.of(2000, 1, 1, 3, 0, 0),
                                        LocalDateTime.of(2000, 1, 1, 4, 30, 0),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "fifa-01"),
                                new GamingSession(
                                        timeStart,
                                        timeEnd,
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "fifa-01"),
                                new GamingSession(
                                        LocalDateTime.of(2020, 1, 1, 6, 20, 0),
                                        LocalDateTime.of(2020, 1, 1, 8, 20, 45),
                                        0, // irrelevant
                                        0, // irrelevant
                                        0, // irrelevant
                                        "metin-01")
                        )
                )
        );

        // TESTING
        GamingSessionTime result = gamingSessionStatistics.findLastSessionTimes(testUser);
        assertEquals(expectedResult, result);
    }

    private GamingSession createRandomGamingSessionBetweenDates(
            LocalDateTime początekZakresuZKtóregoLosujemyPoczątekSesji,
            LocalDateTime koniecZakresuZKtóregoLosujemyPoczątekSesji,
            long maxSessionLength,
            List<String> gameNames) {
        Duration duration = Duration.between(początekZakresuZKtóregoLosujemyPoczątekSesji, koniecZakresuZKtóregoLosujemyPoczątekSesji);
        Long seconds = duration.getSeconds();

        Random generator = new Random();
        Long randomSecond = generator.nextLong(seconds);

        LocalDateTime sessionStart = początekZakresuZKtóregoLosujemyPoczątekSesji.plusSeconds(randomSecond);
        LocalDateTime sessionFinish = sessionStart.plusSeconds(generator.nextLong(maxSessionLength));

        return new GamingSession(
                sessionStart,
                sessionFinish,
                0,
                0,
                0,
                gameNames.get(generator.nextInt(gameNames.size())));
    }
}