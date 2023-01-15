package pl.sda.j133.struktura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 12.01.2023
 */
@Data
@Entity
public class Buty extends Produkt {

    @OneToOne
    private Zestaw zestaw; // jeśli nie jest w zestawie to zestaw == null

    @OneToMany
    private Set<Wypozyczenie> wypozyczenia;

    public Buty() {
    }

    public Buty(Long id, String nazwa) {
        super(id, nazwa);
    }

    public Buty(Long id, String nazwa, Zestaw zestaw, Set<Wypozyczenie> wypozyczenia) {
        super(id, nazwa);
        this.zestaw = zestaw;
        this.wypozyczenia = wypozyczenia;
    }

    @Override
    public boolean sprawdzDostepnosc() {
        if (getZestaw() != null) {
//            log.info("Nie można wynająć, buty są częścią zestawu.");
            return false;
        }

        return !sprawdzCzyWypozyczone();
    }
}
