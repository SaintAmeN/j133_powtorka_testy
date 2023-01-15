package pl.sda.j133.magazyn.service;

import jakarta.persistence.EntityNotFoundException;
import pl.sda.j133.magazyn.hibernate.DataAccessObject;
import pl.sda.j133.magazyn.model.*;
import pl.sda.j133.magazyn.model.struct.IloscProduktu;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 14.01.2023
 */
public class ProductService {
    private final DataAccessObject<Sprzedaz> sprzedazDataAccessObject;
    private final DataAccessObject<PozycjaSprzedazy> pozycjaSprzedazyDataAccessObject;
    private final DataAccessObject<Dostawa> dostawaDataAccessObject;
    private final DataAccessObject<PozycjaDostawy> pozycjaDostawyDataAccessObject;
    private final DataAccessObject<Produkt> produktDataAccessObject;

    public ProductService() {
        this.sprzedazDataAccessObject = new DataAccessObject<>();
        this.pozycjaSprzedazyDataAccessObject = new DataAccessObject<>();
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

        long wszystkichSprzedanych = znajdzIloscSprzedanychProduktow(idProduktu);

        List<IloscProduktu> ilosci = new ArrayList<>();
        long skompletowane = 0;
        long iloscSprzedanych = 0;
        for (int i = 0; i < dostawy.size(); i++) {
            List<PozycjaDostawy> pozycje = dostawy.get(i).getPozycje()
                    .stream()
                    .filter(pozycjaDostawy -> Objects.equals(pozycjaDostawy.getProdukt().getId(), idProduktu))
                    .toList();

            for (int j = 0; j < pozycje.size(); j++) {
                PozycjaDostawy dostawa = pozycje.get(j);

                long iloscWDostawie = dostawa.getIlosc();
                if (iloscSprzedanych < wszystkichSprzedanych) {
                    long ileSprzedanychNamBrakuje = wszystkichSprzedanych - iloscSprzedanych;
                    if (ileSprzedanychNamBrakuje != 0) {
                        if (iloscWDostawie < ileSprzedanychNamBrakuje) {
                            iloscSprzedanych += iloscWDostawie;
                            iloscWDostawie = 0;
                            continue;
                        } else {
                            iloscSprzedanych += ileSprzedanychNamBrakuje;
                            iloscWDostawie = iloscWDostawie - ileSprzedanychNamBrakuje;
                        }
                    }
                }

                long ileNamBrakuje = ilosc - skompletowane;
                if (ileNamBrakuje == 0) {
                    break;
                }

                if (iloscWDostawie < ileNamBrakuje) {
                    // bierzemy całość
                    ilosci.add(new IloscProduktu(dostawa.getCena(), iloscWDostawie, dostawy.get(i).getDataCzas()));
                    skompletowane += iloscWDostawie;
                } else {
                    ilosci.add(new IloscProduktu(dostawa.getCena(), ileNamBrakuje, dostawy.get(i).getDataCzas()));
                    skompletowane += ileNamBrakuje;
                }
            }
        }

        return ilosci;
    }

    public double obliczCeneWszystkichDostawProduktu(Long idProdukt) {
        List<PozycjaDostawy> pozycjaDostaw = produktDataAccessObject.find(Produkt.class, idProdukt)
                .stream()
                .flatMap(produkt -> produkt.getDostawy().stream())
                .toList();

        double cenyDostaw = pozycjaDostaw
                .stream()
                .mapToDouble(value -> value.getIlosc() * value.getCena())
                .sum();

        return cenyDostaw;
    }

    public double obliczCeneWszystkichSprzedazyProduktu(Long idProdukt) {
        List<PozycjaSprzedazy> sprzedaze = produktDataAccessObject.find(Produkt.class, idProdukt)
                .stream()
                .flatMap(produkt -> produkt.getSprzedaze().stream())
                .toList();

        double cenySprzedazy = sprzedaze
                .stream()
                .mapToDouble(value -> value.getIlosc() * value.getCena())
                .sum();

        return cenySprzedazy;
    }

    public long znajdzIloscSprzedanychProduktow(Long idProdukt) {
        List<PozycjaSprzedazy> sprzedaze = produktDataAccessObject.find(Produkt.class, idProdukt)
                .stream()
                .flatMap(produkt -> produkt.getSprzedaze().stream())
                .toList();

        return sprzedaze
                .stream()
                .mapToInt(PozycjaSprzedazy::getIlosc)
                .sum();
    }

    public void dodajProdukt(String nazwa) {
        Produkt p = Produkt.builder().nazwa(nazwa).build();

        produktDataAccessObject.insert(p);
    }

    public void dodajDostawe(Long produktId, int ilosc, double cena) {
        Optional<Produkt> productOptional = produktDataAccessObject.find(Produkt.class, produktId);
        if (productOptional.isEmpty()){
            throw new EntityNotFoundException("Nie ma takiego produktu");
        }

        Dostawa dostawa = Dostawa.builder()
                .dataCzas(LocalDateTime.now())
                .build();
        dostawaDataAccessObject.insert(dostawa);

        PozycjaDostawy pozycjaDostawy = PozycjaDostawy.builder()
                .ilosc(ilosc)
                .cena(cena)
                .produkt(productOptional.get())
                .dostawa(dostawa)
                .build();
        pozycjaDostawyDataAccessObject.insert(pozycjaDostawy);
    }

    public void dodajSprzedaz(Long produktId, int ilosc, double cena) {
        Optional<Produkt> productOptional = produktDataAccessObject.find(Produkt.class, produktId);
        if (productOptional.isEmpty()){
            throw new EntityNotFoundException("Nie ma takiego produktu");
        }
        Sprzedaz sprzedaz = Sprzedaz.builder()
                .dataCzas(LocalDateTime.now())
                .build();
        sprzedazDataAccessObject.insert(sprzedaz);

        PozycjaSprzedazy pozycjaSprzedazy = PozycjaSprzedazy.builder()
                .ilosc(ilosc)
                .cena(cena)
                .produkt(productOptional.get())
                .sprzedaz(sprzedaz)
                .build();
        pozycjaSprzedazyDataAccessObject.insert(pozycjaSprzedazy);
    }
}
