package pl.sda.j133.struktura.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 12.01.2023
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Zestaw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nazwa;

    @OneToOne
    private Narty narty;

    @OneToOne
    private Buty buty;

    @OneToMany
    private Set<Wypozyczenie> wypozyczenia;
}
