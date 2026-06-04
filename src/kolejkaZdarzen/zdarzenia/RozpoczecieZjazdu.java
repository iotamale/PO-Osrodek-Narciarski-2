package kolejkaZdarzen.zdarzenia;

import czas.Moment;
import dziennik.Dziennik;
import osrodek.krawedz.Trasa;
import sportowcy.SportowiecLokalny;

public class RozpoczecieZjazdu extends Zdarzenie {

    private final Trasa trasa;

    private final SportowiecLokalny sportowiec;

    public RozpoczecieZjazdu(Moment moment, Trasa trasa, SportowiecLokalny sportowiec) {
        super(moment);
        this.trasa = trasa;
        this.sportowiec = sportowiec;
    }

    public Trasa trasa() {
        return trasa;
    }

    public SportowiecLokalny sportowiec() {
        return sportowiec;
    }

    @Override
    public Zdarzenie[] przetworz(Dziennik dziennik) {
        dziennik.dodajWpisZeSportowcem(moment, sportowiec, String.format("rozpoczyna zjazd %s", trasa.toString()));

        return new Zdarzenie[]{new DotarcieDoWezla(trasa.przemierz(moment), trasa, trasa.koniec(), sportowiec)};
    }

    @Override
    public boolean czyPrzetwarzacPoZakonczeniuSymulacji() {
        return false;
    }

    @Override
    public String toString() {
        return "RozpoczecieZjazdu [trasa=" + trasa + ", sportowiec=" + sportowiec + ", super=" + super.toString() + "]";
    }
}
