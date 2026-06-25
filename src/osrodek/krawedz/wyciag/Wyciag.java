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
import osrodek.krawedz.OdwiedzajacyKrawedz;
import sportowcy.Sportowiec;

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

    public int ladownosc() {
        return ladownosc;
    }

    public Interwal odstepMiedzyOdjazdami() {
        return odstepMiedzyOdjazdami;
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

        List<Sportowiec> odjezdzajacySportowcy = obecnaKolejka.zdejmij(Math.min(obecnaKolejka.rozmiar(), ladownosc));

        for (Sportowiec sportowiec : odjezdzajacySportowcy) {
            dziennik.dodajWpisZeSportowcem(moment, sportowiec, String.format("rozpoczął wjazd %s", toString()));
            sportowiec.zarejestrujPrzejazdWyciagiem(this);
        }

        Zdarzenie[] noweZdarzenia = new Zdarzenie[1 + odjezdzajacySportowcy.size()];
        noweZdarzenia[0] = new OdjazdWyciagu(moment.dodajInterwal(odstepMiedzyOdjazdami), this);

        for (int i = 0; i < odjezdzajacySportowcy.size(); i++) {
            noweZdarzenia[1 + i] = new DotarcieDoWezla(moment.dodajInterwal(dlugosc()),
                this,
                koniec(),
                odjezdzajacySportowcy.get(i));
        }

        lacznaLiczbaPasazerow += odjezdzajacySportowcy.size();

        return noweZdarzenia;
    }

    /**
     * Zwraca średnią długość kolejki zaokrągloną do najbliższej liczby całkowitej.
     */
    public int sredniaDlugoscKolejki() {
        return (int) Math.round((double) sumaDlugosciKolejki / CZAS_SYMULACJI_S);
    }

    /**
     * Zwraca ilość wjazdów, które są teoretycznie możliwe w godzinach pracy wyciągu.
     */
    public int mozliweWjazdy() {
        return (CZAS_SYMULACJI_S / odstepMiedzyOdjazdami.sekundy()) * ladownosc;
    }

    /**
     * Zwraca procent zajętych miejsc na wyciągu w zaokręgleniu do liczby całkowitej.
     */
    public int procentZajetychMiejsc() {
        return (int) Math.round((double) lacznaLiczbaPasazerow / mozliweWjazdy() * 100);
    }

    /**
     * Funkcja wywoływana na koniec dnia. Upewnia się, że wszystkie zjazdy
     * zostały dodane do statystyk.
     */
    public void zakonczDzien() {
        zaaktulizujStatystykiKolejki(OSTATNI_ODJAZD);
    }

    @Override
    public <T> T przyjmij(OdwiedzajacyKrawedz<T> visitor) {
        return visitor.odwiedz(this);
    }

    public Zdarzenie zdarzenieNastepnegoKroku(Moment moment, Sportowiec sportowiec) {
        return new DolaczenieDoKolejki(moment, this, sportowiec);
    }

    @Override
    public String toString() {
        return String.format("Wyciąg nr %d", id());
    }

}
