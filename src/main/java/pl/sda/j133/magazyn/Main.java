package pl.sda.j133.magazyn;

import pl.sda.j133.magazyn.model.PozycjaDostawy;
import pl.sda.j133.magazyn.model.struct.IloscProduktu;
import pl.sda.j133.magazyn.service.ProductService;

import java.util.List;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 14.01.2023
 */
public class Main {
    public static void main(String[] args) {
        ProductService productService = new ProductService();
//        productService.dodajProdukt("Masło");
//        productService.dodajDostawe(1L, 10, 2.30);
//        productService.dodajDostawe(1L, 20, 4.30);
//        productService.dodajDostawe(1L, 15, 3.00);
//        productService.dodajDostawe(1L, 5, 6.30);
//        productService.dodajSprzedaz(1L, 5, 2.30);

        List<PozycjaDostawy> dostawy = productService.znajdzDostawyProduktu(1L);
        dostawy.forEach(System.out::println);

        List<IloscProduktu> ilosci = productService.znajdzCeneOdNajstarszej(1L, 30);
        ilosci.forEach(System.out::println);
    }
}
