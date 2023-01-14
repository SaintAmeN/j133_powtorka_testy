package pl.sda.j133.magazyn.service;

import pl.sda.j133.magazyn.hibernate.DataAccessObject;
import pl.sda.j133.magazyn.model.Dostawa;
import pl.sda.j133.magazyn.model.PozycjaDostawy;
import pl.sda.j133.magazyn.model.Produkt;
import pl.sda.j133.magazyn.model.struct.IloscProduktu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 14.01.2023
 */
public class ProductService {
    private final DataAccessObject<Dostawa> dostawaDataAccessObject;
    private final DataAccessObject<PozycjaDostawy> pozycjaDostawyDataAccessObject;
    private final DataAccessObject<Produkt> produktDataAccessObject;

    public ProductService() {
        this.dostawaDataAccessObject = new DataAccessObject<>();
        this.pozycjaDostawyDataAccessObject = new DataAccessObject<>();
        this.produktDataAccessObject = new DataAccessObject<>();
    }

    // obliczenie ceny wersja uśredniona
    // obliczenie ceny wersja "od najstarszej"
    // wyświetl stan magazynowy wraz z dostawami

    public List<PozycjaDostawy> znajdzDostawyProduktu(Long idProduktu) {
        return pozycjaDostawyDataAccessObject.findAll(PozycjaDostawy.class)
                .stream()
                .filter(pozycjaDostawy -> Objects.equals(pozycjaDostawy.getProdukt().getId(), idProduktu))
                .toList();
    }

    /**
     * Wynikiem jest cena produktu po której musimy go sprzedać by wyjść "na zero".
     *
     * @param dostawy
     * @return
     */
    public double znajdzCeneUsredniona(List<PozycjaDostawy> dostawy) {
        final long iloscProduktu = dostawy.stream()
                .mapToLong(PozycjaDostawy::getIlosc)
                .sum();


        // Masło x 30 x 2.30 = XXX
        // Masło x 10 x 3.50 = XXX
        // -----------------------
        //                     sum
        // tutaj obliczamy ile nas kosztowało dane zamówienie
        final double cenaUsrednionaSuma = dostawy.stream()
                .mapToDouble(dostawa -> dostawa.getIlosc() * dostawa.getCena())
                .sum();

        return cenaUsrednionaSuma / iloscProduktu;
    }

    public List<IloscProduktu> znajdzCeneOdNajstarszej(Long idProduktu, long ilosc) {
        List<Dostawa> dostawy = znajdzDostawyProduktu(idProduktu)
                .stream().map(PozycjaDostawy::getDostawa)
                .sorted(Comparator.comparing(Dostawa::getDataCzas))
                .toList();

        List<IloscProduktu> ilosci = new ArrayList<>();
        long skompletowane = 0;
        for (int i = 0; i < dostawy.size(); i++) {
            List<PozycjaDostawy> pozycje = dostawy.get(i).getPozycje()
                    .stream()
                    .filter(pozycjaDostawy -> Objects.equals(pozycjaDostawy.getProdukt().getId(), idProduktu))
                    .toList();

            for (int j = 0; j < pozycje.size(); j++) {
                PozycjaDostawy dostawa = pozycje.get(j);

                long ileNamBrakuje = ilosc - skompletowane;
                if (ileNamBrakuje == 0) {
                    break;
                }

                if (dostawa.getIlosc() < ileNamBrakuje) {
                    // bierzemy całość
                    ilosci.add(new IloscProduktu(dostawa.getCena(), dostawa.getIlosc(), dostawy.get(i).getDataCzas()));
                    skompletowane += dostawa.getIlosc();
                } else {
                    ilosci.add(new IloscProduktu(dostawa.getCena(), ileNamBrakuje, dostawy.get(i).getDataCzas()));
                    skompletowane += ileNamBrakuje;
                }
            }
        }

        return ilosci;
    }


    // TODO: Zadanie domowe
    public double obliczCeneWszystkichDostawProduktu(Long idProdukt) {
        return 0.0;
    }

    // TODO: Zadanie domowe
    public double obliczCeneWszystkichSprzedazyProduktu(Long idProdukt) {
        return 0.0;
    }

    // TODO: Zadanie domowe
    public double znajdzIloscSprzedanychProduktow(Long idProdukt) {
        return 0.0;
    }
}
