package symulacja.odwiedzajacy;

import osrodek.krawedz.OdwiedzajacyKrawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;

import java.util.ArrayList;
import java.util.List;

public class OdwiedzajacyMapkiParametrow implements OdwiedzajacyKrawedz<List<String>> {

    @Override
    public List<String> odwiedz(Trasa trasa) {
        final List<String> linie = new ArrayList<>();
        linie.add(String.format("t%d: poziom: %d, czas: %ds", trasa.id(), trasa.poziomTrudnosci(), trasa.dlugosc().sekundy()));
        linie.add(String.format("odporność: %.2f, %.5f", trasa.bazowaAtrakcyjnosc(), trasa.odpornoscNaNierownosci()));
        return linie;
    }

    @Override
    public List<String> odwiedz(Wyciag wyciag) {
        final List<String> linie = new ArrayList<>();
        linie.add(String.format("w%d: %d os. co %ds", wyciag.id(), wyciag.ladownosc(), wyciag.odstepMiedzyOdjazdami().sekundy()));
        linie.add(String.format("czas: %ds", wyciag.dlugosc().sekundy()));
        return linie;
    }

}
