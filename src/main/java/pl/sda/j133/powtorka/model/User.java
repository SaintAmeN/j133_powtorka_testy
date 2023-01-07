package pl.sda.j133.powtorka.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 07.01.2023
 */
@Getter
@Setter
@ToString
public class User {
    @EqualsAndHashCode.Include
    private final String id;          // 030303  040404

    private String username;

    @ToString.Exclude
    private String password;

    private Set<GamingSession> gamingSessions = new HashSet<>();

    public User(String username, String password) {
        this.id = UUID.randomUUID().toString(); // Przykład UUID: f81d4fae-7dec-11d0-a765-00a0c91e6bf6
        this.username = username;
        this.password = password;
    }

//
//    public static void main(String[] args) {
//        String pesel1 = "030303";
//        String pesel2 = "040404";
//
//        System.out.println(pesel1.equals(pesel2));
//
//        User user1 = new User("a", "b");
//        User user2 = new User("c", "d");
//
//        System.out.println(user1.equals(user2));
//    }
}
