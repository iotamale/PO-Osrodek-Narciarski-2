package osrodek.krawedz;

import czas.Interwal;
import czas.Moment;
import kolejkaZdarzen.zdarzenia.RozpoczecieZjazdu;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Wezel;
import sportowcy.Sportowiec;

import java.util.ArrayList;

public class Trasa extends Krawedz {

    private final int poziomTrudnosci; // {0, 1, ..., 10}

    private final double bazowaAtrakcyjnosc; // [0, 1]

    private final double odpornoscNaNierownosci; // [0, 1]

    private int liczbaZjazdow;

    public Trasa(int id,
        Wezel poczatek,
        Wezel koniec,
        Interwal dlugoscZjazdu,
        int poziomTrudnosci,
        double bazowaAtrakcyjnosc,
        double odpornoscNaNierownosci) {
        super(id, poczatek, koniec, dlugoscZjazdu);

        assert poczatek.wysokosc() > koniec.wysokosc()
            : String.format("Trasa %d prowadzi w górę: %d -> %d", id, poczatek.wysokosc(), koniec.wysokosc());

        this.poziomTrudnosci = poziomTrudnosci;
        this.bazowaAtrakcyjnosc = bazowaAtrakcyjnosc;
        this.odpornoscNaNierownosci = odpornoscNaNierownosci;
        liczbaZjazdow = 0;
    }

    public int poziomTrudnosci() {
        return poziomTrudnosci;
    }

    public double bazowaAtrakcyjnosc() {
        return bazowaAtrakcyjnosc;
    }

    public double odpornoscNaNierownosci() {
        return odpornoscNaNierownosci;
    }

    public int liczbaZjazdow() {
        return liczbaZjazdow;
    }

    public Moment przemierz(Moment start) {
        liczbaZjazdow++;
        return start.dodajInterwal(dlugosc());
    }

    public double wyrownanieNawierzchni() {
        return (bazowaAtrakcyjnosc + (1 - bazowaAtrakcyjnosc) * Math.pow(odpornoscNaNierownosci, liczbaZjazdow));
    }

    @Override
    public String wypiszStatystyki() {
        return String.format("Zjazdów: %d\nWyrównanie trasy na koniec dnia: %f", liczbaZjazdow, wyrownanieNawierzchni());
    }

    /**
     * Tworzy zdarzenie zjazdu bezpośrednią trasą w następnym kroku.
     */
    @Override
    public Zdarzenie zdarzenieNastepnegoKroku(Moment moment, Sportowiec sportowiec) {
        return new RozpoczecieZjazdu(moment, this, sportowiec);
    }


    /**
     * Generuje opis wykorzystywany do pierwszej mapki.
     */
    @Override
    public ArrayList<String> generujOpisMapkaParametrow() {
        ArrayList<String> linie = new ArrayList<>();
        linie.add(String.format("t%d: poziom: %d, czas: %ds", id(), poziomTrudnosci, dlugosc().sekundy()));
        linie.add(String.format("odporność: %.2f, %.5f", bazowaAtrakcyjnosc, odpornoscNaNierownosci));
        return linie;
    }

    @Override
    public String toString() {
        return String.format("t%d", id());
    }
}
