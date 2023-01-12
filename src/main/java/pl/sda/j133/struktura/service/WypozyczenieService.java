package pl.sda.j133.struktura.service;

import pl.sda.j133.struktura.model.Wypozyczenie;

import java.util.List;
import java.util.Optional;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 12.01.2023
 */
public class WypozyczenieService {
    public double obliczCeneWypozyczenia(Wypozyczenie wypozyczenie, long iloscDni) {
        // ktoś wynajął na 4 dni a zwrócił po 3 to wynik = -1 (naliczamy tylko 3 dni w orginalnej cenie)
        // ktoś wynajął na 4 dni a zwrócił po 4 to wynik = 0
        // ktoś wynajął na 4 dni a zwrócił po 5 to wynik = 1 (naliczamy 1 extra dzień w cenie +20%)
        long iloscDniPonad = iloscDni - wypozyczenie.getDlugoscWypozyczenia();

        if (iloscDniPonad <= 0) {
            return iloscDni * wypozyczenie.getCenaZaDzien();
        }

        // za każdy dzień extra cena jest wyższa o +20%
        double doZaplatyExtra = iloscDniPonad * (wypozyczenie.getCenaZaDzien() * 1.20);

        return wypozyczenie.getDlugoscWypozyczenia() * wypozyczenie.getCenaZaDzien() + doZaplatyExtra;
    }

    public List<Wypozyczenie> pobierzWszystkieWypozyczenia(){
        return List.of();
    }

    public Optional<Wypozyczenie> pobierzWypozyczenie(){
        return Optional.empty();
    }
}
