package kolejkaZdarzen.zdarzenia;

import czas.Moment;
import dziennik.Dziennik;
import osrodek.Wezel;
import sportowcy.SportowiecLokalny;

public class PoczatekDnia extends Zdarzenie {

    private final Wezel wezel;

    private final SportowiecLokalny sportowiec;

    public PoczatekDnia(Moment moment, Wezel wezel, SportowiecLokalny sportowiec) {
        super(moment);
        this.wezel = wezel;
        this.sportowiec = sportowiec;
    }

    public Wezel wezel() {
        return wezel;
    }

    public SportowiecLokalny sportowiec() {
        return sportowiec;
    }

    @Override
    public Zdarzenie[] przetworz(Dziennik dziennik) {
        dziennik.dodajWpisZeSportowcem(moment,
            sportowiec,
            String.format("rozpoczął swój dzień na stoku w %s", wezel.toString()));

        return new Zdarzenie[]{sportowiec.nastepnyKrok(moment, wezel)};
    }

    @Override
    public boolean czyPrzetwarzacPoZakonczeniuSymulacji() {
        return false;
    }

    @Override
    public String toString() {
        return "PoczatekDnia [wezel=" + wezel + ", sportowiec=" + sportowiec + ", super=" + super.toString() + "]";
    }
}
