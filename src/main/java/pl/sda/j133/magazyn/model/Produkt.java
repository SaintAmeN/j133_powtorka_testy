package pl.sda.j133.magazyn.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 14.01.2023
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Produkt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nazwa;

    @OneToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PozycjaSprzedazy> sprzedaze;

    @OneToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PozycjaDostawy> dostawy;
}
