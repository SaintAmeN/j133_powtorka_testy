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
public class Zestaw extends Produkt {

    @OneToMany
    private Set<Wypozyczenie> wypozyczenia;

    @OneToMany
    private Set<Produkt> produkty;

    @Override
    public boolean sprawdzDostepnosc() {
        return !sprawdzCzyWypozyczone();
    }
}
