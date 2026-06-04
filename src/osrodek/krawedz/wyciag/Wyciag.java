package osrodek.krawedz.wyciag;

import czas.Interwal;
import czas.Moment;
import dziennik.Dziennik;
import kolejkaZdarzen.zdarzenia.DotarcieDoWezla;
import kolejkaZdarzen.zdarzenia.OdjazdWyciagu;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import sportowcy.SportowiecLokalny;

public class Wyciag extends Krawedz {

    private static final Moment PIERWSZY_ODJAZD = new Moment(9, 0, 0);
    private static final Moment OSTATNI_INTERESUJACY_ODJAZD = new Moment(14, 59, 59);
    private static final int SEKUNDY_SYMULACJI = PIERWSZY_ODJAZD.roznicaBezwzgledna(OSTATNI_INTERESUJACY_ODJAZD);

    private final Interwal odstepMiedzyOdjazdami;
    private final int ladownosc;
    private final KolejkaSportowcow obecnaKolejka;
    private int lacznaLiczbaPasazerow;
    private int maksDlugoscKolejki;
    private long sumaDlugosciKolejki; // TODO
    private Moment ostatniaOperacjaNaKolejce;

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
        lacznaLiczbaPasazerow = maksDlugoscKolejki = 0;
        sumaDlugosciKolejki = 0;
        ostatniaOperacjaNaKolejce = PIERWSZY_ODJAZD;
    }

    /**
     * Funkcja odpowiedzialna za obsługę sum długości kolejki.
     * Jeśli moment wywołania.equals(ostatniaOperacjaNaKolejce), to oznacza to, że
     * operacja dzieje sie w tej samej sekundzie co poprzednia. Dopiero pierwsza
     * operacja z "nastepnej" sekundy zaaktulizuje licznik.
     */
    private void obslozSumeDlugKolejki(Moment moment) {
        if (moment.equals(ostatniaOperacjaNaKolejce)) {
            return;
        }

        final int roznica = moment.roznicaBezwzgledna(ostatniaOperacjaNaKolejce);
        sumaDlugosciKolejki += (long) obecnaKolejka.rozmiar() * roznica;

        ostatniaOperacjaNaKolejce = moment;
    }

    public void dodajDoKolejki(SportowiecLokalny sportowiec, Moment moment) {
        obslozSumeDlugKolejki(moment);

        obecnaKolejka.dodaj(sportowiec);
        maksDlugoscKolejki = Math.max(maksDlugoscKolejki, obecnaKolejka.rozmiar());
    }

    /**
     * Symuluje odjazd następnego wagonika oraz daje nowo stworzone zdarzenia:
     * 1. Zdarzenie reprezentujące odjazd następnego wagonika.
     * 2. Zdarzenia reprezentujące dotarcie do końca wyciagu sportowców, którzy załapali się na obecny odjazd.
     */
    public Zdarzenie[] odjazd(Moment moment, Dziennik dziennik) {
        obslozSumeDlugKolejki(moment);

        SportowiecLokalny[] odjezdzajacySportowcy = obecnaKolejka.zdejmij(Math.min(obecnaKolejka.rozmiar(), ladownosc));

        for (SportowiecLokalny sportowiec : odjezdzajacySportowcy) {
            dziennik.dodajWpisZeSportowcem(moment, sportowiec, String.format("rozpoczął wjazd %s", toString()));
            sportowiec.zglosPrzejazdWyciagiem(this);
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

    private double zaokrDoDwoch(double x) {
        return (int) (x * 100) / 100.0;
    }

    private double procentZajetychMiejsc() {
        final int ilePrzejazdow = (odstepMiedzyOdjazdami.sekundy() / SEKUNDY_SYMULACJI) + 1;
        return zaokrDoDwoch((double) lacznaLiczbaPasazerow / (ilePrzejazdow * ladownosc));
    }

    private double sredniaDlugoscKolejki() {
        return zaokrDoDwoch((double) sumaDlugosciKolejki / (SEKUNDY_SYMULACJI + 1));
    }

    // TODO
    @Override
    public String wypiszStatystyki() {
        return String.format("Maks długość kolejki: %d\nŚrednia długość kolejki: %f\nŁączna liczba pasażerów: %d\nProcent zajętych miejsc: %f",
                maksDlugoscKolejki, sredniaDlugoscKolejki(), lacznaLiczbaPasazerow, procentZajetychMiejsc());
    }

    @Override
    public String toString() {
        return String.format("Wyciąg nr %d", id());
    }
}
