package pl.sda.j133.magazyn.model.struct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Paweł Recław, AmeN
 * @project j133_powtorka_1
 * @created 14.01.2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IloscProduktu {
    private double cena;
    private long ilosc;
    private LocalDateTime dataDostawy;
}
