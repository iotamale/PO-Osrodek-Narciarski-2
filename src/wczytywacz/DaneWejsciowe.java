package wczytywacz;

import java.util.List;

import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.GrupaSportowcow;
import sportowcy.Sportowiec;

public class DaneWejsciowe {

    private final Osrodek osrodek;

    private final List<Sportowiec> sportowcy;

    public DaneWejsciowe(List<Wezel> wezly, List<Trasa> trasy, List<Wyciag> wyciagi, List<GrupaSportowcow> grupySportowcow) {
        for (final Wezel wezel : wezly) {
            wezel.wychodzaceTrasy(znajdzWychodzaceTrasy(trasy, wezel));
            wezel.wychodzaceWyciagi(znajdzWychodzaceWyciagi(wyciagi, wezel));
        }
        this.osrodek = new Osrodek(wezly, trasy, wyciagi);
        this.sportowcy = przetworzGrupy(grupySportowcow);
    }

    public Osrodek osrodek() {
        return osrodek;
    }

    public List<Sportowiec> sportowcy() {
        return sportowcy;
    }

    private List<Trasa> znajdzWychodzaceTrasy(List<Trasa> trasy, Wezel wezel) {
        return trasy.stream()
                .filter(trasa -> trasa.poczatek().equals(wezel))
                .toList();
    }

    private List<Wyciag> znajdzWychodzaceWyciagi(List<Wyciag> wyciagi, Wezel wezel) {
        return wyciagi.stream()
                .filter(wyciag -> wyciag.poczatek().equals(wezel))
                .toList();
    }

    private List<Sportowiec> przetworzGrupy(List<GrupaSportowcow> grupySportowcow) {
        return grupySportowcow.stream()
                .flatMap(grupa -> grupa.podajSportowcow().stream())
                .toList();
    }

    @Override
    public String toString() {
        return "DaneWejsciowe [osrodek=" + osrodek + ", sportowcy=" + sportowcy + "]";
    }
}
