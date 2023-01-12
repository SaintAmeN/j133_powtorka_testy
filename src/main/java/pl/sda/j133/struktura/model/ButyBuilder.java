//package pl.sda.j133.struktura.model;
//
//import java.util.Set;
//
//public class ButyBuilder {
//    private Long id;
//    private String nazwa;
//    private Zestaw zestaw;
//    private Set<Wypozyczenie> wypozyczenia;
//
//    public ButyBuilder setId(Long id) {
//        this.id = id;
//        return this;
//    }
//
//    public ButyBuilder setNazwa(String nazwa) {
//        this.nazwa = nazwa;
//        return this;
//    }
//
//    public ButyBuilder setZestaw(Zestaw zestaw) {
//        this.zestaw = zestaw;
//        return this;
//    }
//
//    public ButyBuilder setWypozyczenia(Set<Wypozyczenie> wypozyczenia) {
//        this.wypozyczenia = wypozyczenia;
//        return this;
//    }
//
//    public Buty createButy() {
//        return new Buty(id, nazwa, zestaw, wypozyczenia);
//    }
//}