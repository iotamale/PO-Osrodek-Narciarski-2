package symulacja.odwiedzajacy;

import osrodek.krawedz.OdwiedzajacyKrawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.historia_przejazdow.HistoriaPrzejazdowSportowca;

public class OdwiedzajacyMapkiSportowcow implements OdwiedzajacyKrawedz<String> {

    private final HistoriaPrzejazdowSportowca historia;

    public OdwiedzajacyMapkiSportowcow(HistoriaPrzejazdowSportowca historia) {
        this.historia = historia;
    }

    private String budujLinie(String prefiks, int id, int liczbaPrzejazdow, String zapisRejestru) {
        return String.format("%s%d(%d): %s", prefiks, id, liczbaPrzejazdow, zapisRejestru);
    }

    @Override
    public String odwiedz(Trasa trasa) {
        return budujLinie("t", trasa.id(), historia.liczbaPrzejazdowKrawedzia(trasa), historia.pobierzZapisRejestru(trasa));
    }

    @Override
    public String odwiedz(Wyciag wyciag) {
        return budujLinie("w", wyciag.id(), historia.liczbaPrzejazdowKrawedzia(wyciag), historia.pobierzZapisRejestru(wyciag));
    }
}
