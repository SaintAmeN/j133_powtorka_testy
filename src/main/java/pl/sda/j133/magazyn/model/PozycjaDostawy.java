package pl.sda.j133.magazyn.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 14.01.2023
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PozycjaDostawy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ilosc;
    private double cena;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Produkt produkt;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Dostawa dostawa;
}
