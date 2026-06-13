package osrodek.krawedz;

import czas.Interwal;
import czas.Moment;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Wezel;
import sportowcy.Sportowiec;

import java.util.ArrayList;

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
    public abstract ArrayList<String> generujOpisMapkaParametrow();

    /**
     * Generuje opis wykorzystywany do drugiej mapki (statystyki).
     */
    public abstract ArrayList<String> generujOpisMapkaStatystyk();

    @Override
    public String toString() {
        return "Krawedz [id=" + id + ", poczatek=" + poczatek + ", koniec=" + koniec + ", dlugosc=" + dlugosc + "]";
    }
}
