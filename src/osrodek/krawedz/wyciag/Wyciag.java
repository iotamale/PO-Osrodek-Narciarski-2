package osrodek.krawedz.wyciag;

import czas.Interwal;
import czas.Moment;
import dziennik.Dziennik;
import kolejkaZdarzen.zdarzenia.DotarcieDoWezla;
import kolejkaZdarzen.zdarzenia.OdjazdWyciagu;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import sportowcy.Sportowiec;

public class Wyciag extends Krawedz {

    private final Interwal odstepMiedzyOdjazdami;

    private final int ladownosc;

    private final KolejkaSportowcow obecnaKolejka;

    private int lacznaLiczbaPasazerow;

    public Wyciag(int id,
        Wezel poczatek,
        Wezel koniec,
        Interwal odstepMiedzyOdjazdami,
        Interwal dlugoscPrzejazdu,
        int ladownosc) {
        super(id, poczatek, koniec, dlugoscPrzejazdu);

        assert poczatek.wysokosc() < koniec.wysokosc()
            : String.format("Wyciąg %d prowadzi w dół: %d -> %d", id, poczatek.wysokosc(), koniec.wysokosc());

        this.odstepMiedzyOdjazdami = odstepMiedzyOdjazdami;
        this.ladownosc = ladownosc;
        obecnaKolejka = new BuforCyklicznySportowcow();
        lacznaLiczbaPasazerow = 0;
    }

    public void dodajDoKolejki(Sportowiec sportowiec) {
        obecnaKolejka.dodaj(sportowiec);
    }

    /**
     * Symuluje odjazd następnego wagonika oraz daje nowo stworzone zdarzenia:
     * 1. Zdarzenie reprezentujące odjazd następnego wagonika.
     * 2. Zdarzenia reprezentujące dotarcie do końca wyciagu sportowców, którzy załapali się na obecny odjazd.
     */
    public Zdarzenie[] odjazd(Moment moment, Dziennik dziennik) {
        Sportowiec[] odjezdzajacySportowcy = obecnaKolejka.zdejmij(Math.min(obecnaKolejka.rozmiar(), ladownosc));

        for (Sportowiec sportowiec : odjezdzajacySportowcy) {
            dziennik.dodajWpisZeSportowcem(moment, sportowiec, String.format("rozpoczął wjazd %s", toString()));
        }

        Zdarzenie[] noweZdarzenia = new Zdarzenie[1 + odjezdzajacySportowcy.length];
        noweZdarzenia[0] = new OdjazdWyciagu(moment.dodajInterwal(odstepMiedzyOdjazdami), this);

        for (int i = 0; i < odjezdzajacySportowcy.length; i++) {
            noweZdarzenia[1 + i] = new DotarcieDoWezla(moment.dodajInterwal(dlugosc()),
                this,
                koniec(),
                odjezdzajacySportowcy[i]);
        }

        lacznaLiczbaPasazerow += odjezdzajacySportowcy.length;

        return noweZdarzenia;
    }

    @Override
    public String wypiszStatystyki() {
        return String.format("%d pasażerów", lacznaLiczbaPasazerow);
    }

    @Override
    public String toString() {
        return String.format("Wyciąg nr %d", id());
    }
}
