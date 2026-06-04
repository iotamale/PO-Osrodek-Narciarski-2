package kolejkaZdarzen.zdarzenia;

import czas.Moment;
import dziennik.Dziennik;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.SportowiecLokalny;

public class DolaczenieDoKolejki extends Zdarzenie {

    private final Wyciag wyciag;

    private final SportowiecLokalny sportowiec;

    public DolaczenieDoKolejki(Moment moment, Wyciag wyciag, SportowiecLokalny sportowiec) {
        super(moment);
        this.wyciag = wyciag;
        this.sportowiec = sportowiec;
    }

    public Wyciag wyciag() {
        return wyciag;
    }

    public SportowiecLokalny sportowiec() {
        return sportowiec;
    }

    @Override
    public Zdarzenie[] przetworz(Dziennik dziennik) {
        dziennik
            .dodajWpisZeSportowcem(moment, sportowiec, String.format("dołączył do kolejki w %s", wyciag.toString()));

        wyciag.dodajDoKolejki(sportowiec);

        return new Zdarzenie[0];
    }

    @Override
    public boolean czyPrzetwarzacPoZakonczeniuSymulacji() {
        return false;
    }

    @Override
    public String toString() {
        return "DolaczenieDoKolejki [wyciag=" + wyciag + ", sportowiec=" + sportowiec + ", super=" + super.toString()
            + "]";
    }
}
