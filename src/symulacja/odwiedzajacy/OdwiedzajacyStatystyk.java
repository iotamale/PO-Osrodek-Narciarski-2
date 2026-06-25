package symulacja.odwiedzajacy;

import osrodek.krawedz.OdwiedzajacyKrawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;

public class OdwiedzajacyStatystyk implements OdwiedzajacyKrawedz<String> {

    @Override
    public String odwiedz(Trasa trasa) {
        return String.format("Zjazdów: %d | Wyrównanie trasy: %f", trasa.liczbaZjazdow(), trasa.wyrownanieNawierzchni());
    }

    @Override
    public String odwiedz(Wyciag wyciag) {
        return String.format("Max dł. kolejki: %d | Śr. dł. kolejki: %d | Pasażerowie: %d | Procent zajętych miejsc: %d",
                wyciag.maksDlugoscKolejki(), wyciag.sredniaDlugoscKolejki(), wyciag.lacznaLiczbaPasazerow(), wyciag.procentZajetychMiejsc());
    }

}
