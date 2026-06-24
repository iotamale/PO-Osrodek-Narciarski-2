package osrodek.krawedz.wyciag;

import czas.Interwal;
import czas.Moment;
import dziennik.Dziennik;
import kolejkaZdarzen.zdarzenia.DolaczenieDoKolejki;
import kolejkaZdarzen.zdarzenia.DotarcieDoWezla;
import kolejkaZdarzen.zdarzenia.OdjazdWyciagu;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import sportowcy.Sportowiec;

import java.util.ArrayList;
import java.util.List;

public class Wyciag extends Krawedz {

    private static final Moment PIERWSZY_ODJAZD = new Moment(9, 0, 0);
    private static final Moment OSTATNI_ODJAZD = new Moment(15, 0, 0);
    private static final int CZAS_SYMULACJI_S = PIERWSZY_ODJAZD.roznicaBezwzglednaWSekundach(OSTATNI_ODJAZD);

    private final Interwal odstepMiedzyOdjazdami;
    private final int ladownosc;
    private final KolejkaSportowcow obecnaKolejka;
    private int lacznaLiczbaPasazerow;
    private int maksDlugoscKolejki;
    private long sumaDlugosciKolejki;
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

        lacznaLiczbaPasazerow = 0;
        maksDlugoscKolejki = 0;
        sumaDlugosciKolejki = 0;
        ostatniaOperacjaNaKolejce = PIERWSZY_ODJAZD;
    }

    public int lacznaLiczbaPasazerow() {
        return lacznaLiczbaPasazerow;
    }

    public int maksDlugoscKolejki() {
        return maksDlugoscKolejki;
    }

    /**
     * Funkcja odpowiedzialna za aktulizacje statystyk kolejki.
     */
    private void zaaktulizujStatystykiKolejki(Moment moment) {
        final int mineloSekund = moment.roznicaBezwzglednaWSekundach(ostatniaOperacjaNaKolejce);

        if (mineloSekund == 0) {
            return;
        }

        sumaDlugosciKolejki += (long) mineloSekund * obecnaKolejka.rozmiar();
        ostatniaOperacjaNaKolejce = moment;
    }

    public void dodajDoKolejki(Sportowiec sportowiec, Moment moment) {
        zaaktulizujStatystykiKolejki(moment);
        obecnaKolejka.dodaj(sportowiec);

        final int obecnyRozmiar = obecnaKolejka.rozmiar();
        if (obecnyRozmiar > maksDlugoscKolejki) {
            maksDlugoscKolejki = obecnyRozmiar;
        }
    }

    /**
     * Symuluje odjazd następnego wagonika oraz daje nowo stworzone zdarzenia:
     * 1. Zdarzenie reprezentujące odjazd następnego wagonika.
     * 2. Zdarzenia reprezentujące dotarcie do końca wyciagu sportowców, którzy załapali się na obecny odjazd.
     */
    public Zdarzenie[] odjazd(Moment moment, Dziennik dziennik) {
        zaaktulizujStatystykiKolejki(moment);   // Aktulizujemy przed zdjęciem z kolejki.

        Sportowiec[] odjezdzajacySportowcy = obecnaKolejka.zdejmij(Math.min(obecnaKolejka.rozmiar(), ladownosc));

        for (Sportowiec sportowiec : odjezdzajacySportowcy) {
            dziennik.dodajWpisZeSportowcem(moment, sportowiec, String.format("rozpoczął wjazd %s", toString()));
            sportowiec.zarejestrujPrzejazdWyciagiem(this);
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

    /**
     * Zwraca średnią długość kolejki zaokrągloną do najbliższej liczby całkowitej.
     */
    private int sredniaDlugoscKolejki() {
        return (int) Math.round((double) sumaDlugosciKolejki / CZAS_SYMULACJI_S);
    }

    /**
     * Zwraca ilość wjazdów, które są teoretycznie możliwe w godzinach pracy wyciągu.
     */
    private int mozliweWjazdy() {
        return (CZAS_SYMULACJI_S / odstepMiedzyOdjazdami.sekundy()) * ladownosc;
    }

    /**
     * Zwraca procent zajętych miejsc na wyciągu w zaokręgleniu do liczby całkowitej.
     */
    private int procentZajetychMiejsc() {
        return (int) Math.round((double) lacznaLiczbaPasazerow / mozliweWjazdy() * 100);
    }

    /**
     * Generuje "ładne" statystyki wypisywane dla użytkownika do dziennika.
     */
    @Override
    public String wypiszStatystyki() {
        zaaktulizujStatystykiKolejki(OSTATNI_ODJAZD);

        return String.format("Maks długość kolejki: %d\nŚrednia długość kolejki: %d\nŁączna liczba pasażerów: %d\nProcent zajętych miejsc: %d",
                maksDlugoscKolejki, sredniaDlugoscKolejki(), lacznaLiczbaPasazerow, procentZajetychMiejsc());
    }

    /**
     * Tworzy zdarzenie dołączenia do kolejki do wyciągu w nastepnym kroku.
     */
    @Override
    public Zdarzenie zdarzenieNastepnegoKroku(Moment moment, Sportowiec sportowiec) {
        return new DolaczenieDoKolejki(moment, this, sportowiec);
    }

    /**
     * Generuje opis wykorzystywany do pierwszej mapki (parametry).
     */
    @Override
    public List<String> generujOpisMapkaParametrow() {
        final List<String> linie = new ArrayList<>();
        linie.add(etykietaPodMapke() + String.format(": %d os. co %ds", ladownosc, odstepMiedzyOdjazdami.sekundy()));
        linie.add(String.format("czas: %ds", dlugosc().sekundy()));
        return linie;
    }

    /**
     * Generuje opis wykorzystywany do drugiej mapki (statystyki).
     */
    @Override
    public List<String> generujOpisMapkaStatystyk() {
        final List<String> linie = new ArrayList<>();
        linie.add(etykietaPodMapke() + String.format(": kol: %d(śr), %d(maks)", sredniaDlugoscKolejki(), maksDlugoscKolejki));
        linie.add(String.format("wjazdy: %d / %d (%d%%)", lacznaLiczbaPasazerow, mozliweWjazdy(), procentZajetychMiejsc()));
        return linie;
    }

    @Override
    protected String oznaczenieRodzaju() {
        return "w";
    }

    @Override
    public String toString() {
        return String.format("Wyciąg nr %d", id());
    }

}
