package pl.sda.j133.struktura.service;

import lombok.extern.java.Log;
import pl.sda.j133.struktura.model.Buty;
import pl.sda.j133.struktura.model.UmowaWypozyczenia;
import pl.sda.j133.struktura.model.Wypozyczenie;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 12.01.2023
 */
@Log
public class UmowaService {
    private WypozyczenieService wypozyczenieService = new WypozyczenieService();
    private ButyService butyService = new ButyService();

    public double obliczCeneUmowy(UmowaWypozyczenia umowa) {
        LocalDateTime teraz = LocalDateTime.now();
        LocalDateTime czasWypozyczenia = umowa.getDataCzas();

        // Obliczam ile dni minęło
        Duration okresWypozyczenia = Duration.between(czasWypozyczenia, teraz);
        long dni = (okresWypozyczenia.getSeconds() / 60) / 60 / 24;

        double kwotaFinalna = 0.0;
        Set<Wypozyczenie> wypozyczenieSet = umowa.getWypozyczenia();
        for (Wypozyczenie wypozyczenie : wypozyczenieSet) {
            kwotaFinalna += wypozyczenieService.obliczCeneWypozyczenia(wypozyczenie, dni);
        }

        return kwotaFinalna;
    }

    public boolean sprawdzDostepnoscButow(Long id) {
        Optional<Buty> butyOptional = butyService.pobierz(id);
        if (butyOptional.isEmpty()) {
//             throw new EntityNotFoundException("Nie ma takich butów w bazie");
            log.info("Nie ma takich butów w bazie");
            return false;
        }

        Buty buty = butyOptional.get();
        if (buty.getZestaw() != null) {
            log.info("Nie można wynająć, buty są częścią zestawu.");
            return false;
        }

        Set<Wypozyczenie> wypozyczenia = buty.getWypozyczenia();
        for (Wypozyczenie wypozyczenie : wypozyczenia) {
            if (wypozyczenie.getZwrot() == null){
                log.info("Nie można wynająć, buty są wciąż wypozyczone.");
                return false;
            }
        }

        // Buty:
        // - istnieją
        // - nie są częścią zestawu
        // - nie są wciąż wypożyczone
        return true;
    }

    public boolean sprawdzDostepnoscNart() {

    }

    public boolean sprawdzDostepnoscZestawu() {

    }
}
