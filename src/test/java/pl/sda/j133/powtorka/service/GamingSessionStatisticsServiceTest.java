package pl.sda.j133.powtorka.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.sda.j133.powtorka.model.GamingSession;
import pl.sda.j133.powtorka.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

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
 * - data i czas ostatniej sesji
 * <p>
 * Liczenie statystyki rozegranych meczy:
 * - średnia ilość rozegranych meczy
 * - średnie ratio zwycięstw do porażek
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
        assertEquals(5100, result); // 90 min + 45 min + 120 min = 255 * 60 = 15300 / 3 = 5100
    }
}