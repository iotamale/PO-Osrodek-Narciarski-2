package kolejkaZdarzen.zdarzenia;

import czas.Moment;
import dziennik.Dziennik;
import osrodek.Osrodek;
import osrodek.krawedz.wyciag.Wyciag;

public class OdjazdWyciagu extends Zdarzenie {

    private final Wyciag wyciag;

    public OdjazdWyciagu(Moment moment, Wyciag wyciag) {
        super(moment);
        this.wyciag = wyciag;
    }

    public Wyciag wyciag() {
        return wyciag;
    }

    @Override
    public Zdarzenie[] przetworz(Dziennik dziennik, Osrodek osrodek) {
        return wyciag.odjazd(moment, dziennik);
    }

    @Override
    public boolean czyPrzetwarzacPoZakonczeniuSymulacji() {
        return false;
    }

    @Override
    public String toString() {
        return "OdjazdWyciagu [wyciag=" + wyciag + ", super= " + super.toString() + "]";
    }
}
