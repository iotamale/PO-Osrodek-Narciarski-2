package symulacja.odwiedzajacy;

import osrodek.krawedz.OdwiedzajacyKrawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;

import java.util.ArrayList;
import java.util.List;

public class OdwiedzajacyMapkiStatystyk implements OdwiedzajacyKrawedz<List<String>> {

    @Override
    public List<String> odwiedz(Trasa trasa) {
        final List<String> linie = new ArrayList<>();
        linie.add(String.format("t%d: śnieg: %.2f", trasa.id(), trasa.wyrownanieNawierzchni()));
        linie.add(String.format("zjazdy: %d", trasa.liczbaZjazdow()));
        return linie;
    }

    @Override
    public List<String> odwiedz(Wyciag wyciag) {
        final List<String> linie = new ArrayList<>();
        linie.add(String.format("w%d: kol: %d(śr), %d(maks)", wyciag.id(), wyciag.sredniaDlugoscKolejki(), wyciag.maksDlugoscKolejki()));
        linie.add(String.format("wjazdy: %d / %d (%d%%)", wyciag.lacznaLiczbaPasazerow(), wyciag.mozliweWjazdy(), wyciag.procentZajetychMiejsc()));
        return linie;
    }
}
