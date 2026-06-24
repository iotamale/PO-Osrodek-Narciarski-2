package osrodek.krawedz;

import czas.Interwal;
import czas.Moment;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Wezel;
import sportowcy.Sportowiec;

import java.util.ArrayList;
import java.util.List;

public abstract class Krawedz {

    private final int id;

    private final Wezel poczatek;

    private final Wezel koniec;

    private final Interwal dlugosc;

    public Krawedz(int id, Wezel poczatek, Wezel koniec, Interwal dlugosc) {
        this.id = id;
        this.poczatek = poczatek;
        this.koniec = koniec;
        this.dlugosc = dlugosc;
    }

    public int id() {
        return id;
    }

    public Wezel poczatek() {
        return poczatek;
    }

    public Wezel koniec() {
        return koniec;
    }

    public Interwal dlugosc() {
        return dlugosc;
    }

    /**
     * Generuje "ładne" statystyki wypisywane dla użytkownika do dziennika.
     */
    public abstract String wypiszStatystyki();

    public abstract Zdarzenie zdarzenieNastepnegoKroku(Moment moment, Sportowiec sportowiec);

    /**
     * Generuje opis wykorzystywany do pierwszej mapki (parametry).
     */
    public abstract List<String> generujOpisMapkaParametrow();

    /**
     * Generuje opis wykorzystywany do drugiej mapki (statystyki).
     */
    public abstract List<String> generujOpisMapkaStatystyk();

    /**
     * Zwraca oznaczenie pod etykiety mapek (wyciąg - w, trasa - t).
     */
    protected abstract String oznaczenieRodzaju();

    /**
     * Generuje początek etykiety pod mapkę (np. "w1")
     */
    public String etykietaPodMapke() {
        return oznaczenieRodzaju() + id;
    }

    @Override
    public String toString() {
        return "Krawedz [id=" + id + ", poczatek=" + poczatek + ", koniec=" + koniec + ", dlugosc=" + dlugosc + "]";
    }
}
