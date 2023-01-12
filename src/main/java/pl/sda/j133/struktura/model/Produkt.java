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
public abstract class Produkt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String nazwa;

    public abstract Set<Wypozyczenie> getWypozyczenia();
    public abstract boolean sprawdzDostepnosc();

    protected boolean sprawdzCzyWypozyczone(){
        Set<Wypozyczenie> wypozyczenia = getWypozyczenia();
        for (Wypozyczenie wypozyczenie : wypozyczenia) {
            if (wypozyczenie.getZwrot() == null) {
                return true;
            }
        }
        return false;
    }
}
